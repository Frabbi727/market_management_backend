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
public class ReadingStatusDto {
    private String shopCode;
    private String shopName;
    private String meterNumber;
    private BigDecimal prevReading;
    private BigDecimal currReading;
    private BigDecimal units;
    private Boolean missing;
}