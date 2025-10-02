package org.market_mangement.market_management_backend.modules.meter;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

@Setter
@Getter
@Entity
@ToString
@Table(
        name = "meters",
        indexes = {
                @Index(name = "idx_meters_shop", columnList = "shop_id")
        },
        uniqueConstraints = {
                @UniqueConstraint(name = "meters_shop_id_utility_type_serial_key", columnNames = {"shop_id", "utility_type", "serial"})
        }
)
public class Meter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "shop_id", nullable = false)
    private Long shopId;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "utility_type", nullable = false, columnDefinition = "utility_type")
    private UtilityType utilityType;

    @NotNull
    @Size(max = 64)
    @Column(nullable = false, length = 64)
    private String serial;

    @NotNull
    @Positive
    @Column(nullable = false, precision = 10, scale = 4)
    private BigDecimal multiplier = BigDecimal.ONE;

    @Column(nullable = false)
    private Boolean active = true;

    @PrePersist
    protected void onCreate() {
        if (active == null) active = true;
        if (multiplier == null) multiplier = BigDecimal.ONE;
    }
}