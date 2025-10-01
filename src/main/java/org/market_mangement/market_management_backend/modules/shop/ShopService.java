package org.market_mangement.market_management_backend.modules.shop;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityNotFoundException;

@Service
@Transactional(readOnly = true)
public class ShopService {

    private final ShopRepository shopRepository;

    public ShopService(ShopRepository shopRepository) {
        this.shopRepository = shopRepository;
    }

    public List<Shop> findAll() {
        return shopRepository.findAll();
    }

    public Shop findById(Long id) {
        return shopRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Shop not found with id=" + id));
    }

/*    @Transactional
    public Shop create(Shop shop) {
        return shopRepository.save(shop);
    }

    @Transactional
    public Shop update(Long id, Shop updatedShop) {
        Shop existing = findById(id);
        existing.setName(updatedShop.getName());
        existing.setAddress(updatedShop.getAddress());
        existing.setPhone(updatedShop.getPhone());
        return shopRepository.save(existing);
    }

    @Transactional
    public void delete(Long id) {
        Shop existing = findById(id);
        shopRepository.delete(existing);
    }*/
}
