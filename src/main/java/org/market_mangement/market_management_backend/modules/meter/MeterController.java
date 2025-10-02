package org.market_mangement.market_management_backend.modules.meter;

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
public class MeterController {

    private final MeterService meterService;

    @GetMapping
    public ResponseEntity<List<Meter>> getAllMeters() {
        return ResponseEntity.ok(meterService.getAllMeters());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Meter> getMeterById(@PathVariable Long id) {
        return meterService.getMeterById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/shop/{shopId}")
    public ResponseEntity<List<Meter>> getMetersByShopId(@PathVariable Long shopId) {
        return ResponseEntity.ok(meterService.getMetersByShopId(shopId));
    }

    @GetMapping("/utility-type/{utilityType}")
    public ResponseEntity<List<Meter>> getMetersByUtilityType(@PathVariable UtilityType utilityType) {
        return ResponseEntity.ok(meterService.getMetersByUtilityType(utilityType));
    }

    @GetMapping("/active")
    public ResponseEntity<List<Meter>> getActiveMeters() {
        return ResponseEntity.ok(meterService.getActiveMeters());
    }

    @PostMapping
    public ResponseEntity<Meter> createMeter(@Valid @RequestBody Meter meter) {
        return ResponseEntity.status(HttpStatus.CREATED).body(meterService.createMeter(meter));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Meter> updateMeter(@PathVariable Long id, @Valid @RequestBody Meter meter) {
        return ResponseEntity.ok(meterService.updateMeter(id, meter));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMeter(@PathVariable Long id) {
        meterService.deleteMeter(id);
        return ResponseEntity.noContent().build();
    }
}