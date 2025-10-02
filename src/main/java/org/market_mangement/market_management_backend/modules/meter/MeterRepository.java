package org.market_mangement.market_management_backend.modules.meter;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MeterRepository extends JpaRepository<Meter, Long> {
    List<Meter> findByShopId(Long shopId);
    List<Meter> findByUtilityType(UtilityType utilityType);
    List<Meter> findByActive(Boolean active);
}