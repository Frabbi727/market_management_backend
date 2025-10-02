package org.market_mangement.market_management_backend.modules.invoice;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/invoice-items")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class InvoiceItemController {

    private final InvoiceItemService invoiceItemService;

    @GetMapping
    public ResponseEntity<List<InvoiceItem>> getAllInvoiceItems() {
        return ResponseEntity.ok(invoiceItemService.getAllInvoiceItems());
    }

    @GetMapping("/{id}")
    public ResponseEntity<InvoiceItem> getInvoiceItemById(@PathVariable Long id) {
        return invoiceItemService.getInvoiceItemById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/invoice/{invoiceId}")
    public ResponseEntity<List<InvoiceItem>> getInvoiceItemsByInvoiceId(@PathVariable Long invoiceId) {
        return ResponseEntity.ok(invoiceItemService.getInvoiceItemsByInvoiceId(invoiceId));
    }

    @GetMapping("/item-type/{itemType}")
    public ResponseEntity<List<InvoiceItem>> getInvoiceItemsByItemType(@PathVariable ItemType itemType) {
        return ResponseEntity.ok(invoiceItemService.getInvoiceItemsByItemType(itemType));
    }

    @GetMapping("/overridden")
    public ResponseEntity<List<InvoiceItem>> getOverriddenInvoiceItems() {
        return ResponseEntity.ok(invoiceItemService.getOverriddenInvoiceItems());
    }

    @PostMapping
    public ResponseEntity<InvoiceItem> createInvoiceItem(@Valid @RequestBody InvoiceItem invoiceItem) {
        return ResponseEntity.status(HttpStatus.CREATED).body(invoiceItemService.createInvoiceItem(invoiceItem));
    }

    @PutMapping("/{id}")
    public ResponseEntity<InvoiceItem> updateInvoiceItem(@PathVariable Long id, @Valid @RequestBody InvoiceItem invoiceItem) {
        return ResponseEntity.ok(invoiceItemService.updateInvoiceItem(id, invoiceItem));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInvoiceItem(@PathVariable Long id) {
        invoiceItemService.deleteInvoiceItem(id);
        return ResponseEntity.noContent().build();
    }
}