package org.market_mangement.market_management_backend.modules.invoice;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class InvoiceService {

    private final InvoiceRepository invoiceRepository;

    public List<Invoice> getAllInvoices() {
        return invoiceRepository.findAll();
    }

    public Optional<Invoice> getInvoiceById(Long id) {
        return invoiceRepository.findById(id);
    }

    public List<Invoice> getInvoicesByPeriod(String period) {
        return invoiceRepository.findByPeriod(period);
    }

    public List<Invoice> getInvoicesByShopId(Long shopId) {
        return invoiceRepository.findByShopId(shopId);
    }

    public Optional<Invoice> getInvoiceByPeriodAndShopId(String period, Long shopId) {
        return invoiceRepository.findByPeriodAndShopId(period, shopId);
    }

    public List<Invoice> getInvoicesByStatus(String status) {
        return invoiceRepository.findByStatus(status);
    }

    public List<Invoice> getLockedInvoices() {
        return invoiceRepository.findByLocked(true);
    }

    public Invoice createInvoice(Invoice invoice) {
        return invoiceRepository.save(invoice);
    }

    public Invoice updateInvoice(Long id, Invoice invoice) {
        invoice.setId(id);
        return invoiceRepository.save(invoice);
    }

    public void deleteInvoice(Long id) {
        invoiceRepository.deleteById(id);
    }
}