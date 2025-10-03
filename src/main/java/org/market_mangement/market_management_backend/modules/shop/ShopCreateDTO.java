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
@Schema(description = "Request payload for creating a new shop")
public class ShopCreateDTO {

    @Schema(description = "Unique shop number", example = "1")
    private Long shopNumber;

    @Size(max = 32)
    @Pattern(regexp = "^[A-Z0-9-]+$", message = "Code must contain only uppercase letters, numbers, and hyphens")
    @Schema(description = "Unique shop code (auto-generated if not provided)", example = "SHOP-F1-NORTH-1")
    private String code;

    @NotBlank(message = "Name is required")
    @Size(max = 120)
    @Pattern(regexp = "^[a-zA-Z0-9\\s-]+$", message = "Invalid characters in name")
    @Schema(description = "Shop name", example = "Shop A", required = true)
    private String shopName;

    @NotNull(message = "Market ID is required")
    @Schema(description = "ID of the market this shop belongs to", example = "1", required = true)
    private Long marketId;

    @Schema(description = "Floor number", example = "1")
    private Integer floor;

    @Size(max = 32)
    @Schema(description = "Side or block identifier", example = "North")
    private String side;

    @Size(max = 128)
    @Schema(description = "Location description", example = "Main corridor")
    private String location;

    @Size(max = 32)
    @Schema(description = "Location/booth number", example = "A-01")
    private String locationNo;

    @Size(max = 64)
    @Schema(description = "Registration number", example = "REG123")
    private String registrationNo;

    @Schema(description = "Shop area in square feet", example = "100.50")
    private BigDecimal areaSqft;

    @Size(max = 120)
    @Schema(description = "Owner name", example = "John Doe")
    private String ownerName;

    @Size(max = 32)
    @Pattern(regexp = "^[0-9+\\-() ]*$", message = "Invalid phone number format")
    @Schema(description = "Owner phone number", example = "123-456-7890")
    private String ownerPhone;

    @Size(max = 5000, message = "Remarks too long")
    @Schema(description = "Additional remarks or notes")
    private String remarks;

    @Schema(description = "Whether the shop is active", example = "true", defaultValue = "true")
    private Boolean active = true;
}