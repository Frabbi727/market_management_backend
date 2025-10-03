package org.market_mangement.market_management_backend.modules.market;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MarketRepository extends JpaRepository<Market, Long> {

    Optional<Market> findByName(String name);

    List<Market> findByActive(Boolean active);
}