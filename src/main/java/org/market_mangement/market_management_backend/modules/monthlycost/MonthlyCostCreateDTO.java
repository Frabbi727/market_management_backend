package org.market_mangement.market_management_backend.modules.monthlycost;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class MonthlyCostCreateDTO {

    @NotNull(message = "Market ID is required")
    private Long marketId;

    @NotNull(message = "Year is required")
    @Min(value = 2000, message = "Year must be at least 2000")
    @Max(value = 2100, message = "Year must be at most 2100")
    private Integer year;

    @NotNull(message = "Month is required")
    @Min(value = 1, message = "Month must be between 1 and 12")
    @Max(value = 12, message = "Month must be between 1 and 12")
    private Integer month;

    @NotNull(message = "Total billing sqft is required")
    @DecimalMin(value = "0.01", message = "Total billing sqft must be greater than 0")
    private BigDecimal totalBillingSqft;

    private Boolean useAutoCalculation = true;

    // Electricity
    @NotNull(message = "Electricity rate is required")
    @DecimalMin(value = "0.00", message = "Electricity rate cannot be negative")
    private BigDecimal electricityRate;

    // AC
    private Boolean acEnabled = true;

    @DecimalMin(value = "0.00", message = "AC cost cannot be negative")
    private BigDecimal totalAcCost = BigDecimal.ZERO;

    // Service
    private Boolean serviceEnabled = true;

    @DecimalMin(value = "0.00", message = "Service cost cannot be negative")
    private BigDecimal totalServiceCost = BigDecimal.ZERO;

    // Generator
    private Boolean generatorEnabled = true;

    @DecimalMin(value = "0.00", message = "Generator cost cannot be negative")
    private BigDecimal totalGeneratorCost = BigDecimal.ZERO;

    // Special
    private Boolean specialEnabled = false;

    @DecimalMin(value = "0.00", message = "Special cost cannot be negative")
    private BigDecimal totalSpecialCost = BigDecimal.ZERO;

    @Size(max = 200)
    private String specialCostName;

    @Size(max = 1000)
    private String specialCostRemarks;

    // Dates
    @NotNull(message = "Issue date is required")
    private LocalDate issueDate;

    @NotNull(message = "Due date is required")
    private LocalDate dueDate;
}