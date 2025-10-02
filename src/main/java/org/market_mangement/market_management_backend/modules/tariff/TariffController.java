package org.market_mangement.market_management_backend.modules.tariff;

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
public class TariffController {

    private final TariffService tariffService;

    @GetMapping
    public ResponseEntity<List<Tariff>> getAllTariffs() {
        return ResponseEntity.ok(tariffService.getAllTariffs());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Tariff> getTariffById(@PathVariable Long id) {
        return tariffService.getTariffById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/utility-type/{utilityType}")
    public ResponseEntity<List<Tariff>> getTariffsByUtilityType(@PathVariable UtilityType utilityType) {
        return ResponseEntity.ok(tariffService.getTariffsByUtilityType(utilityType));
    }

    @GetMapping("/utility-type/{utilityType}/ordered")
    public ResponseEntity<List<Tariff>> getTariffsByUtilityTypeOrderedByDate(@PathVariable UtilityType utilityType) {
        return ResponseEntity.ok(tariffService.getTariffsByUtilityTypeOrderedByDate(utilityType));
    }

    @GetMapping("/utility-type/{utilityType}/date/{effectiveFrom}")
    public ResponseEntity<Tariff> getTariffByUtilityTypeAndDate(
            @PathVariable UtilityType utilityType,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate effectiveFrom) {
        return tariffService.getTariffByUtilityTypeAndDate(utilityType, effectiveFrom)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Tariff> createTariff(@Valid @RequestBody Tariff tariff) {
        return ResponseEntity.status(HttpStatus.CREATED).body(tariffService.createTariff(tariff));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Tariff> updateTariff(@PathVariable Long id, @Valid @RequestBody Tariff tariff) {
        return ResponseEntity.ok(tariffService.updateTariff(id, tariff));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTariff(@PathVariable Long id) {
        tariffService.deleteTariff(id);
        return ResponseEntity.noContent().build();
    }
}