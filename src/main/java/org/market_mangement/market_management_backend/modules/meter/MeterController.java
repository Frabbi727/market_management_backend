package org.market_mangement.market_management_backend.modules.meter;

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
@RequestMapping("/api/meters")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Tag(name = "Meters", description = "Maintain utility meters and their lifecycle")
public class MeterController {

    private final MeterService meterService;

    @GetMapping
    @Operation(summary = "List meters", description = "Returns all meters with their attributes")
    public ResponseEntity<List<Meter>> getAllMeters() {
        return ResponseEntity.ok(meterService.getAllMeters());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Find meter", description = "Retrieves a meter by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Meter found"),
            @ApiResponse(responseCode = "404", description = "Meter not found")
    })
    public ResponseEntity<Meter> getMeterById(@PathVariable Long id) {
        return meterService.getMeterById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/shop/{shopId}")
    @Operation(summary = "List meters by shop", description = "Returns meters installed for a shop")
    public ResponseEntity<List<Meter>> getMetersByShopId(@PathVariable Long shopId) {
        return ResponseEntity.ok(meterService.getMetersByShopId(shopId));
    }

    @GetMapping("/utility-type/{utilityType}")
    @Operation(summary = "List meters by utility", description = "Filters meters for a utility type")
    public ResponseEntity<List<Meter>> getMetersByUtilityType(@PathVariable UtilityType utilityType) {
        return ResponseEntity.ok(meterService.getMetersByUtilityType(utilityType));
    }

    @GetMapping("/active")
    @Operation(summary = "List active meters", description = "Returns only meters flagged as active")
    public ResponseEntity<List<Meter>> getActiveMeters() {
        return ResponseEntity.ok(meterService.getActiveMeters());
    }

    @PostMapping
    @Operation(summary = "Create meter", description = "Registers a new meter")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Meter created"),
            @ApiResponse(responseCode = "400", description = "Validation error")
    })
    public ResponseEntity<Meter> createMeter(@Valid @RequestBody Meter meter) {
        return ResponseEntity.status(HttpStatus.CREATED).body(meterService.createMeter(meter));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update meter", description = "Updates meter details by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Meter updated"),
            @ApiResponse(responseCode = "400", description = "Validation error"),
            @ApiResponse(responseCode = "404", description = "Meter not found")
    })
    public ResponseEntity<Meter> updateMeter(@PathVariable Long id, @Valid @RequestBody Meter meter) {
        return ResponseEntity.ok(meterService.updateMeter(id, meter));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete meter", description = "Removes a meter record")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Meter deleted"),
            @ApiResponse(responseCode = "404", description = "Meter not found")
    })
    public ResponseEntity<Void> deleteMeter(@PathVariable Long id) {
        meterService.deleteMeter(id);
        return ResponseEntity.noContent().build();
    }
}
