package org.market_mangement.market_management_backend.modules.billing;

import lombok.RequiredArgsConstructor;
import org.market_mangement.market_management_backend.modules.invoice.*;
import org.market_mangement.market_management_backend.modules.meter.Meter;
import org.market_mangement.market_management_backend.modules.meter.MeterRepository;
import org.market_mangement.market_management_backend.modules.meter.UtilityType;
import org.market_mangement.market_management_backend.modules.periodinput.PeriodInput;
import org.market_mangement.market_management_backend.modules.periodinput.PeriodInputRepository;
import org.market_mangement.market_management_backend.modules.reading.Reading;
import org.market_mangement.market_management_backend.modules.reading.ReadingRepository;
import org.market_mangement.market_management_backend.modules.shop.Shop;
import org.market_mangement.market_management_backend.modules.shop.ShopRepository;
import org.market_mangement.market_management_backend.modules.tariff.Tariff;
import org.market_mangement.market_management_backend.modules.tariff.TariffRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.*;

@Service
@RequiredArgsConstructor
public class BillingService {

    private final ShopRepository shopRepository;
    private final InvoiceRepository invoiceRepository;
    private final InvoiceItemRepository invoiceItemRepository;
    private final ReadingRepository readingRepository;
    private final MeterRepository meterRepository;
    private final PeriodInputRepository periodInputRepository;
    private final TariffRepository tariffRepository;

