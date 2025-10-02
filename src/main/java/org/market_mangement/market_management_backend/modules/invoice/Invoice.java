package org.market_mangement.market_management_backend.modules.invoice;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.Type;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;

@Setter
@Getter
@Entity
@ToString
@Table(
        name = "invoices",
        indexes = {
                @Index(name = "idx_invoices_period", columnList = "period"),
                @Index(name = "idx_invoices_shop", columnList = "shop_id"),
                @Index(name = "idx_invoices_locked", columnList = "locked")
        },
        uniqueConstraints = {
                @UniqueConstraint(name = "invoices_period_shop_id_key", columnNames = {"period", "shop_id"})
        }
)
public class Invoice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(min = 7, max = 7)
    @Column(nullable = false, length = 7, columnDefinition = "char(7)")
    private String period;

    @NotNull
    @Column(name = "shop_id", nullable = false)
    private Long shopId;

    @Column(nullable = false, precision = 14, scale = 2)
    private BigDecimal total = BigDecimal.ZERO;

    @Size(max = 16)
    @Column(nullable = false, length = 16)
    private String status = "UNPAID";

    @Column(nullable = false)
    private Boolean locked = false;

    @Column(nullable = false)
    private Integer revision = 1;

    @Column(nullable = false, columnDefinition = "jsonb")
    private String meta = "{}";

    @Column(name = "created_at", nullable = false, updatable = false)
    private ZonedDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private ZonedDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        if (total == null) total = BigDecimal.ZERO;
        if (status == null) status = "UNPAID";
        if (locked == null) locked = false;
        if (revision == null) revision = 1;
        if (meta == null) meta = "{}";
        createdAt = ZonedDateTime.now();
        updatedAt = ZonedDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = ZonedDateTime.now();
    }
}