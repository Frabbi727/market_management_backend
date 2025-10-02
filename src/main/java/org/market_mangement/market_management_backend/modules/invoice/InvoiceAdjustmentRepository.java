package org.market_mangement.market_management_backend.modules.invoice;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InvoiceAdjustmentRepository extends JpaRepository<InvoiceAdjustment, Long> {
    List<InvoiceAdjustment> findByInvoiceId(Long invoiceId);
    List<InvoiceAdjustment> findByItemType(ItemType itemType);
}