    @Transactional
    public Map<String, Object> computeBills(String period, Boolean force) {
        // Extract year-month from YYYY-MM-DD
        String monthPeriod = period.substring(0, 7);

        Map<String, Object> result = new HashMap<>();
        List<String> errors = new ArrayList<>();
        List<String> warnings = new ArrayList<>();

        // Validate inputs exist
        Optional<PeriodInput> inputOpt = periodInputRepository.findById(monthPeriod);
        if (inputOpt.isEmpty()) {
            errors.add("Period inputs not found. Please set inputs first.");
            result.put("success", false);
            result.put("errors", errors);
            return result;
        }

        PeriodInput periodInput = inputOpt.get();

        // Validate tariff exists
        List<Tariff> tariffs = tariffRepository.findByUtilityTypeOrderByEffectiveFromDesc(UtilityType.ELECTRIC);
        if (tariffs.isEmpty()) {
            errors.add("No electricity tariff found. Please configure tariff first.");
            result.put("success", false);
            result.put("errors", errors);
            return result;
        }

        Tariff electricityTariff = tariffs.get(0);

        // Get all shops
        List<Shop> shops = shopRepository.findAll();
        if (shops.isEmpty()) {
            errors.add("No shops found.");
            result.put("success", false);
            result.put("errors", errors);
            return result;
        }

        // Check for locked invoices
        List<Long> shopIds = shops.stream().map(Shop::getId).toList();
        List<Invoice> existingInvoices = invoiceRepository.findByPeriodAndShopIdIn(LocalDate.parse(monthPeriod), shopIds);
        long lockedCount = existingInvoices.stream().filter(Invoice::getLocked).count();

        if (lockedCount > 0 && !force) {
            errors.add(lockedCount + " invoices are locked. Use force=true to recompute.");
            result.put("success", false);
            result.put("errors", errors);
            return result;
        }

        // Calculate market totals
        BigDecimal marketTotalSqft = periodInput.getMarketSqftOverride() != null
                ? periodInput.getMarketSqftOverride()
                : shops.stream()
                .map(Shop::getAreaSqft)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        if (marketTotalSqft.compareTo(BigDecimal.ZERO) == 0) {
            errors.add("Market total sqft is zero. Cannot compute bills.");
            result.put("success", false);
            result.put("errors", errors);
            return result;
        }

        // Calculate AC per sqft rate
        BigDecimal acPerSqftRate;
        if (periodInput.getAcRatePerSqftOverride() != null) {
            acPerSqftRate = periodInput.getAcRatePerSqftOverride();
        } else {
            BigDecimal totalAcCost = periodInput.getTotalAcUnits().multiply(periodInput.getAcUnitPrice());
            acPerSqftRate = totalAcCost.divide(marketTotalSqft, 6, RoundingMode.HALF_UP);
        }

        // Calculate service per sqft rate
        BigDecimal servicePerSqftRate;
        if (periodInput.getServiceRatePerSqftOverride() != null) {
            servicePerSqftRate = periodInput.getServiceRatePerSqftOverride();
        } else {
            BigDecimal totalServiceCost = periodInput.getGuardCost()
                    .add(periodInput.getMaidCost())
                    .add(periodInput.getOtherCost());
            servicePerSqftRate = totalServiceCost.divide(marketTotalSqft, 6, RoundingMode.HALF_UP);
        }

        // Process each shop
        int processedCount = 0;
        int skippedCount = 0;

        for (Shop shop : shops) {
            try {
                // Skip if invoice is locked
                Optional<Invoice> existingInvoice = invoiceRepository.findByPeriodAndShopId(LocalDate.parse(monthPeriod), shop.getId());
                if (existingInvoice.isPresent() && existingInvoice.get().getLocked() && !force) {
                    skippedCount++;
                    continue;
                }

                // Get meter for this shop
                List<Meter> meters = meterRepository.findByShopId(shop.getId());
                if (meters.isEmpty()) {
                    warnings.add("Shop " + shop.getCode() + " has no meter. Skipped.");
                    skippedCount++;
                    continue;
                }

                Meter meter = meters.get(0); // Use first meter

                // Get reading
                Optional<Reading> readingOpt = readingRepository.findByMeterIdAndPeriod(meter.getId(), LocalDate.parse(monthPeriod));
                if (readingOpt.isEmpty()) {
                    warnings.add("Shop " + shop.getCode() + " has no reading. Skipped.");
                    skippedCount++;
                    continue;
                }

                Reading reading = readingOpt.get();

                // Calculate shop area
                BigDecimal shopSqft = shop.getAreaSqft() != null ? shop.getAreaSqft() : BigDecimal.ZERO;

                // Calculate electricity amount
                BigDecimal electricityUnits = reading.getConsumption();
                BigDecimal electricityAmount = electricityUnits.multiply(electricityTariff.getFlatRatePerUnit())
                        .setScale(2, RoundingMode.HALF_UP);

                // Calculate AC amount
                BigDecimal acAmount = shopSqft.multiply(acPerSqftRate)
                        .setScale(2, RoundingMode.HALF_UP);

                // Calculate service amount
                BigDecimal serviceAmount = shopSqft.multiply(servicePerSqftRate)
                        .setScale(2, RoundingMode.HALF_UP);

                // Calculate total
                BigDecimal total = electricityAmount.add(acAmount).add(serviceAmount);

                // Create or update invoice
                Invoice invoice;
                if (existingInvoice.isPresent()) {
                    invoice = existingInvoice.get();
                    invoice.setTotal(total);
                    invoice.setRevision(invoice.getRevision() + 1);
                } else {
                    invoice = new Invoice();
                    invoice.setPeriod(LocalDate.parse(monthPeriod));
                    invoice.setShopId(shop.getId());
                    invoice.setTotal(total);
                    invoice.setStatus("UNPAID");
                    invoice.setLocked(false);
                }

                invoice = invoiceRepository.save(invoice);

                // Delete existing items if updating
                if (existingInvoice.isPresent()) {
                    List<InvoiceItem> existingItems = invoiceItemRepository.findByInvoiceId(invoice.getId());
                    invoiceItemRepository.deleteAll(existingItems);
                }

                // Create invoice items
                InvoiceItem electricityItem = new InvoiceItem();
                electricityItem.setInvoiceId(invoice.getId());
                electricityItem.setItemType(ItemType.ELECTRICITY);
                electricityItem.setDescription("Electricity consumption");
                electricityItem.setQuantity(electricityUnits);
                electricityItem.setUnit("kWh");
                electricityItem.setUnitPrice(electricityTariff.getFlatRatePerUnit());
                electricityItem.setAmount(electricityAmount);
                electricityItem.setIsOverridden(false);
                invoiceItemRepository.save(electricityItem);

                InvoiceItem acItem = new InvoiceItem();
                acItem.setInvoiceId(invoice.getId());
                acItem.setItemType(ItemType.AC);
                acItem.setDescription("AC charges");
                acItem.setQuantity(shopSqft);
                acItem.setUnit("sqft");
                acItem.setUnitPrice(acPerSqftRate);
                acItem.setAmount(acAmount);
                acItem.setIsOverridden(false);
                invoiceItemRepository.save(acItem);

                InvoiceItem serviceItem = new InvoiceItem();
                serviceItem.setInvoiceId(invoice.getId());
                serviceItem.setItemType(ItemType.SERVICE);
                serviceItem.setDescription("Service charges (Guard, Maid, Other)");
                serviceItem.setQuantity(shopSqft);
                serviceItem.setUnit("sqft");
                serviceItem.setUnitPrice(servicePerSqftRate);
                serviceItem.setAmount(serviceAmount);
                serviceItem.setIsOverridden(false);
                invoiceItemRepository.save(serviceItem);

                processedCount++;

            } catch (Exception e) {
                warnings.add("Error processing shop " + shop.getCode() + ": " + e.getMessage());
                skippedCount++;
            }
        }

        result.put("success", true);
        result.put("processedCount", processedCount);
        result.put("skippedCount", skippedCount);
        result.put("warnings", warnings);
        result.put("errors", errors);

        return result;
    }
}