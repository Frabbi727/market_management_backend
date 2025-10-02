package org.market_mangement.market_management_backend.modules.invoice;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, Long> {
    List<Invoice> findByPeriod(LocalDate period);
    List<Invoice> findByShopId(Long shopId);
    Optional<Invoice> findByPeriodAndShopId(LocalDate period, Long shopId);
    List<Invoice> findByStatus(String status);
    List<Invoice> findByLocked(Boolean locked);
    List<Invoice> findByPeriodAndShopIdIn(LocalDate period, List<Long> shopIds);
    Page<Invoice> findByPeriodAndShopIdIn(LocalDate period, List<Long> shopIds, Pageable pageable);
}