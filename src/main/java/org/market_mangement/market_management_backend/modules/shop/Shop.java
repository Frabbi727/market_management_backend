package org.market_mangement.market_management_backend.modules.shop;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

@Setter
@Getter
@Entity
@ToString
@Table(
        name = "shops",
        indexes = {
                @Index(name = "idx_shops_market", columnList = "market"),
                @Index(name = "idx_shops_floor", columnList = "floor"),
                @Index(name = "idx_shops_side", columnList = "side"),
                @Index(name = "idx_shops_location_no", columnList = "location_no"),
                @Index(name = "idx_shops_owner", columnList = "owner_name"),
        }
)
public class Shop {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Unique shop code like MK1-F1-S001 */
    @Column(nullable = false, unique = true, length = 32)
    private String code;

    /** Optional human-readable shop name */
    @Column(name = "shop_name", length = 120)
    private String shopName;

    /** Market tag (e.g., Market-1) */
    @Column(nullable = false, length = 64)
    private String market;

    /** Floor number, use 0 for Ground if you like */
    private Integer floor;

    /** Free-text side/block (user-entered, not enum) */
    @Column(length = 32)
    private String side;

    /** Location description (e.g., 'North Aisle') */
    @Column(length = 128)
    private String location;

    /** Location/booth/row number like A-01 */
    @Column(name = "location_no", length = 32)
    private String locationNo;

    /** Registration/Govt/Committee number (unique if present) */
    @Column(name = "registration_no", length = 64, unique = true)
    private String registrationNo;

    /** Shop size in sqft */
    @Column(name = "area_sqft", precision = 10, scale = 2)
    private BigDecimal areaSqft;

    @Column(name = "owner_name", length = 120)
    private String ownerName;

    @Column(name = "owner_phone", length = 32)
    private String ownerPhone;

    @Column(columnDefinition = "text")
    private String remarks;

    @Column(nullable = false)
    private Boolean active = true;

    @Column(name = "created_at", nullable = false, updatable = false)
    private ZonedDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private ZonedDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        if (active == null) active = true;
        createdAt = ZonedDateTime.now();
        updatedAt = ZonedDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        if (active == null) active = true;
        updatedAt = ZonedDateTime.now();
    }
}
