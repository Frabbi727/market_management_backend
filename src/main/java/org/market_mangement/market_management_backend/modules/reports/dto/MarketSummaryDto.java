package org.market_mangement.market_management_backend.modules.reports.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MarketSummaryDto {
    private KpiCardDto kpis;
    private HealthPanelDto health;
    private InputsSnapshotDto inputs;
}