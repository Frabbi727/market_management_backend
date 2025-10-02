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
public class InvoiceTableDto {
    private Long invoiceId;
    private String shopCode;
    private String shopName;
    private BigDecimal electricityAmount;
    private BigDecimal acAmount;
    private BigDecimal serviceAmount;
    private BigDecimal total;
    private String status;
    private Boolean locked;
    private Boolean hasOverride;
}