package org.market_mangement.market_management_backend.modules.reports;

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
public class ReportsController {

    private final ReportsService reportsService;

    /**
     * GET /api/v1/reports/market-summary?market=Market-1&period=2025-01-15
     * Returns KPIs, health status, and inputs snapshot for the dashboard
     * Period format: YYYY-MM-DD
     */
    @GetMapping("/market-summary")
    public ResponseEntity<MarketSummaryDto> getMarketSummary(
            @RequestParam String market,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate period) {
        return ResponseEntity.ok(reportsService.getMarketSummary(market, period));
    }

    /**
     * GET /api/v1/reports/invoices?market=Market-1&period=2025-01-15&page=0&size=20
     * Returns paginated invoices table for the dashboard
     * Period format: YYYY-MM-DD
     */
    @GetMapping("/invoices")
    public ResponseEntity<InvoicePageDto> getInvoicesTable(
            @RequestParam String market,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate period,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "20") Integer size) {
        return ResponseEntity.ok(reportsService.getInvoicesTable(market, period, page, size));
    }

    /**
     * GET /api/v1/reports/reading-status?market=Market-1&period=2025-01-15
     * Returns readings status table for specific date, showing missing readings
     * Period format: YYYY-MM-DD (filters readings by exact date)
     */
    @GetMapping("/reading-status")
    public ResponseEntity<List<ReadingStatusDto>> getReadingStatus(
            @RequestParam String market,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate period) {
        return ResponseEntity.ok(reportsService.getReadingStatus(market, period));
    }
}