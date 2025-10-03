package org.market_mangement.market_management_backend.modules.billing;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/billing")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Tag(name = "Billing", description = "Bulk operations for generating invoices and charges")
public class BillingController {

    private final BillingService billingService;

    /**
     * POST /api/v1/billing/compute?period=2025-01-15&force=false
     * Computes bills for all shops in the given period
     * Period format: YYYY-MM-DD
     */
    @PostMapping("/compute")
    @Operation(
            summary = "Compute monthly billing",
            description = "Triggers bill calculation for all shops in the supplied period",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Billing completed",
                            content = @Content(schema = @Schema(implementation = Map.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid period supplied", content = @Content)
            }
    )
    public ResponseEntity<Map<String, Object>> computeBills(
            @Parameter(description = "Billing period in YYYY-MM-DD format", required = true, in = ParameterIn.QUERY)
            @RequestParam String period,
            @Parameter(description = "Force recomputation even if bills already exist", in = ParameterIn.QUERY)
            @RequestParam(defaultValue = "false") Boolean force) {
        Map<String, Object> result = billingService.computeBills(period, force);
        return ResponseEntity.ok(result);
    }
}
