package org.market_mangement.market_management_backend.modules.tariff;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.market_mangement.market_management_backend.modules.meter.UtilityType;

import java.math.BigDecimal;
import java.time.LocalDate;

@Setter
@Getter
@Entity
@ToString
@Table(
        name = "tariffs",
        indexes = {
                @Index(name = "idx_tariffs_utl_eff", columnList = "utility_type, effective_from")
        },
        uniqueConstraints = {
                @UniqueConstraint(name = "tariffs_utility_type_effective_from_key", columnNames = {"utility_type", "effective_from"})
        }
)
public class Tariff {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "utility_type", nullable = false, columnDefinition = "utility_type")
    private UtilityType utilityType;

    @NotNull
    @PositiveOrZero
    @Column(name = "flat_rate_per_unit", nullable = false, precision = 12, scale = 4)
    private BigDecimal flatRatePerUnit;

    @NotNull
    @Column(name = "effective_from", nullable = false)
    private LocalDate effectiveFrom;
}