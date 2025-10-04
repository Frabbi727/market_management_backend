package org.market_mangement.market_management_backend.modules.monthlycost;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MonthlyCostRepository extends JpaRepository<MonthlyCost, Long> {

    /**
     * Find by market, year, and month
     */
    Optional<MonthlyCost> findByMarket_IdAndYearAndMonth(Long marketId, Integer year, Integer month);

    /**
     * Find all by market ID
     */
    List<MonthlyCost> findByMarket_IdOrderByYearDescMonthDesc(Long marketId);

    /**
     * Find all by market ID and year
     */
    List<MonthlyCost> findByMarket_IdAndYearOrderByMonthDesc(Long marketId, Integer year);

    /**
     * Find all by market ID and locked status
     */
    List<MonthlyCost> findByMarket_IdAndLockedOrderByYearDescMonthDesc(Long marketId, Boolean locked);

    /**
     * Check if exists for market, year, month
     */
    boolean existsByMarket_IdAndYearAndMonth(Long marketId, Integer year, Integer month);

    /**
     * Find previous month's configuration
     */
    @Query("SELECT mc FROM MonthlyCost mc WHERE mc.market.id = :marketId " +
            "AND (mc.year < :year OR (mc.year = :year AND mc.month < :month)) " +
            "ORDER BY mc.year DESC, mc.month DESC LIMIT 1")
    Optional<MonthlyCost> findPreviousMonth(@Param("marketId") Long marketId,
                                             @Param("year") Integer year,
                                             @Param("month") Integer month);
}