package org.market_mangement.market_management_backend.modules.invoice;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class InvoiceItemService {

    private final InvoiceItemRepository invoiceItemRepository;

    public List<InvoiceItem> getAllInvoiceItems() {
        return invoiceItemRepository.findAll();
    }

    public Optional<InvoiceItem> getInvoiceItemById(Long id) {
        return invoiceItemRepository.findById(id);
    }

    public List<InvoiceItem> getInvoiceItemsByInvoiceId(Long invoiceId) {
        return invoiceItemRepository.findByInvoiceId(invoiceId);
    }

    public List<InvoiceItem> getInvoiceItemsByItemType(ItemType itemType) {
        return invoiceItemRepository.findByItemType(itemType);
    }

    public List<InvoiceItem> getOverriddenInvoiceItems() {
        return invoiceItemRepository.findByIsOverridden(true);
    }

    public InvoiceItem createInvoiceItem(InvoiceItem invoiceItem) {
        return invoiceItemRepository.save(invoiceItem);
    }

    public InvoiceItem updateInvoiceItem(Long id, InvoiceItem invoiceItem) {
        invoiceItem.setId(id);
        return invoiceItemRepository.save(invoiceItem);
    }

    public void deleteInvoiceItem(Long id) {
        invoiceItemRepository.deleteById(id);
    }
}