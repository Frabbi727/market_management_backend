package org.market_mangement.market_management_backend.modules.invoice;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InvoiceItemRepository extends JpaRepository<InvoiceItem, Long> {
    List<InvoiceItem> findByInvoiceId(Long invoiceId);
    List<InvoiceItem> findByItemType(ItemType itemType);
    List<InvoiceItem> findByIsOverridden(Boolean isOverridden);
}