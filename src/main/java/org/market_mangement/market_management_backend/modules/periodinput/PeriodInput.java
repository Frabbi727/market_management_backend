package org.market_mangement.market_management_backend.modules.periodinput;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZonedDateTime;

@Setter
@Getter
@Entity
@ToString
@Table(name = "period_inputs")
public class PeriodInput {

    @Id
    @NotNull
    @Column(nullable = false)
    private LocalDate period;

    @NotNull
    @PositiveOrZero
    @Column(name = "total_ac_units", nullable = false, precision = 14, scale = 4)
    private BigDecimal totalAcUnits;

    @NotNull
    @PositiveOrZero
    @Column(name = "ac_unit_price", nullable = false, precision = 12, scale = 4)
    private BigDecimal acUnitPrice;

    @NotNull
    @PositiveOrZero
    @Column(name = "guard_cost", nullable = false, precision = 14, scale = 2)
    private BigDecimal guardCost = BigDecimal.ZERO;

    @NotNull
    @PositiveOrZero
    @Column(name = "maid_cost", nullable = false, precision = 14, scale = 2)
    private BigDecimal maidCost = BigDecimal.ZERO;

    @NotNull
    @PositiveOrZero
    @Column(name = "other_cost", nullable = false, precision = 14, scale = 2)
    private BigDecimal otherCost = BigDecimal.ZERO;

    @Column(name = "market_sqft_override", precision = 14, scale = 2)
    private BigDecimal marketSqftOverride;

    @Column(name = "ac_rate_per_sqft_override", precision = 14, scale = 6)
    private BigDecimal acRatePerSqftOverride;

    @Column(name = "service_rate_per_sqft_override", precision = 14, scale = 6)
    private BigDecimal serviceRatePerSqftOverride;

    @Column(name = "created_at", nullable = false, updatable = false)
    private ZonedDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private ZonedDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        if (guardCost == null) guardCost = BigDecimal.ZERO;
        if (maidCost == null) maidCost = BigDecimal.ZERO;
        if (otherCost == null) otherCost = BigDecimal.ZERO;
        createdAt = ZonedDateTime.now();
        updatedAt = ZonedDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = ZonedDateTime.now();
    }
}