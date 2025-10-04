package org.market_mangement.market_management_backend.modules.monthlycost;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class MonthlyCostUpdateDTO {

    @NotNull(message = "Total billing sqft is required")
    @DecimalMin(value = "0.01", message = "Total billing sqft must be greater than 0")
    private BigDecimal totalBillingSqft;

    private Boolean useAutoCalculation;

    // Electricity
    @NotNull(message = "Electricity rate is required")
    @DecimalMin(value = "0.00", message = "Electricity rate cannot be negative")
    private BigDecimal electricityRate;

    // AC
    private Boolean acEnabled;

    @DecimalMin(value = "0.00", message = "AC cost cannot be negative")
    private BigDecimal totalAcCost;

    // Service
    private Boolean serviceEnabled;

    @DecimalMin(value = "0.00", message = "Service cost cannot be negative")
    private BigDecimal totalServiceCost;

    // Generator
    private Boolean generatorEnabled;

    @DecimalMin(value = "0.00", message = "Generator cost cannot be negative")
    private BigDecimal totalGeneratorCost;

    // Special
    private Boolean specialEnabled;

    @DecimalMin(value = "0.00", message = "Special cost cannot be negative")
    private BigDecimal totalSpecialCost;

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