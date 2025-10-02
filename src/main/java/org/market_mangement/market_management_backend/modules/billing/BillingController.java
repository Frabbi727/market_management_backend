package org.market_mangement.market_management_backend.modules.billing;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/billing")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class BillingController {

    private final BillingService billingService;

    /**
     * POST /api/v1/billing/compute?period=2025-01-15&force=false
     * Computes bills for all shops in the given period
     * Period format: YYYY-MM-DD
     */
    @PostMapping("/compute")
    public ResponseEntity<Map<String, Object>> computeBills(
            @RequestParam String period,
            @RequestParam(defaultValue = "false") Boolean force) {
        Map<String, Object> result = billingService.computeBills(period, force);
        return ResponseEntity.ok(result);
    }
}