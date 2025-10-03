package org.market_mangement.market_management_backend.modules.periodinput;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface PeriodInputRepository extends JpaRepository<PeriodInput, LocalDate> {
    List<PeriodInput> findAllByOrderByPeriodDesc();
}
