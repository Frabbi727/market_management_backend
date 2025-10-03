package org.market_mangement.market_management_backend.modules.market;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.owasp.encoder.Encode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MarketService {

    private final MarketRepository marketRepository;

    public List<Market> findAll() {
        return marketRepository.findAll();
    }

    public Market findById(Long id) {
        return marketRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Market not found with id=" + id));
    }

    public Market findByName(String name) {
        return marketRepository.findByName(name)
                .orElseThrow(() -> new EntityNotFoundException("Market not found with name=" + name));
    }

    public List<Market> findActiveMarkets() {
        return marketRepository.findByActive(true);
    }

    @Transactional
    public Market create(Market market) {
        sanitizeMarketInputs(market);
        return marketRepository.save(market);
    }

    @Transactional
    public Market update(Long id, Market updatedMarket) {
        sanitizeMarketInputs(updatedMarket);
        Market existing = findById(id);

        existing.setName(updatedMarket.getName());
        existing.setAddress(updatedMarket.getAddress());
        existing.setPhone(updatedMarket.getPhone());
        existing.setEmail(updatedMarket.getEmail());
        existing.setTaxId(updatedMarket.getTaxId());
        existing.setFloorCount(updatedMarket.getFloorCount());
        existing.setTotalShopsCapacity(updatedMarket.getTotalShopsCapacity());
        existing.setManagerName(updatedMarket.getManagerName());
        existing.setManagerPhone(updatedMarket.getManagerPhone());
        existing.setRemarks(updatedMarket.getRemarks());
        existing.setActive(updatedMarket.getActive());

        return marketRepository.save(existing);
    }

    @Transactional
    public void delete(Long id) {
        Market existing = findById(id);
        marketRepository.delete(existing);
    }

    /**
     * Sanitizes user inputs to prevent XSS attacks
     */
    private void sanitizeMarketInputs(Market market) {
        if (market.getName() != null) {
            market.setName(Encode.forHtml(market.getName()));
        }
        if (market.getAddress() != null) {
            market.setAddress(Encode.forHtml(market.getAddress()));
        }
        if (market.getPhone() != null) {
            market.setPhone(Encode.forHtml(market.getPhone()));
        }
        if (market.getEmail() != null) {
            market.setEmail(Encode.forHtml(market.getEmail()));
        }
        if (market.getTaxId() != null) {
            market.setTaxId(Encode.forHtml(market.getTaxId()));
        }
        if (market.getManagerName() != null) {
            market.setManagerName(Encode.forHtml(market.getManagerName()));
        }
        if (market.getManagerPhone() != null) {
            market.setManagerPhone(Encode.forHtml(market.getManagerPhone()));
        }
        if (market.getRemarks() != null) {
            market.setRemarks(Encode.forHtml(market.getRemarks()));
        }
    }
}