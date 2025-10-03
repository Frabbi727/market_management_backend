package org.market_mangement.market_management_backend.modules.market;

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
@RequestMapping("/api/v1/markets")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Tag(name = "Markets", description = "Manage market profiles and availability")
public class MarketController {

    private final MarketService marketService;

    @GetMapping
    @Operation(summary = "List markets", description = "Returns all markets regardless of status")
    public ResponseEntity<List<Market>> getAllMarkets() {
        return ResponseEntity.ok(marketService.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Find market", description = "Loads a single market by its identifier")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Market found"),
            @ApiResponse(responseCode = "404", description = "Market not found")
    })
    public ResponseEntity<Market> getMarketById(@PathVariable Long id) {
        return ResponseEntity.ok(marketService.findById(id));
    }

    @GetMapping("/name/{name}")
    @Operation(summary = "Find market by name", description = "Searches for a market using its unique name")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Market found"),
            @ApiResponse(responseCode = "404", description = "Market not found")
    })
    public ResponseEntity<Market> getMarketByName(@PathVariable String name) {
        return ResponseEntity.ok(marketService.findByName(name));
    }

    @GetMapping("/active")
    @Operation(summary = "List active markets", description = "Returns only markets flagged as active")
    public ResponseEntity<List<Market>> getActiveMarkets() {
        return ResponseEntity.ok(marketService.findActiveMarkets());
    }

    @PostMapping
    @Operation(summary = "Create market", description = "Registers a new market profile")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Market created"),
            @ApiResponse(responseCode = "400", description = "Validation error")
    })
    public ResponseEntity<Market> createMarket(@Valid @RequestBody Market market) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(marketService.create(market));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update market", description = "Replaces market details for the supplied identifier")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Market updated"),
            @ApiResponse(responseCode = "400", description = "Validation error"),
            @ApiResponse(responseCode = "404", description = "Market not found")
    })
    @SuppressWarnings("java:S5131")
    public ResponseEntity<Market> updateMarket(
            @PathVariable Long id,
            @Valid @RequestBody Market market) {
        return ResponseEntity.ok(marketService.update(id, market));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete market", description = "Removes the market and all related associations")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Market deleted"),
            @ApiResponse(responseCode = "404", description = "Market not found")
    })
    public ResponseEntity<Void> deleteMarket(@PathVariable Long id) {
        marketService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
