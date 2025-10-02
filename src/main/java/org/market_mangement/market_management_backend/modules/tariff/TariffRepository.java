package org.market_mangement.market_management_backend.modules.tariff;

import org.market_mangement.market_management_backend.modules.meter.UtilityType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface TariffRepository extends JpaRepository<Tariff, Long> {
    List<Tariff> findByUtilityType(UtilityType utilityType);
    List<Tariff> findByUtilityTypeOrderByEffectiveFromDesc(UtilityType utilityType);
    Optional<Tariff> findByUtilityTypeAndEffectiveFrom(UtilityType utilityType, LocalDate effectiveFrom);
}