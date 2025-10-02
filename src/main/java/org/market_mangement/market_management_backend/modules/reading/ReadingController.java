package org.market_mangement.market_management_backend.modules.reading;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/readings")
@RequiredArgsConstructor
public class ReadingController {

    private final ReadingService readingService;

    @GetMapping
    public ResponseEntity<List<Reading>> getAllReadings() {
        return ResponseEntity.ok(readingService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Reading> getReadingById(@PathVariable Long id) {
        return ResponseEntity.ok(readingService.findById(id));
    }

    @GetMapping("/meter/{meterId}")
    public ResponseEntity<List<Reading>> getReadingsByMeterId(@PathVariable Long meterId) {
        return ResponseEntity.ok(readingService.findByMeterId(meterId));
    }

    @GetMapping("/period/{period}")
    public ResponseEntity<List<Reading>> getReadingsByPeriod(@PathVariable String period) {
        return ResponseEntity.ok(readingService.findByPeriod(period));
    }

    @GetMapping("/meter/{meterId}/period/{period}")
    public ResponseEntity<Reading> getReadingByMeterAndPeriod(
            @PathVariable Long meterId,
            @PathVariable String period) {
        return ResponseEntity.ok(readingService.findByMeterIdAndPeriod(meterId, period));
    }

    @PostMapping
    public ResponseEntity<Reading> createReading(@RequestBody Reading reading) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(readingService.create(reading));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Reading> updateReading(
            @PathVariable Long id,
            @RequestBody Reading reading) {
        return ResponseEntity.ok(readingService.update(id, reading));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReading(@PathVariable Long id) {
        readingService.delete(id);
        return ResponseEntity.noContent().build();
    }
}