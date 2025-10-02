package org.market_mangement.market_management_backend.modules.shop;

import java.util.List;

import org.owasp.encoder.Encode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ShopService {

    private final ShopRepository shopRepository;

    public List<Shop> findAll() {
        return shopRepository.findAll();
    }

    public Shop findById(Long id) {
        return shopRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Shop not found with id=" + id));
    }

    public Shop findByCode(String code) {
        return shopRepository.findByCode(code)
                .orElseThrow(() -> new EntityNotFoundException("Shop not found with code=" + code));
    }

    public List<Shop> findByMarket(String market) {
        return shopRepository.findByMarket(market);
    }

    public List<Shop> findByFloor(Integer floor) {
        return shopRepository.findByFloor(floor);
    }

    public List<Shop> findActiveShops() {
        return shopRepository.findByActive(true);
    }

    @Transactional
    public Shop create(Shop shop) {
        sanitizeShopInputs(shop);

        // Auto-generate code if not provided
        if (shop.getCode() == null || shop.getCode().isBlank()) {
            shop.setCode(generateShopCodeWithAllFields(shop));
        }

        return shopRepository.save(shop);
    }

    /**
     * Generates unique shop code based on: shopName-market-floor-side-location
     * Example: ELECTRONICS-MARKET1-F1-NORTH-CENTER
     */
    private String generateShopCode(String market, Integer floor) {
        StringBuilder code = new StringBuilder();

        // Add market (sanitized, uppercase, no spaces)
        if (market != null && !market.isBlank()) {
            code.append(sanitizeForCode(market));
        }

        // Add floor
        int floorNum = (floor != null) ? floor : 0;
        code.append("-F").append(floorNum);

        return code.toString().toUpperCase();
    }

    /**
     * Generates unique shop code with all fields
     */
    private String generateShopCodeWithAllFields(Shop shop) {
        StringBuilder code = new StringBuilder();

        // Add shop name
        if (shop.getShopName() != null && !shop.getShopName().isBlank()) {
            code.append(sanitizeForCode(shop.getShopName()));
        }

        // Add floor
        int floorNum = (shop.getFloor() != null) ? shop.getFloor() : 0;
        code.append("-F").append(floorNum);

        // Add side
        if (shop.getSide() != null && !shop.getSide().isBlank()) {
            code.append("-").append(sanitizeForCode(shop.getSide()));
        }
        if(shop.getShopNumber() != null){
            code.append("-").append(shop.getShopNumber());
        }

        return code.toString().toUpperCase();
    }

    /**
     * Sanitizes a string for use in code: removes spaces, special chars, keeps alphanumeric
     */
    private String sanitizeForCode(String input) {
        return input.replaceAll("[^a-zA-Z0-9]", "").trim();
    }

    @Transactional
    public Shop update(Long id, Shop updatedShop) {
        sanitizeShopInputs(updatedShop);
        Shop existing = findById(id);
        existing.setCode(updatedShop.getCode());
        existing.setShopName(updatedShop.getShopName());
        existing.setMarket(updatedShop.getMarket());
        existing.setFloor(updatedShop.getFloor());
        existing.setSide(updatedShop.getSide());
        existing.setLocation(updatedShop.getLocation());
        existing.setLocationNo(updatedShop.getLocationNo());
        existing.setRegistrationNo(updatedShop.getRegistrationNo());
        existing.setAreaSqft(updatedShop.getAreaSqft());
        existing.setOwnerName(updatedShop.getOwnerName());
        existing.setOwnerPhone(updatedShop.getOwnerPhone());
        existing.setRemarks(updatedShop.getRemarks());
        existing.setActive(updatedShop.getActive());
        return shopRepository.save(existing);
    }

    @Transactional
    public void delete(Long id) {
        Shop existing = findById(id);
        shopRepository.delete(existing);
    }

    /**
     * Sanitizes user inputs to prevent XSS attacks
     * Uses OWASP Encoder to encode HTML entities in text fields
     */
    private void sanitizeShopInputs(Shop shop) {
        if (shop.getCode() != null) {
            shop.setCode(Encode.forHtml(shop.getCode()));
        }
        if (shop.getShopName() != null) {
            shop.setShopName(Encode.forHtml(shop.getShopName()));
        }
        if (shop.getMarket() != null) {
            shop.setMarket(Encode.forHtml(shop.getMarket()));
        }
        if (shop.getSide() != null) {
            shop.setSide(Encode.forHtml(shop.getSide()));
        }
        if (shop.getLocation() != null) {
            shop.setLocation(Encode.forHtml(shop.getLocation()));
        }
        if (shop.getLocationNo() != null) {
            shop.setLocationNo(Encode.forHtml(shop.getLocationNo()));
        }
        if (shop.getRegistrationNo() != null) {
            shop.setRegistrationNo(Encode.forHtml(shop.getRegistrationNo()));
        }
        if (shop.getOwnerName() != null) {
            shop.setOwnerName(Encode.forHtml(shop.getOwnerName()));
        }
        if (shop.getOwnerPhone() != null) {
            shop.setOwnerPhone(Encode.forHtml(shop.getOwnerPhone()));
        }
        if (shop.getRemarks() != null) {
            shop.setRemarks(Encode.forHtml(shop.getRemarks()));
        }
    }
}
