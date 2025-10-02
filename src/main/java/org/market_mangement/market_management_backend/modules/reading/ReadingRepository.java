package org.market_mangement.market_management_backend.modules.reading;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReadingRepository extends JpaRepository<Reading, Long> {

    List<Reading> findByMeterId(Long meterId);

    List<Reading> findByPeriod(String period);

    Optional<Reading> findByMeterIdAndPeriod(Long meterId, String period);
}