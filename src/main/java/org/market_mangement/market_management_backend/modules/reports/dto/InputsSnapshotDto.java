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
public class InputsSnapshotDto {
    private BigDecimal acTotalUnits;
    private BigDecimal acUnitPrice;
    private BigDecimal acPerSqftRate;
    private BigDecimal guardCost;
    private BigDecimal maidCost;
    private BigDecimal otherCost;
    private BigDecimal servicePerSqftRate;
    private BigDecimal marketTotalSqft;
}