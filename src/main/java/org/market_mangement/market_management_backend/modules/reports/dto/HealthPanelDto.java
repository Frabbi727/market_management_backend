package org.market_mangement.market_management_backend.modules.reports.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HealthPanelDto {
    private Boolean inputsOk;
    private Integer missingReadingsCount;
    private Boolean tariffOk;
    private Integer unlockedInvoicesCount;
}