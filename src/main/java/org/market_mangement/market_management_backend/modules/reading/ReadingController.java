package org.market_mangement.market_management_backend.modules.reading;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/readings")
@RequiredArgsConstructor
@Tag(name = "Readings", description = "Capture and update utility readings")
public class ReadingController {

    private final ReadingService readingService;

    @GetMapping
    @Operation(summary = "List readings", description = "Returns all meter readings")
    public ResponseEntity<List<Reading>> getAllReadings() {
        return ResponseEntity.ok(readingService.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Find reading", description = "Retrieves a reading by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Reading found"),
            @ApiResponse(responseCode = "404", description = "Reading not found")
    })
    public ResponseEntity<Reading> getReadingById(@PathVariable Long id) {
        return ResponseEntity.ok(readingService.findById(id));
    }

    @GetMapping("/meter/{meterId}")
    @Operation(summary = "List readings by meter", description = "Returns readings for a meter across periods")
    public ResponseEntity<List<Reading>> getReadingsByMeterId(@PathVariable Long meterId) {
        return ResponseEntity.ok(readingService.findByMeterId(meterId));
    }

    @GetMapping("/period/{period}")
    @Operation(summary = "List readings by period", description = "Returns readings captured for a specific period")
    public ResponseEntity<List<Reading>> getReadingsByPeriod(@PathVariable String period) {
        return ResponseEntity.ok(readingService.findByPeriod(period));
    }

    @GetMapping("/meter/{meterId}/period/{period}")
    @Operation(summary = "Find reading by meter and period", description = "Loads the single reading for a meter/period combination")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Reading found"),
            @ApiResponse(responseCode = "404", description = "Reading not found")
    })
    public ResponseEntity<Reading> getReadingByMeterAndPeriod(
            @PathVariable Long meterId,
            @PathVariable String period) {
        return ResponseEntity.ok(readingService.findByMeterIdAndPeriod(meterId, period));
    }

    @PostMapping
    @Operation(summary = "Create reading", description = "Captures a new meter reading")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Reading created"),
            @ApiResponse(responseCode = "400", description = "Validation error")
    })
    public ResponseEntity<Reading> createReading(@RequestBody Reading reading) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(readingService.create(reading));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update reading", description = "Updates an existing meter reading")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Reading updated"),
            @ApiResponse(responseCode = "400", description = "Validation error"),
            @ApiResponse(responseCode = "404", description = "Reading not found")
    })
    public ResponseEntity<Reading> updateReading(
            @PathVariable Long id,
            @RequestBody Reading reading) {
        return ResponseEntity.ok(readingService.update(id, reading));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete reading", description = "Deletes a reading permanently")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Reading deleted"),
            @ApiResponse(responseCode = "404", description = "Reading not found")
    })
    public ResponseEntity<Void> deleteReading(@PathVariable Long id) {
        readingService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
