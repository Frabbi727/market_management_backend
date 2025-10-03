package org.market_mangement.market_management_backend.modules.shop;


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
@RequestMapping("/api/v1/shops")
@RequiredArgsConstructor
@Tag(name = "Shops", description = "Manage shop inventory, status, and metadata")
public class ShopController {

    private final ShopService shopService;

    @GetMapping
    @Operation(summary = "List shops", description = "Returns every shop in the system")
    public ResponseEntity<List<Shop>> getAllShops() {
        return ResponseEntity.ok(shopService.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Find shop", description = "Loads a shop by its primary identifier")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Shop found"),
            @ApiResponse(responseCode = "404", description = "Shop not found")
    })
    public ResponseEntity<Shop> getShopById(@PathVariable Long id) {
        return ResponseEntity.ok(shopService.findById(id));
    }

    @GetMapping("/code/{code}")
    @Operation(summary = "Find shop by code", description = "Searches for a shop using its unique code")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Shop found"),
            @ApiResponse(responseCode = "404", description = "Shop not found")
    })
    public ResponseEntity<Shop> getShopByCode(@PathVariable String code) {
        return ResponseEntity.ok(shopService.findByCode(code));
    }

    @GetMapping("/floor/{floor}")
    @Operation(summary = "List shops by floor", description = "Retrieves all shops assigned to a specific floor")
    public ResponseEntity<List<Shop>> getShopsByFloor(@PathVariable Integer floor) {
        return ResponseEntity.ok(shopService.findByFloor(floor));
    }

    @GetMapping("/market/{marketId}")
    @Operation(summary = "List shops by market", description = "Returns shops belonging to a single market")
    public ResponseEntity<List<Shop>> getShopsByMarketId(@PathVariable Long marketId) {
        return ResponseEntity.ok(shopService.findByMarketId(marketId));
    }

    @GetMapping("/active")
    @Operation(summary = "List active shops", description = "Returns shops currently marked as active")
    public ResponseEntity<List<Shop>> getActiveShops() {
        return ResponseEntity.ok(shopService.findActiveShops());
    }

    @PostMapping
    @Operation(summary = "Create shop", description = "Registers a new shop record and assigns identifiers")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Shop created"),
            @ApiResponse(responseCode = "400", description = "Validation error")
    })
    public ResponseEntity<Shop> createShop(@Valid @RequestBody ShopCreateDTO shopDTO) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(shopService.create(shopDTO));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update shop", description = "Replaces shop attributes for the supplied ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Shop updated"),
            @ApiResponse(responseCode = "400", description = "Validation error"),
            @ApiResponse(responseCode = "404", description = "Shop not found")
    })
    @SuppressWarnings("java:S5131") // False positive: Input is validated and sanitized in service layer
    public ResponseEntity<Shop> updateShop(
            @PathVariable Long id,
            @Valid @RequestBody ShopUpdateDTO shopDTO) {
        return ResponseEntity.ok(shopService.update(id, shopDTO));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete shop", description = "Removes the shop and dependent records where cascading applies")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Shop deleted"),
            @ApiResponse(responseCode = "404", description = "Shop not found")
    })
    public ResponseEntity<Void> deleteShop(@PathVariable Long id) {
        shopService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
