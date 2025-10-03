package org.market_mangement.market_management_backend.modules.reports;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.market_mangement.market_management_backend.modules.reports.dto.InvoicePageDto;
import org.market_mangement.market_management_backend.modules.reports.dto.MarketSummaryDto;
import org.market_mangement.market_management_backend.modules.reports.dto.ReadingStatusDto;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/reports")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Tag(name = "Reports", description = "Dashboards and aggregated reporting endpoints")
public class ReportsController {

    private final ReportsService reportsService;

    /**
     * GET /api/v1/reports/market-summary?marketId=1&period=2025-01-15
     * Returns KPIs, health status, and inputs snapshot for the dashboard
     * Period format: YYYY-MM-DD
     * marketId is optional - if not provided, returns data for all markets
     */
    @GetMapping("/market-summary")
    @Operation(
            summary = "Get market summary",
            description = "Returns KPI metrics, health status, and input snapshots for dashboards",
            responses = {@ApiResponse(responseCode = "200", description = "Summary retrieved")}
    )
    public ResponseEntity<MarketSummaryDto> getMarketSummary(
            @Parameter(description = "Optional market filter", in = ParameterIn.QUERY)
            @RequestParam(required = false) Long marketId,
            @Parameter(description = "Billing period in YYYY-MM-DD format", required = true, in = ParameterIn.QUERY)
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate period) {
        return ResponseEntity.ok(reportsService.getMarketSummary(marketId, period));
    }

    /**
     * GET /api/v1/reports/invoices?marketId=1&period=2025-01-15&page=0&size=20
     * Returns paginated invoices table for the dashboard
     * Period format: YYYY-MM-DD
     * marketId is optional - if not provided, returns data for all markets
     */
    @GetMapping("/invoices")
    @Operation(
            summary = "Get invoice table",
            description = "Returns paginated invoice data for dashboards",
            responses = {@ApiResponse(responseCode = "200", description = "Invoice table retrieved")}
    )
    public ResponseEntity<InvoicePageDto> getInvoicesTable(
            @Parameter(description = "Optional market filter", in = ParameterIn.QUERY)
            @RequestParam(required = false) Long marketId,
            @Parameter(description = "Billing period in YYYY-MM-DD format", required = true, in = ParameterIn.QUERY)
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate period,
            @Parameter(description = "Page number (0-indexed)", in = ParameterIn.QUERY)
            @RequestParam(defaultValue = "0") Integer page,
            @Parameter(description = "Page size", in = ParameterIn.QUERY)
            @RequestParam(defaultValue = "20") Integer size) {
        return ResponseEntity.ok(reportsService.getInvoicesTable(marketId, period, page, size));
    }

    /**
     * GET /api/v1/reports/reading-status?marketId=1&period=2025-01-15
     * Returns readings status table for specific date, showing missing readings
     * Period format: YYYY-MM-DD (filters readings by exact date)
     * marketId is optional - if not provided, returns data for all markets
     */
    @GetMapping("/reading-status")
    @Operation(
            summary = "Get reading status",
            description = "Returns reading completion state by market and period",
            responses = {@ApiResponse(responseCode = "200", description = "Reading status retrieved")}
    )
    public ResponseEntity<List<ReadingStatusDto>> getReadingStatus(
            @Parameter(description = "Optional market filter", in = ParameterIn.QUERY)
            @RequestParam(required = false) Long marketId,
            @Parameter(description = "Period in YYYY-MM-DD format", required = true, in = ParameterIn.QUERY)
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate period) {
        return ResponseEntity.ok(reportsService.getReadingStatus(marketId, period));
    }
}
