package org.market_mangement.market_management_backend.modules.reading;

import jakarta.persistence.*;
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
@Table(
        name = "readings",
        indexes = {
                @Index(name = "idx_readings_meter", columnList = "meter_id"),
                @Index(name = "idx_readings_period", columnList = "period")
        },
        uniqueConstraints = {
                @UniqueConstraint(name = "readings_meter_id_period_key", columnNames = {"meter_id", "period"})
        }
)
public class Reading {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "meter_id", nullable = false)
    private Long meterId;

    /** Billing period date */
    @Column(nullable = false)
    private LocalDate period;

    @Column(name = "prev_reading", nullable = false, precision = 14, scale = 4)
    private BigDecimal prevReading;

    @Column(name = "curr_reading", nullable = false, precision = 14, scale = 4)
    private BigDecimal currReading;

    @Column(nullable = false, precision = 10, scale = 4)
    private BigDecimal multiplier;

    @Column(nullable = false, precision = 14, scale = 4)
    private BigDecimal consumption;

    @Column(name = "read_at", nullable = false)
    private ZonedDateTime readAt;

    @PrePersist
    protected void onCreate() {
        if (readAt == null) {
            readAt = ZonedDateTime.now();
        }
    }
}