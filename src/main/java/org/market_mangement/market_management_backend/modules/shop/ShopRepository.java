package org.market_mangement.market_management_backend.modules.shop;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ShopRepository extends JpaRepository<Shop, Long> {

    Optional<Shop> findByCode(String code);

    List<Shop> findByMarket(String market);

    List<Shop> findByFloor(Integer floor);

    List<Shop> findByActive(Boolean active);

    List<Shop> findByOwnerName(String ownerName);
}
