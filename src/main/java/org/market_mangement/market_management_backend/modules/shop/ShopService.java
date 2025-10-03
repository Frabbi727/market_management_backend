package org.market_mangement.market_management_backend.modules.shop;

import java.util.List;

import org.market_mangement.market_management_backend.modules.market.Market;
import org.market_mangement.market_management_backend.modules.market.MarketRepository;
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
    private final MarketRepository marketRepository;

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

    public List<Shop> findByFloor(Integer floor) {
        return shopRepository.findByFloor(floor);
    }

    public List<Shop> findByMarketId(Long marketId) {
        return shopRepository.findByMarketId(marketId);
    }

    public List<Shop> findActiveShops() {
        return shopRepository.findByActive(true);
    }

    @Transactional
    public Shop create(ShopCreateDTO dto) {
        // Fetch market by ID
        Market market = marketRepository.findById(dto.getMarketId())
                .orElseThrow(() -> new EntityNotFoundException("Market not found with id=" + dto.getMarketId()));

        // Convert DTO to entity
        Shop shop = new Shop();
        shop.setShopNumber(dto.getShopNumber());
        shop.setCode(dto.getCode());
        shop.setShopName(dto.getShopName());
        shop.setMarket(market);
        shop.setFloor(dto.getFloor());
        shop.setSide(dto.getSide());
        shop.setLocation(dto.getLocation());
        shop.setLocationNo(dto.getLocationNo());
        shop.setRegistrationNo(dto.getRegistrationNo());
        shop.setAreaSqft(dto.getAreaSqft());
        shop.setOwnerName(dto.getOwnerName());
        shop.setOwnerPhone(dto.getOwnerPhone());
        shop.setRemarks(dto.getRemarks());
        shop.setActive(dto.getActive());

        sanitizeShopInputs(shop);

        // Auto-generate code if not provided
        if (shop.getCode() == null || shop.getCode().isBlank()) {
            shop.setCode(generateShopCodeWithAllFields(shop));
        }

        return shopRepository.save(shop);
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
    public Shop update(Long id, ShopUpdateDTO dto) {
        Shop existing = findById(id);

        // Fetch market by ID if changed
        Market market = marketRepository.findById(dto.getMarketId())
                .orElseThrow(() -> new EntityNotFoundException("Market not found with id=" + dto.getMarketId()));

        existing.setShopNumber(dto.getShopNumber());
        existing.setCode(dto.getCode());
        existing.setShopName(dto.getShopName());
        existing.setMarket(market);
        existing.setFloor(dto.getFloor());
        existing.setSide(dto.getSide());
        existing.setLocation(dto.getLocation());
        existing.setLocationNo(dto.getLocationNo());
        existing.setRegistrationNo(dto.getRegistrationNo());
        existing.setAreaSqft(dto.getAreaSqft());
        existing.setOwnerName(dto.getOwnerName());
        existing.setOwnerPhone(dto.getOwnerPhone());
        existing.setRemarks(dto.getRemarks());
        existing.setActive(dto.getActive());

        sanitizeShopInputs(existing);
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
