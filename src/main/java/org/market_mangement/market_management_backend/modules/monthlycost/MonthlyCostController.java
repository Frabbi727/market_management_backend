package org.market_mangement.market_management_backend.modules.monthlycost;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/monthly-costs")
@RequiredArgsConstructor
@Tag(name = "Monthly Cost", description = "Monthly cost configuration and distribution management")
public class MonthlyCostController {

    private final MonthlyCostService monthlyCostService;

    /**
     * Create new monthly cost configuration
     * POST /api/v1/monthly-costs
     */
    @PostMapping
    @Operation(summary = "Create monthly cost configuration", description = "Create a new monthly cost configuration for a market")
    public ResponseEntity<MonthlyCost> create(@Valid @RequestBody MonthlyCostCreateDTO dto) {
        MonthlyCost created = monthlyCostService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    /**
     * Get monthly cost configurations with filters
     * GET /api/v1/monthly-costs?marketId={id}&year={year}&month={month}
     */
    @GetMapping
    @Operation(summary = "Get monthly cost configurations", description = "Get all monthly cost configurations with optional filters")
    public ResponseEntity<List<MonthlyCost>> getAll(
            @RequestParam Long marketId,
            @RequestParam(required = false) Integer year,
            @RequestParam(required = false) Integer month) {
        List<MonthlyCost> monthlyCosts = monthlyCostService.getAllByMarket(marketId, year, month);
        return ResponseEntity.ok(monthlyCosts);
    }

    /**
     * Get single monthly cost configuration by ID
     * GET /api/v1/monthly-costs/{id}
     */
    @GetMapping("/{id}")
    @Operation(summary = "Get monthly cost by ID", description = "Get a specific monthly cost configuration by ID")
    public ResponseEntity<MonthlyCost> getById(@PathVariable Long id) {
        MonthlyCost monthlyCost = monthlyCostService.getById(id);
        return ResponseEntity.ok(monthlyCost);
    }

    /**
     * Update monthly cost configuration
     * PUT /api/v1/monthly-costs/{id}
     */
    @PutMapping("/{id}")
    @Operation(summary = "Update monthly cost", description = "Update an existing monthly cost configuration")
    public ResponseEntity<MonthlyCost> update(
            @PathVariable Long id,
            @Valid @RequestBody MonthlyCostUpdateDTO dto) {
        MonthlyCost updated = monthlyCostService.update(id, dto);
        return ResponseEntity.ok(updated);
    }

    /**
     * Delete monthly cost configuration
     * DELETE /api/v1/monthly-costs/{id}
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete monthly cost", description = "Delete a monthly cost configuration (only if not locked)")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        monthlyCostService.delete(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Lock monthly cost configuration
     * POST /api/v1/monthly-costs/{id}/lock
     */
    @PostMapping("/{id}/lock")
    @Operation(summary = "Lock monthly cost", description = "Lock monthly cost configuration to prevent changes")
    public ResponseEntity<Map<String, Object>> lock(@PathVariable Long id) {
        MonthlyCost locked = monthlyCostService.lock(id);

        Map<String, Object> response = new HashMap<>();
        response.put("id", locked.getId());
        response.put("locked", locked.getLocked());
        response.put("message", "Monthly cost configuration locked successfully");

        return ResponseEntity.ok(response);
    }

    /**
     * Unlock monthly cost configuration
     * POST /api/v1/monthly-costs/{id}/unlock
     */
    @PostMapping("/{id}/unlock")
    @Operation(summary = "Unlock monthly cost", description = "Unlock monthly cost configuration to allow changes")
    public ResponseEntity<Map<String, Object>> unlock(@PathVariable Long id) {
        MonthlyCost unlocked = monthlyCostService.unlock(id);

        Map<String, Object> response = new HashMap<>();
        response.put("id", unlocked.getId());
        response.put("locked", unlocked.getLocked());
        response.put("message", "Monthly cost configuration unlocked successfully");

        return ResponseEntity.ok(response);
    }
}