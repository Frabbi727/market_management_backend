package org.market_mangement.market_management_backend.modules.invoice;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class InvoiceAdjustmentService {

    private final InvoiceAdjustmentRepository invoiceAdjustmentRepository;

    public List<InvoiceAdjustment> getAllInvoiceAdjustments() {
        return invoiceAdjustmentRepository.findAll();
    }

    public Optional<InvoiceAdjustment> getInvoiceAdjustmentById(Long id) {
        return invoiceAdjustmentRepository.findById(id);
    }

    public List<InvoiceAdjustment> getInvoiceAdjustmentsByInvoiceId(Long invoiceId) {
        return invoiceAdjustmentRepository.findByInvoiceId(invoiceId);
    }

    public List<InvoiceAdjustment> getInvoiceAdjustmentsByItemType(ItemType itemType) {
        return invoiceAdjustmentRepository.findByItemType(itemType);
    }

    public InvoiceAdjustment createInvoiceAdjustment(InvoiceAdjustment invoiceAdjustment) {
        return invoiceAdjustmentRepository.save(invoiceAdjustment);
    }

    public InvoiceAdjustment updateInvoiceAdjustment(Long id, InvoiceAdjustment invoiceAdjustment) {
        invoiceAdjustment.setId(id);
        return invoiceAdjustmentRepository.save(invoiceAdjustment);
    }

    public void deleteInvoiceAdjustment(Long id) {
        invoiceAdjustmentRepository.deleteById(id);
    }
}