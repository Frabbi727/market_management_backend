package org.market_mangement.market_management_backend.modules.invoice;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/invoice-adjustments")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class InvoiceAdjustmentController {

    private final InvoiceAdjustmentService invoiceAdjustmentService;

    @GetMapping
    public ResponseEntity<List<InvoiceAdjustment>> getAllInvoiceAdjustments() {
        return ResponseEntity.ok(invoiceAdjustmentService.getAllInvoiceAdjustments());
    }

    @GetMapping("/{id}")
    public ResponseEntity<InvoiceAdjustment> getInvoiceAdjustmentById(@PathVariable Long id) {
        return invoiceAdjustmentService.getInvoiceAdjustmentById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/invoice/{invoiceId}")
    public ResponseEntity<List<InvoiceAdjustment>> getInvoiceAdjustmentsByInvoiceId(@PathVariable Long invoiceId) {
        return ResponseEntity.ok(invoiceAdjustmentService.getInvoiceAdjustmentsByInvoiceId(invoiceId));
    }

    @GetMapping("/item-type/{itemType}")
    public ResponseEntity<List<InvoiceAdjustment>> getInvoiceAdjustmentsByItemType(@PathVariable ItemType itemType) {
        return ResponseEntity.ok(invoiceAdjustmentService.getInvoiceAdjustmentsByItemType(itemType));
    }

    @PostMapping
    public ResponseEntity<InvoiceAdjustment> createInvoiceAdjustment(@Valid @RequestBody InvoiceAdjustment invoiceAdjustment) {
        return ResponseEntity.status(HttpStatus.CREATED).body(invoiceAdjustmentService.createInvoiceAdjustment(invoiceAdjustment));
    }

    @PutMapping("/{id}")
    public ResponseEntity<InvoiceAdjustment> updateInvoiceAdjustment(@PathVariable Long id, @Valid @RequestBody InvoiceAdjustment invoiceAdjustment) {
        return ResponseEntity.ok(invoiceAdjustmentService.updateInvoiceAdjustment(id, invoiceAdjustment));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInvoiceAdjustment(@PathVariable Long id) {
        invoiceAdjustmentService.deleteInvoiceAdjustment(id);
        return ResponseEntity.noContent().build();
    }
}