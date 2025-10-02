package org.market_mangement.market_management_backend.modules.periodinput;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/period-inputs")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class PeriodInputController {

    private final PeriodInputService periodInputService;

    @GetMapping
    public ResponseEntity<List<PeriodInput>> getAllPeriodInputs() {
        return ResponseEntity.ok(periodInputService.getAllPeriodInputs());
    }

    @GetMapping("/ordered")
    public ResponseEntity<List<PeriodInput>> getAllPeriodInputsOrderedByPeriod() {
        return ResponseEntity.ok(periodInputService.getAllPeriodInputsOrderedByPeriod());
    }

    @GetMapping("/{period}")
    public ResponseEntity<PeriodInput> getPeriodInputByPeriod(@PathVariable String period) {
        return periodInputService.getPeriodInputByPeriod(period)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<PeriodInput> createPeriodInput(@Valid @RequestBody PeriodInput periodInput) {
        return ResponseEntity.status(HttpStatus.CREATED).body(periodInputService.createPeriodInput(periodInput));
    }

    @PutMapping("/{period}")
    public ResponseEntity<PeriodInput> updatePeriodInput(@PathVariable String period, @Valid @RequestBody PeriodInput periodInput) {
        return ResponseEntity.ok(periodInputService.updatePeriodInput(period, periodInput));
    }

    @DeleteMapping("/{period}")
    public ResponseEntity<Void> deletePeriodInput(@PathVariable String period) {
        periodInputService.deletePeriodInput(period);
        return ResponseEntity.noContent().build();
    }
}