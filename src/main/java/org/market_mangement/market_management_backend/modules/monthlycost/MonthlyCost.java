package org.market_mangement.market_management_backend.modules.monthlycost;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import org.market_mangement.market_management_backend.modules.market.Market;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.ZonedDateTime;

@Setter
@Getter
@Entity
@Table(
        name = "monthly_costs",
        uniqueConstraints = {
                @UniqueConstraint(name = "unique_market_year_month", columnNames = {"market_id", "year", "month"})
        },
        indexes = {
                @Index(name = "idx_monthly_costs_market_id", columnList = "market_id"),
                @Index(name = "idx_monthly_costs_period", columnList = "year, month"),
                @Index(name = "idx_monthly_costs_locked", columnList = "locked")
        }
)
public class MonthlyCost {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Market relationship
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "market_id", nullable = false)
    @NotNull(message = "Market is required")
    private Market market;

    /**
     * Year (e.g., 2025)
     */
    @NotNull(message = "Year is required")
    @Min(value = 2000, message = "Year must be at least 2000")
    @Max(value = 2100, message = "Year must be at most 2100")
    @Column(nullable = false)
    private Integer year;

    /**
     * Month (1-12)
     */
    @NotNull(message = "Month is required")
    @Min(value = 1, message = "Month must be between 1 and 12")
    @Max(value = 12, message = "Month must be between 1 and 12")
    @Column(nullable = false)
    private Integer month;

    /**
     * Period date (auto-generated from year and month)
     * Stored as first day of the month
     */
    @Column(nullable = false)
    private LocalDate period;

    /**
     * Total billing square footage
     * Can be auto-calculated or manually entered
     */
    @NotNull(message = "Total billing sqft is required")
    @DecimalMin(value = "0.01", message = "Total billing sqft must be greater than 0")
    @Column(name = "total_billing_sqft", precision = 10, scale = 2, nullable = false)
    private BigDecimal totalBillingSqft;

    /**
     * If true, auto-calculate totalBillingSqft from all active shops
     * If false, use the manually entered totalBillingSqft
     */
    @Column(name = "use_auto_calculation", nullable = false)
    private Boolean useAutoCalculation = true;

    // ========== ELECTRICITY ==========

    /**
     * Electricity rate per kwh (e.g., 15.20 Tk/kwh)
     */
    @NotNull(message = "Electricity rate is required")
    @DecimalMin(value = "0.00", message = "Electricity rate cannot be negative")
    @Column(name = "electricity_rate", precision = 10, scale = 2, nullable = false)
    private BigDecimal electricityRate;

    // ========== AC CHARGES ==========

    @Column(name = "ac_enabled", nullable = false)
    private Boolean acEnabled = true;

    /**
     * Total AC cost for the month (e.g., 85,000 Tk)
     */
    @DecimalMin(value = "0.00", message = "AC cost cannot be negative")
    @Column(name = "total_ac_cost", precision = 10, scale = 2)
    private BigDecimal totalAcCost = BigDecimal.ZERO;

    /**
     * Auto-calculated AC rate per sqft
     * = totalAcCost / totalBillingSqft
     */
    @Column(name = "ac_rate_per_sqft", precision = 10, scale = 2)
    private BigDecimal acRatePerSqft = BigDecimal.ZERO;

    // ========== SERVICE CHARGES ==========

    @Column(name = "service_enabled", nullable = false)
    private Boolean serviceEnabled = true;

    /**
     * Total service cost for the month (e.g., 180,000 Tk)
     */
    @DecimalMin(value = "0.00", message = "Service cost cannot be negative")
    @Column(name = "total_service_cost", precision = 10, scale = 2)
    private BigDecimal totalServiceCost = BigDecimal.ZERO;

    /**
     * Auto-calculated service rate per sqft
     */
    @Column(name = "service_rate_per_sqft", precision = 10, scale = 2)
    private BigDecimal serviceRatePerSqft = BigDecimal.ZERO;

    // ========== GENERATOR CHARGES ==========

    @Column(name = "generator_enabled", nullable = false)
    private Boolean generatorEnabled = true;

    /**
     * Total generator cost for the month (e.g., 6,300 Tk)
     */
    @DecimalMin(value = "0.00", message = "Generator cost cannot be negative")
    @Column(name = "total_generator_cost", precision = 10, scale = 2)
    private BigDecimal totalGeneratorCost = BigDecimal.ZERO;

    /**
     * Auto-calculated generator rate per sqft
     */
    @Column(name = "generator_rate_per_sqft", precision = 10, scale = 2)
    private BigDecimal generatorRatePerSqft = BigDecimal.ZERO;

    // ========== SPECIAL CHARGES ==========

    @Column(name = "special_enabled", nullable = false)
    private Boolean specialEnabled = false;

    /**
     * Total special cost for the month (optional)
     */
    @DecimalMin(value = "0.00", message = "Special cost cannot be negative")
    @Column(name = "total_special_cost", precision = 10, scale = 2)
    private BigDecimal totalSpecialCost = BigDecimal.ZERO;

    /**
     * Auto-calculated special rate per sqft
     */
    @Column(name = "special_rate_per_sqft", precision = 10, scale = 2)
    private BigDecimal specialRatePerSqft = BigDecimal.ZERO;

    /**
     * Name of the special charge (e.g., "Building Maintenance")
     */
    @Size(max = 200)
    @Column(name = "special_cost_name", length = 200)
    private String specialCostName;

    /**
     * Remarks/description for special charge
     */
    @Size(max = 1000)
    @Column(name = "special_cost_remarks", columnDefinition = "text")
    private String specialCostRemarks;

    // ========== DATES ==========

    /**
     * Bill issue date (e.g., 2025-12-05)
     */
    @NotNull(message = "Issue date is required")
    @Column(name = "issue_date", nullable = false)
    private LocalDate issueDate;

    /**
     * Bill due/payment date (e.g., 2025-12-25)
     */
    @NotNull(message = "Due date is required")
    @Column(name = "due_date", nullable = false)
    private LocalDate dueDate;

    // ========== STATUS ==========

    /**
     * Lock to prevent changes after bill generation
     */
    @Column(nullable = false)
    private Boolean locked = false;

    @Column(name = "created_at", nullable = false, updatable = false)
    private ZonedDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private ZonedDateTime updatedAt;

    // ========== LIFECYCLE HOOKS ==========

    @PrePersist
    protected void onCreate() {
        if (useAutoCalculation == null) useAutoCalculation = true;
        if (acEnabled == null) acEnabled = true;
        if (serviceEnabled == null) serviceEnabled = true;
        if (generatorEnabled == null) generatorEnabled = true;
        if (specialEnabled == null) specialEnabled = false;
        if (locked == null) locked = false;

        // Generate period from year and month
        if (year != null && month != null) {
            this.period = LocalDate.of(year, month, 1);
        }

        calculateRatesPerSqft();

        createdAt = ZonedDateTime.now();
        updatedAt = ZonedDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        // Update period if year/month changed
        if (year != null && month != null) {
            this.period = LocalDate.of(year, month, 1);
        }

        calculateRatesPerSqft();

        updatedAt = ZonedDateTime.now();
    }

    // ========== HELPER METHODS ==========

    /**
     * Calculate all rates per sqft based on total costs
     */
    public void calculateRatesPerSqft() {
        if (totalBillingSqft == null || totalBillingSqft.compareTo(BigDecimal.ZERO) <= 0) {
            return;
        }

        // AC rate per sqft
        if (totalAcCost != null && acEnabled) {
            acRatePerSqft = totalAcCost.divide(totalBillingSqft, 2, RoundingMode.HALF_UP);
        } else {
            acRatePerSqft = BigDecimal.ZERO;
        }

        // Service rate per sqft
        if (totalServiceCost != null && serviceEnabled) {
            serviceRatePerSqft = totalServiceCost.divide(totalBillingSqft, 2, RoundingMode.HALF_UP);
        } else {
            serviceRatePerSqft = BigDecimal.ZERO;
        }

        // Generator rate per sqft
        if (totalGeneratorCost != null && generatorEnabled) {
            generatorRatePerSqft = totalGeneratorCost.divide(totalBillingSqft, 2, RoundingMode.HALF_UP);
        } else {
            generatorRatePerSqft = BigDecimal.ZERO;
        }

        // Special rate per sqft
        if (totalSpecialCost != null && specialEnabled) {
            specialRatePerSqft = totalSpecialCost.divide(totalBillingSqft, 2, RoundingMode.HALF_UP);
        } else {
            specialRatePerSqft = BigDecimal.ZERO;
        }
    }

    /**
     * Get month name (for display)
     */
    @Transient
    public String getMonthName() {
        if (month == null) return null;
        String[] months = {"January", "February", "March", "April", "May", "June",
                "July", "August", "September", "October", "November", "December"};
        return months[month - 1];
    }
}