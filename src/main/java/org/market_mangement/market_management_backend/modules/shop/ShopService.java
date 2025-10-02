package org.market_mangement.market_management_backend.modules.shop;

import java.util.List;

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
        return shopRepository.save(shop);
    }

    @Transactional
    public Shop update(Long id, Shop updatedShop) {
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
}
