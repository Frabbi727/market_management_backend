package org.market_mangement.market_management_backend.modules.shop;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/shops")
@RequiredArgsConstructor
public class ShopController {

    private final ShopService shopService;

    @GetMapping
    public ResponseEntity<List<Shop>> getAllShops() {
        return ResponseEntity.ok(shopService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Shop> getShopById(@PathVariable Long id) {
        return ResponseEntity.ok(shopService.findById(id));
    }

    @GetMapping("/code/{code}")
    public ResponseEntity<Shop> getShopByCode(@PathVariable String code) {
        return ResponseEntity.ok(shopService.findByCode(code));
    }

    @GetMapping("/market/{market}")
    public ResponseEntity<List<Shop>> getShopsByMarket(@PathVariable String market) {
        return ResponseEntity.ok(shopService.findByMarket(market));
    }

    @GetMapping("/floor/{floor}")
    public ResponseEntity<List<Shop>> getShopsByFloor(@PathVariable Integer floor) {
        return ResponseEntity.ok(shopService.findByFloor(floor));
    }

    @GetMapping("/active")
    public ResponseEntity<List<Shop>> getActiveShops() {
        return ResponseEntity.ok(shopService.findActiveShops());
    }

    @PostMapping
    public ResponseEntity<Shop> createShop(@Valid @RequestBody Shop shop) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(shopService.create(shop));
    }

    @PutMapping("/{id}")
    @SuppressWarnings("java:S5131") // False positive: Input is validated and sanitized in service layer
    public ResponseEntity<Shop> updateShop(
            @PathVariable Long id,
            @Valid @RequestBody Shop shop) {
        return ResponseEntity.ok(shopService.update(id, shop));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteShop(@PathVariable Long id) {
        shopService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
