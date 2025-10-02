package org.market_mangement.market_management_backend.modules.invoice;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, Long> {
    List<Invoice> findByPeriod(String period);
    List<Invoice> findByShopId(Long shopId);
    Optional<Invoice> findByPeriodAndShopId(String period, Long shopId);
    List<Invoice> findByStatus(String status);
    List<Invoice> findByLocked(Boolean locked);
}