package org.market_mangement.market_management_backend.modules.reports;

import lombok.RequiredArgsConstructor;
import org.market_mangement.market_management_backend.modules.invoice.*;
import org.market_mangement.market_management_backend.modules.meter.Meter;
import org.market_mangement.market_management_backend.modules.meter.MeterRepository;
import org.market_mangement.market_management_backend.modules.periodinput.PeriodInput;
import org.market_mangement.market_management_backend.modules.periodinput.PeriodInputRepository;
import org.market_mangement.market_management_backend.modules.reading.Reading;
import org.market_mangement.market_management_backend.modules.reading.ReadingRepository;
import org.market_mangement.market_management_backend.modules.reports.dto.*;
import org.market_mangement.market_management_backend.modules.shop.Shop;
import org.market_mangement.market_management_backend.modules.shop.ShopRepository;
import org.market_mangement.market_management_backend.modules.tariff.TariffRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReportsService {

    private final ShopRepository shopRepository;
    private final InvoiceRepository invoiceRepository;
    private final InvoiceItemRepository invoiceItemRepository;
    private final ReadingRepository readingRepository;
    private final MeterRepository meterRepository;
    private final PeriodInputRepository periodInputRepository;
    private final TariffRepository tariffRepository;

    public MarketSummaryDto getMarketSummary(String market, LocalDate period) {
        // Get all shops in the market
        List<Shop> shops = shopRepository.findByMarket(market);
        List<Long> shopIds = shops.stream().map(Shop::getId).collect(Collectors.toList());

        // Get all invoices for this period and market
        List<Invoice> invoices = invoiceRepository.findByPeriodAndShopIdIn(period, shopIds);

        // Get all invoice items for these invoices
        List<Long> invoiceIds = invoices.stream().map(Invoice::getId).collect(Collectors.toList());
        List<InvoiceItem> invoiceItems = invoiceItemRepository.findByInvoiceIdIn(invoiceIds);

        // Build KPI data
        KpiCardDto kpis = buildKpis(invoices, invoiceItems);

        // Build health panel
        HealthPanelDto health = buildHealthPanel(market, period, shops, invoices);

        // Build inputs snapshot
        InputsSnapshotDto inputs = buildInputsSnapshot(period, shops);

        return MarketSummaryDto.builder()
                .kpis(kpis)
                .health(health)
                .inputs(inputs)
                .build();
    }

    public InvoicePageDto getInvoicesTable(String market, LocalDate period, Integer page, Integer size) {
        // Get shops in market
        List<Shop> shops = shopRepository.findByMarket(market);
        List<Long> shopIds = shops.stream().map(Shop::getId).collect(Collectors.toList());

        // Create shop map for quick lookup
        Map<Long, Shop> shopMap = shops.stream()
                .collect(Collectors.toMap(Shop::getId, shop -> shop));

        // Get paginated invoices
        Pageable pageable = PageRequest.of(page, size);
        Page<Invoice> invoicePage = invoiceRepository.findByPeriodAndShopIdIn(period, shopIds, pageable);

        // Get invoice items for this page
        List<Long> invoiceIds = invoicePage.getContent().stream()
                .map(Invoice::getId)
                .collect(Collectors.toList());
        List<InvoiceItem> items = invoiceItemRepository.findByInvoiceIdIn(invoiceIds);

        // Group items by invoice
        Map<Long, List<InvoiceItem>> itemsByInvoice = items.stream()
                .collect(Collectors.groupingBy(InvoiceItem::getInvoiceId));

        // Build DTOs
        List<InvoiceTableDto> content = invoicePage.getContent().stream()
                .map(invoice -> {
                    Shop shop = shopMap.get(invoice.getShopId());
                    List<InvoiceItem> invoiceItems = itemsByInvoice.getOrDefault(invoice.getId(), Collections.emptyList());

                    BigDecimal elecAmount = getAmountByType(invoiceItems, ItemType.ELECTRICITY);
                    BigDecimal acAmount = getAmountByType(invoiceItems, ItemType.AC);
                    BigDecimal serviceAmount = getAmountByType(invoiceItems, ItemType.SERVICE);
                    boolean hasOverride = invoiceItems.stream().anyMatch(InvoiceItem::getIsOverridden);

                    return InvoiceTableDto.builder()
                            .invoiceId(invoice.getId())
                            .shopCode(shop != null ? shop.getCode() : "")
                            .shopName(shop != null ? shop.getShopName() : "")
                            .electricityAmount(elecAmount)
                            .acAmount(acAmount)
                            .serviceAmount(serviceAmount)
                            .total(invoice.getTotal())
                            .status(invoice.getStatus())
                            .locked(invoice.getLocked())
                            .hasOverride(hasOverride)
                            .build();
                })
                .collect(Collectors.toList());

        return InvoicePageDto.builder()
                .content(content)
                .pageNumber(invoicePage.getNumber())
                .pageSize(invoicePage.getSize())
                .totalElements(invoicePage.getTotalElements())
                .totalPages(invoicePage.getTotalPages())
                .build();
    }

    public List<ReadingStatusDto> getReadingStatus(String market, LocalDate period) {
        // Get all shops in the market
        List<Shop> shops = shopRepository.findByMarket(market);
        Map<Long, Shop> shopMap = shops.stream()
                .collect(Collectors.toMap(Shop::getId, shop -> shop));

        // Get all meters for these shops
        List<Long> shopIds = shops.stream().map(Shop::getId).collect(Collectors.toList());
        List<Meter> meters = meterRepository.findByShopIdIn(shopIds);

        // Get all readings for this period date
        List<Long> meterIds = meters.stream().map(Meter::getId).collect(Collectors.toList());
        List<Reading> readings = readingRepository.findByMeterIdInAndPeriod(meterIds, period);

        // Create reading map
        Map<Long, Reading> readingMap = readings.stream()
                .collect(Collectors.toMap(Reading::getMeterId, r -> r, (existing, replacement) -> existing));

        // Build status DTOs
        return meters.stream()
                .map(meter -> {
                    Shop shop = shopMap.get(meter.getShopId());
                    Reading reading = readingMap.get(meter.getId());
                    boolean missing = reading == null;

                    return ReadingStatusDto.builder()
                            .shopCode(shop != null ? shop.getCode() : "")
                            .shopName(shop != null ? shop.getShopName() : "")
                            .meterNumber(meter.getSerial())
                            .prevReading(reading != null ? reading.getPrevReading() : null)
                            .currReading(reading != null ? reading.getCurrReading() : null)
                            .units(reading != null ? reading.getConsumption() : null)
                            .missing(missing)
                            .build();
                })
                .collect(Collectors.toList());
    }

    // Helper methods

    private KpiCardDto buildKpis(List<Invoice> invoices, List<InvoiceItem> items) {
        Map<Long, List<InvoiceItem>> itemsByInvoice = items.stream()
                .collect(Collectors.groupingBy(InvoiceItem::getInvoiceId));

        BigDecimal totalAmount = invoices.stream()
                .map(Invoice::getTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal electricityAmount = BigDecimal.ZERO;
        BigDecimal electricityUnits = BigDecimal.ZERO;
        BigDecimal acCost = BigDecimal.ZERO;
        BigDecimal serviceCost = BigDecimal.ZERO;

        for (Invoice invoice : invoices) {
            List<InvoiceItem> invoiceItems = itemsByInvoice.getOrDefault(invoice.getId(), Collections.emptyList());

            for (InvoiceItem item : invoiceItems) {
                if (item.getItemType() == ItemType.ELECTRICITY) {
                    electricityAmount = electricityAmount.add(item.getAmount());
                    if (item.getQuantity() != null) {
                        electricityUnits = electricityUnits.add(item.getQuantity());
                    }
                } else if (item.getItemType() == ItemType.AC) {
                    acCost = acCost.add(item.getAmount());
                } else if (item.getItemType() == ItemType.SERVICE) {
                    serviceCost = serviceCost.add(item.getAmount());
                }
            }
        }

        return KpiCardDto.builder()
                .invoiceCount(invoices.size())
                .totalAmount(totalAmount)
                .electricityUnits(electricityUnits)
                .electricityAmount(electricityAmount)
                .acCost(acCost)
                .serviceCost(serviceCost)
                .build();
    }

    private HealthPanelDto buildHealthPanel(String market, LocalDate period, List<Shop> shops, List<Invoice> invoices) {
        // Check if inputs exist
        Optional<PeriodInput> inputOpt = periodInputRepository.findById(String.valueOf(period));
        boolean inputsOk = inputOpt.isPresent();

        // Check for missing readings
        List<Long> shopIds = shops.stream().map(Shop::getId).collect(Collectors.toList());
        List<Meter> meters = meterRepository.findByShopIdIn(shopIds);
        List<Long> meterIds = meters.stream().map(Meter::getId).collect(Collectors.toList());
        List<Reading> readings = readingRepository.findByMeterIdInAndPeriod(meterIds, period);
        int missingReadingsCount = meters.size() - readings.size();

        // Check if tariff exists (assuming at least one tariff should exist)
        boolean tariffOk = tariffRepository.count() > 0;

        // Count unlocked invoices
        long unlockedCount = invoices.stream()
                .filter(inv -> !inv.getLocked())
                .count();

        return HealthPanelDto.builder()
                .inputsOk(inputsOk)
                .missingReadingsCount(missingReadingsCount)
                .tariffOk(tariffOk)
                .unlockedInvoicesCount((int) unlockedCount)
                .build();
    }

    private InputsSnapshotDto buildInputsSnapshot(LocalDate period, List<Shop> shops) {
        Optional<PeriodInput> inputOpt = periodInputRepository.findById(String.valueOf(period));

        if (inputOpt.isEmpty()) {
            return InputsSnapshotDto.builder().build();
        }

        PeriodInput input = inputOpt.get();

        // Calculate market total sqft
        BigDecimal marketTotalSqft = shops.stream()
                .map(Shop::getAreaSqft)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Use override if present, otherwise use calculated
        if (input.getMarketSqftOverride() != null) {
            marketTotalSqft = input.getMarketSqftOverride();
        }

        // Calculate AC per sqft rate
        BigDecimal acPerSqftRate = BigDecimal.ZERO;
        if (input.getAcRatePerSqftOverride() != null) {
            acPerSqftRate = input.getAcRatePerSqftOverride();
        } else if (marketTotalSqft.compareTo(BigDecimal.ZERO) > 0) {
            BigDecimal totalAcCost = input.getTotalAcUnits().multiply(input.getAcUnitPrice());
            acPerSqftRate = totalAcCost.divide(marketTotalSqft, 6, java.math.RoundingMode.HALF_UP);
        }

        // Calculate service per sqft rate
        BigDecimal servicePerSqftRate = BigDecimal.ZERO;
        if (input.getServiceRatePerSqftOverride() != null) {
            servicePerSqftRate = input.getServiceRatePerSqftOverride();
        } else if (marketTotalSqft.compareTo(BigDecimal.ZERO) > 0) {
            BigDecimal totalServiceCost = input.getGuardCost()
                    .add(input.getMaidCost())
                    .add(input.getOtherCost());
            servicePerSqftRate = totalServiceCost.divide(marketTotalSqft, 6, java.math.RoundingMode.HALF_UP);
        }

        return InputsSnapshotDto.builder()
                .acTotalUnits(input.getTotalAcUnits())
                .acUnitPrice(input.getAcUnitPrice())
                .acPerSqftRate(acPerSqftRate)
                .guardCost(input.getGuardCost())
                .maidCost(input.getMaidCost())
                .otherCost(input.getOtherCost())
                .servicePerSqftRate(servicePerSqftRate)
                .marketTotalSqft(marketTotalSqft)
                .build();
    }

    private BigDecimal getAmountByType(List<InvoiceItem> items, ItemType type) {
        return items.stream()
                .filter(item -> item.getItemType() == type)
                .map(InvoiceItem::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}