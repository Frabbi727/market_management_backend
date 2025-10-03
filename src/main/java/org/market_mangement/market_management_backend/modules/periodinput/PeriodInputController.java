package org.market_mangement.market_management_backend.modules.periodinput;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/periods")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Tag(name = "Periods", description = "Manage billing periods and inputs")
public class PeriodInputController {

    private final PeriodInputService periodInputService;

    @GetMapping
    @Operation(summary = "List periods", description = "Returns all stored period inputs")
    public ResponseEntity<List<PeriodInput>> getAllPeriodInputs() {
        return ResponseEntity.ok(periodInputService.getAllPeriodInputs());
    }

    @GetMapping("/ordered")
    @Operation(summary = "List periods ordered", description = "Returns all periods sorted by their value")
    public ResponseEntity<List<PeriodInput>> getAllPeriodInputsOrderedByPeriod() {
        return ResponseEntity.ok(periodInputService.getAllPeriodInputsOrderedByPeriod());
    }

    @GetMapping("/{period}")
    @Operation(summary = "Find period", description = "Retrieves a single period by value")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Period found"),
            @ApiResponse(responseCode = "404", description = "Period not found")
    })
    public ResponseEntity<PeriodInput> getPeriodInputByPeriod(@PathVariable String period) {
        return periodInputService.getPeriodInputByPeriod(period)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Create period", description = "Adds a new period input")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Period created"),
            @ApiResponse(responseCode = "400", description = "Validation error")
    })
    public ResponseEntity<PeriodInput> createPeriodInput(@Valid @RequestBody PeriodInput periodInput) {
        return ResponseEntity.status(HttpStatus.CREATED).body(periodInputService.createPeriodInput(periodInput));
    }

    @PutMapping("/{period}/inputs")
    @Operation(summary = "Update period", description = "Updates a period by its key")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Period updated"),
            @ApiResponse(responseCode = "400", description = "Validation error"),
            @ApiResponse(responseCode = "404", description = "Period not found")
    })
    public ResponseEntity<PeriodInput> updatePeriodInput(@PathVariable String period, @Valid @RequestBody PeriodInput periodInput) {
        return ResponseEntity.ok(periodInputService.updatePeriodInput(period, periodInput));
    }

    @DeleteMapping("/{period}")
    @Operation(summary = "Delete period", description = "Removes a period and its inputs")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Period deleted"),
            @ApiResponse(responseCode = "404", description = "Period not found")
    })
    public ResponseEntity<Void> deletePeriodInput(@PathVariable String period) {
        periodInputService.deletePeriodInput(period);
        return ResponseEntity.noContent().build();
    }
}
