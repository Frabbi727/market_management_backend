package org.market_mangement.market_management_backend.modules.shop;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.market_mangement.market_management_backend.modules.market.Market;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

@Setter
@Getter
@Entity
@ToString
@Table(
        name = "shops",
        indexes = {
                @Index(name = "idx_shops_market_id", columnList = "market_id"),
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

    /**
     * Shop number (required, unique)
     */
    @Column(name = "shop_number", nullable = false, unique = true)
    private Long shopNumber;

    /**
     * Unique shop code like MK1-F1-S001 (auto-generated if not provided)
     */
    @Size(max = 32)
    @Pattern(regexp = "^[A-Z0-9-]+$", message = "Code must contain only uppercase letters, numbers, and hyphens")
    @Column(nullable = false, unique = true, length = 32)
    private String code;

    /**
     * Optional human-readable shop name
     */
    @NotBlank(message = "Name is required")
    @Column(name = "shop_name", length = 120)
    @Pattern(regexp = "^[a-zA-Z0-9\\s-]+$", message = "Invalid characters in name")

    private String shopName;

    /**
     * Market relationship - Foreign key to markets table
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "market_id", nullable = false)
    @NotNull(message = "Market is required")
    private Market market;

    /**
     * Floor number, use 0 for Ground if you like
     */
    private Integer floor;

    /**
     * Free-text side/block (user-entered, not enum)
     */
    @Size(max = 32)
    @Column(length = 32)
    private String side;

    /**
     * Location description (e.g., 'North Aisle')
     */
    @Size(max = 128)
    @Column(length = 128)
    private String location;

    /**
     * Location/booth/row number like A-01
     */
    @Size(max = 32)
    @Column(name = "location_no", length = 32)
    private String locationNo;

    /**
     * Registration/Govt/Committee number (unique if present)
     */
    @Size(max = 64)
    @Column(name = "registration_no", length = 64, unique = true)
    private String registrationNo;

    /**
     * Shop size in sqft
     */
    @Column(name = "area_sqft", precision = 10, scale = 2)
    private BigDecimal areaSqft;

    @Size(max = 120)
    @Column(name = "owner_name", length = 120)
    private String ownerName;

    @Size(max = 32)
    @Pattern(regexp = "^[0-9+\\-() ]*$", message = "Invalid phone number format")
    @Column(name = "owner_phone", length = 32)
    private String ownerPhone;

    @Size(max = 5000, message = "Remarks too long")
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
