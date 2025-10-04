package org.market_mangement.market_management_backend.modules.monthlycost;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.market_mangement.market_management_backend.modules.market.Market;
import org.market_mangement.market_management_backend.modules.market.MarketRepository;
import org.market_mangement.market_management_backend.modules.shop.Shop;
import org.market_mangement.market_management_backend.modules.shop.ShopRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MonthlyCostService {

    private final MonthlyCostRepository monthlyCostRepository;
    private final MarketRepository marketRepository;
    private final ShopRepository shopRepository;

    /**
     * Create new monthly cost configuration
     */
    @Transactional
    public MonthlyCost create(MonthlyCostCreateDTO dto) {
        // Check if configuration already exists for this market, year, and month
        if (monthlyCostRepository.existsByMarket_IdAndYearAndMonth(dto.getMarketId(), dto.getYear(), dto.getMonth())) {
            throw new IllegalArgumentException("Monthly cost configuration already exists for this market, year, and month");
        }

        // Fetch market
        Market market = marketRepository.findById(dto.getMarketId())
                .orElseThrow(() -> new EntityNotFoundException("Market not found with id: " + dto.getMarketId()));

        // Create entity
        MonthlyCost monthlyCost = new MonthlyCost();
        monthlyCost.setMarket(market);
        monthlyCost.setYear(dto.getYear());
        monthlyCost.setMonth(dto.getMonth());
        monthlyCost.setUseAutoCalculation(dto.getUseAutoCalculation());

        // Calculate or use manual total sqft
        if (dto.getUseAutoCalculation()) {
            BigDecimal calculatedSqft = calculateTotalSqft(dto.getMarketId());
            monthlyCost.setTotalBillingSqft(calculatedSqft);
        } else {
            monthlyCost.setTotalBillingSqft(dto.getTotalBillingSqft());
        }

        // Set electricity
        monthlyCost.setElectricityRate(dto.getElectricityRate());

        // Set AC
        monthlyCost.setAcEnabled(dto.getAcEnabled());
        monthlyCost.setTotalAcCost(dto.getTotalAcCost() != null ? dto.getTotalAcCost() : BigDecimal.ZERO);

        // Set service
        monthlyCost.setServiceEnabled(dto.getServiceEnabled());
        monthlyCost.setTotalServiceCost(dto.getTotalServiceCost() != null ? dto.getTotalServiceCost() : BigDecimal.ZERO);

        // Set generator
        monthlyCost.setGeneratorEnabled(dto.getGeneratorEnabled());
        monthlyCost.setTotalGeneratorCost(dto.getTotalGeneratorCost() != null ? dto.getTotalGeneratorCost() : BigDecimal.ZERO);

        // Set special
        monthlyCost.setSpecialEnabled(dto.getSpecialEnabled());
        monthlyCost.setTotalSpecialCost(dto.getTotalSpecialCost() != null ? dto.getTotalSpecialCost() : BigDecimal.ZERO);
        monthlyCost.setSpecialCostName(dto.getSpecialCostName());
        monthlyCost.setSpecialCostRemarks(dto.getSpecialCostRemarks());

        // Set dates
        monthlyCost.setIssueDate(dto.getIssueDate());
        monthlyCost.setDueDate(dto.getDueDate());

        return monthlyCostRepository.save(monthlyCost);
    }

    /**
     * Update existing monthly cost configuration
     */
    @Transactional
    public MonthlyCost update(Long id, MonthlyCostUpdateDTO dto) {
        MonthlyCost monthlyCost = monthlyCostRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Monthly cost not found with id: " + id));

        // Check if locked
        if (monthlyCost.getLocked()) {
            throw new IllegalStateException("Cannot update locked monthly cost configuration");
        }

        // Update total sqft
        if (dto.getUseAutoCalculation() != null) {
            monthlyCost.setUseAutoCalculation(dto.getUseAutoCalculation());
        }

        if (monthlyCost.getUseAutoCalculation()) {
            BigDecimal calculatedSqft = calculateTotalSqft(monthlyCost.getMarket().getId());
            monthlyCost.setTotalBillingSqft(calculatedSqft);
        } else {
            monthlyCost.setTotalBillingSqft(dto.getTotalBillingSqft());
        }

        // Update electricity
        monthlyCost.setElectricityRate(dto.getElectricityRate());

        // Update AC
        if (dto.getAcEnabled() != null) {
            monthlyCost.setAcEnabled(dto.getAcEnabled());
        }
        if (dto.getTotalAcCost() != null) {
            monthlyCost.setTotalAcCost(dto.getTotalAcCost());
        }

        // Update service
        if (dto.getServiceEnabled() != null) {
            monthlyCost.setServiceEnabled(dto.getServiceEnabled());
        }
        if (dto.getTotalServiceCost() != null) {
            monthlyCost.setTotalServiceCost(dto.getTotalServiceCost());
        }

        // Update generator
        if (dto.getGeneratorEnabled() != null) {
            monthlyCost.setGeneratorEnabled(dto.getGeneratorEnabled());
        }
        if (dto.getTotalGeneratorCost() != null) {
            monthlyCost.setTotalGeneratorCost(dto.getTotalGeneratorCost());
        }

        // Update special
        if (dto.getSpecialEnabled() != null) {
            monthlyCost.setSpecialEnabled(dto.getSpecialEnabled());
        }
        if (dto.getTotalSpecialCost() != null) {
            monthlyCost.setTotalSpecialCost(dto.getTotalSpecialCost());
        }
        if (dto.getSpecialCostName() != null) {
            monthlyCost.setSpecialCostName(dto.getSpecialCostName());
        }
        if (dto.getSpecialCostRemarks() != null) {
            monthlyCost.setSpecialCostRemarks(dto.getSpecialCostRemarks());
        }

        // Update dates
        monthlyCost.setIssueDate(dto.getIssueDate());
        monthlyCost.setDueDate(dto.getDueDate());

        return monthlyCostRepository.save(monthlyCost);
    }

    /**
     * Get all monthly costs for a market
     */
    @Transactional(readOnly = true)
    public List<MonthlyCost> getAllByMarket(Long marketId, Integer year, Integer month) {
        if (year != null && month != null) {
            // Get specific month
            return monthlyCostRepository.findByMarket_IdAndYearAndMonth(marketId, year, month)
                    .map(List::of)
                    .orElse(List.of());
        } else if (year != null) {
            // Get all for year
            return monthlyCostRepository.findByMarket_IdAndYearOrderByMonthDesc(marketId, year);
        } else {
            // Get all
            return monthlyCostRepository.findByMarket_IdOrderByYearDescMonthDesc(marketId);
        }
    }

    /**
     * Get single monthly cost by ID
     */
    @Transactional(readOnly = true)
    public MonthlyCost getById(Long id) {
        return monthlyCostRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Monthly cost not found with id: " + id));
    }

    /**
     * Delete monthly cost configuration
     */
    @Transactional
    public void delete(Long id) {
        MonthlyCost monthlyCost = monthlyCostRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Monthly cost not found with id: " + id));

        // Check if locked
        if (monthlyCost.getLocked()) {
            throw new IllegalStateException("Cannot delete locked monthly cost configuration. Unlock it first.");
        }

        monthlyCostRepository.delete(monthlyCost);
    }

    /**
     * Lock monthly cost configuration
     */
    @Transactional
    public MonthlyCost lock(Long id) {
        MonthlyCost monthlyCost = monthlyCostRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Monthly cost not found with id: " + id));

        monthlyCost.setLocked(true);
        return monthlyCostRepository.save(monthlyCost);
    }

    /**
     * Unlock monthly cost configuration
     */
    @Transactional
    public MonthlyCost unlock(Long id) {
        MonthlyCost monthlyCost = monthlyCostRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Monthly cost not found with id: " + id));

        monthlyCost.setLocked(false);
        return monthlyCostRepository.save(monthlyCost);
    }

    /**
     * Calculate total sqft from all active shops in market
     */
    private BigDecimal calculateTotalSqft(Long marketId) {
        List<Shop> shops = shopRepository.findByMarketId(marketId);

        return shops.stream()
                .filter(Shop::getActive)
                .map(Shop::getAreaSqft)
                .filter(sqft -> sqft != null)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}