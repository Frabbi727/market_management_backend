package org.market_mangement.market_management_backend.modules.invoice;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
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
        name = "invoice_items",
        indexes = {
                @Index(name = "idx_items_invoice", columnList = "invoice_id"),
                @Index(name = "idx_items_overridden", columnList = "is_overridden")
        },
        uniqueConstraints = {
                @UniqueConstraint(name = "invoice_items_invoice_id_item_type_key", columnNames = {"invoice_id", "item_type"})
        }
)
public class InvoiceItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "invoice_id", nullable = false)
    private Long invoiceId;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "item_type", nullable = false, columnDefinition = "item_type")
    private ItemType itemType;

    @Size(max = 160)
    @Column(length = 160)
    private String description;

    @Column(precision = 14, scale = 4)
    private BigDecimal quantity;

    @Size(max = 16)
    @Column(length = 16)
    private String unit;

    @Column(name = "unit_price", precision = 14, scale = 6)
    private BigDecimal unitPrice;

    @NotNull
    @Column(nullable = false, precision = 14, scale = 2)
    private BigDecimal amount;

    @Column(columnDefinition = "jsonb")
    private String basis;

    @Column(columnDefinition = "jsonb")
    private String meta;

    @Column(name = "is_overridden", nullable = false)
    private Boolean isOverridden = false;

    @Column(name = "override_reason", columnDefinition = "text")
    private String overrideReason;

    @PrePersist
    protected void onCreate() {
        if (isOverridden == null) isOverridden = false;
    }
}