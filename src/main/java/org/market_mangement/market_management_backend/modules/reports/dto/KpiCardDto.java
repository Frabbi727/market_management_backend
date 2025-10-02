package org.market_mangement.market_management_backend.modules.reports.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class KpiCardDto {
    private Integer invoiceCount;
    private BigDecimal totalAmount;
    private BigDecimal electricityUnits;
    private BigDecimal electricityAmount;
    private BigDecimal acCost;
    private BigDecimal serviceCost;
}