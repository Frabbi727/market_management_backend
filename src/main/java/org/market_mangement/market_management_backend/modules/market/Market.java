package org.market_mangement.market_management_backend.modules.market;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.ZonedDateTime;

@Setter
@Getter
@Entity
@ToString
@Table(name = "markets")
public class Market {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Market name (e.g., "Central Market", "City Plaza")
     */
    @NotBlank(message = "Market name is required")
    @Size(max = 120)
    @Column(name = "name", nullable = false, length = 120)
    private String name;

    /**
     * Market full address
     */
    @NotBlank(message = "Address is required")
    @Size(max = 500)
    @Column(name = "address", nullable = false, length = 500)
    private String address;

    /**
     * Market contact phone number
     */
    @Size(max = 32)
    @Pattern(regexp = "^[0-9+\\-() ]*$", message = "Invalid phone number format")
    @Column(name = "phone", length = 32)
    private String phone;

    /**
     * Market email address
     */
    @Size(max = 100)
    @Pattern(regexp = "^[A-Za-z0-9+_.-]+@(.+)$", message = "Invalid email format")
    @Column(name = "email", length = 100)
    private String email;

    /**
     * Tax ID or registration number
     */
    @Size(max = 64)
    @Column(name = "tax_id", length = 64)
    private String taxId;

    /**
     * Number of floors in the market
     */
    @Column(name = "floor_count")
    private Integer floorCount;

    /**
     * Total number of shops capacity
     */
    @Column(name = "total_shops_capacity")
    private Integer totalShopsCapacity;

    /**
     * Manager or admin name
     */
    @Size(max = 120)
    @Column(name = "manager_name", length = 120)
    private String managerName;

    /**
     * Manager contact
     */
    @Size(max = 32)
    @Pattern(regexp = "^[0-9+\\-() ]*$", message = "Invalid phone number format")
    @Column(name = "manager_phone", length = 32)
    private String managerPhone;

    /**
     * Additional notes
     */
    @Size(max = 5000)
    @Column(name = "remarks", columnDefinition = "text")
    private String remarks;

    /**
     * Is this market active
     */
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