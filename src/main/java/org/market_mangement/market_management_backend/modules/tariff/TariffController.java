package org.market_mangement.market_management_backend.modules.tariff;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.market_mangement.market_management_backend.modules.meter.UtilityType;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/tariffs")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Tag(name = "Tariffs", description = "Maintain tariff schedules per utility")
public class TariffController {

    private final TariffService tariffService;

    @GetMapping
    @Operation(summary = "List tariffs", description = "Returns all tariff records")
    public ResponseEntity<List<Tariff>> getAllTariffs() {
        return ResponseEntity.ok(tariffService.getAllTariffs());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Find tariff", description = "Retrieves a tariff by its identifier")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Tariff found"),
            @ApiResponse(responseCode = "404", description = "Tariff not found")
    })
    public ResponseEntity<Tariff> getTariffById(@PathVariable Long id) {
        return tariffService.getTariffById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/utility-type/{utilityType}")
    @Operation(summary = "List tariffs by utility", description = "Filters tariffs using their utility type")
    public ResponseEntity<List<Tariff>> getTariffsByUtilityType(@PathVariable UtilityType utilityType) {
        return ResponseEntity.ok(tariffService.getTariffsByUtilityType(utilityType));
    }

    @GetMapping("/utility-type/{utilityType}/ordered")
    @Operation(summary = "List tariffs ordered", description = "Returns tariffs for a utility ordered by start date")
    public ResponseEntity<List<Tariff>> getTariffsByUtilityTypeOrderedByDate(@PathVariable UtilityType utilityType) {
        return ResponseEntity.ok(tariffService.getTariffsByUtilityTypeOrderedByDate(utilityType));
    }

    @GetMapping("/utility-type/{utilityType}/date/{effectiveFrom}")
    @Operation(summary = "Find tariff by utility and date", description = "Retrieves the tariff active for a utility on the given date")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Tariff found"),
            @ApiResponse(responseCode = "404", description = "Tariff not found")
    })
    public ResponseEntity<Tariff> getTariffByUtilityTypeAndDate(
            @PathVariable UtilityType utilityType,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate effectiveFrom) {
        return tariffService.getTariffByUtilityTypeAndDate(utilityType, effectiveFrom)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Create tariff", description = "Registers a new tariff schedule")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Tariff created"),
            @ApiResponse(responseCode = "400", description = "Validation error")
    })
    public ResponseEntity<Tariff> createTariff(@Valid @RequestBody Tariff tariff) {
        return ResponseEntity.status(HttpStatus.CREATED).body(tariffService.createTariff(tariff));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update tariff", description = "Updates an existing tariff record")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Tariff updated"),
            @ApiResponse(responseCode = "400", description = "Validation error"),
            @ApiResponse(responseCode = "404", description = "Tariff not found")
    })
    public ResponseEntity<Tariff> updateTariff(@PathVariable Long id, @Valid @RequestBody Tariff tariff) {
        return ResponseEntity.ok(tariffService.updateTariff(id, tariff));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete tariff", description = "Deletes a tariff schedule")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Tariff deleted"),
            @ApiResponse(responseCode = "404", description = "Tariff not found")
    })
    public ResponseEntity<Void> deleteTariff(@PathVariable Long id) {
        tariffService.deleteTariff(id);
        return ResponseEntity.noContent().build();
    }
}
