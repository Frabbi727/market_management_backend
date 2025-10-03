package org.market_mangement.market_management_backend.modules.shop;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Schema(description = "Request payload for updating an existing shop")
public class ShopUpdateDTO {

    @Schema(description = "Unique shop number", example = "1")
    private Long shopNumber;

    @Size(max = 32)
    @Pattern(regexp = "^[A-Z0-9-]+$", message = "Code must contain only uppercase letters, numbers, and hyphens")
    @Schema(description = "Unique shop code", example = "SHOP-F1-NORTH-1")
    private String code;

    @NotBlank(message = "Name is required")
    @Size(max = 120)
    @Pattern(regexp = "^[a-zA-Z0-9\\s-]+$", message = "Invalid characters in name")
    @Schema(description = "Shop name", example = "Shop A Updated", required = true)
    private String shopName;

    @NotNull(message = "Market ID is required")
    @Schema(description = "ID of the market this shop belongs to", example = "1", required = true)
    private Long marketId;

    @Schema(description = "Floor number", example = "2")
    private Integer floor;

    @Size(max = 32)
    @Schema(description = "Side or block identifier", example = "South")
    private String side;

    @Size(max = 128)
    @Schema(description = "Location description", example = "East corridor")
    private String location;

    @Size(max = 32)
    @Schema(description = "Location/booth number", example = "B-02")
    private String locationNo;

    @Size(max = 64)
    @Schema(description = "Registration number", example = "REG456")
    private String registrationNo;

    @Schema(description = "Shop area in square feet", example = "150.75")
    private BigDecimal areaSqft;

    @Size(max = 120)
    @Schema(description = "Owner name", example = "Jane Smith")
    private String ownerName;

    @Size(max = 32)
    @Pattern(regexp = "^[0-9+\\-() ]*$", message = "Invalid phone number format")
    @Schema(description = "Owner phone number", example = "987-654-3210")
    private String ownerPhone;

    @Size(max = 5000, message = "Remarks too long")
    @Schema(description = "Additional remarks or notes")
    private String remarks;

    @Schema(description = "Whether the shop is active", example = "true")
    private Boolean active;
}