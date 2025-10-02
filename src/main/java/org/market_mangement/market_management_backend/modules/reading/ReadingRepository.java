package org.market_mangement.market_management_backend.modules.reading;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ReadingRepository extends JpaRepository<Reading, Long> {

    List<Reading> findByMeterId(Long meterId);

    List<Reading> findByPeriod(LocalDate period);

    Optional<Reading> findByMeterIdAndPeriod(Long meterId, LocalDate period);

    List<Reading> findByMeterIdInAndPeriod(List<Long> meterIds, LocalDate period);
}