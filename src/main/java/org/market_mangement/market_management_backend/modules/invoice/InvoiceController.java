package org.market_mangement.market_management_backend.modules.invoice;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/invoices")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Tag(name = "Invoices", description = "CRUD operations and lookups for invoices")
public class InvoiceController {

    private final InvoiceService invoiceService;

    @GetMapping
    @Operation(summary = "List invoices", description = "Returns all invoices sorted by persistence order")
    public ResponseEntity<List<Invoice>> getAllInvoices() {
        return ResponseEntity.ok(invoiceService.getAllInvoices());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Find invoice", description = "Retrieves an invoice by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Invoice found"),
            @ApiResponse(responseCode = "404", description = "Invoice not found")
    })
    public ResponseEntity<Invoice> getInvoiceById(@PathVariable Long id) {
        return invoiceService.getInvoiceById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/period/{period}")
    @Operation(summary = "List invoices by period", description = "Finds invoices generated for a specific billing period")
    public ResponseEntity<List<Invoice>> getInvoicesByPeriod(@PathVariable String period) {
        return ResponseEntity.ok(invoiceService.getInvoicesByPeriod(period));
    }

    @GetMapping("/shop/{shopId}")
    @Operation(summary = "List invoices by shop", description = "Returns all invoices associated with a shop")
    public ResponseEntity<List<Invoice>> getInvoicesByShopId(@PathVariable Long shopId) {
        return ResponseEntity.ok(invoiceService.getInvoicesByShopId(shopId));
    }

    @GetMapping("/period/{period}/shop/{shopId}")
    @Operation(summary = "Find invoice by period and shop", description = "Looks up the invoice for a shop within a billing period")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Invoice found"),
            @ApiResponse(responseCode = "404", description = "Invoice not found")
    })
    public ResponseEntity<Invoice> getInvoiceByPeriodAndShopId(@PathVariable String period, @PathVariable Long shopId) {
        return invoiceService.getInvoiceByPeriodAndShopId(period, shopId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/status/{status}")
    @Operation(summary = "List invoices by status", description = "Filters invoices using their workflow status")
    public ResponseEntity<List<Invoice>> getInvoicesByStatus(@PathVariable String status) {
        return ResponseEntity.ok(invoiceService.getInvoicesByStatus(status));
    }

    @GetMapping("/locked")
    @Operation(summary = "List locked invoices", description = "Returns invoices that are marked as locked")
    public ResponseEntity<List<Invoice>> getLockedInvoices() {
        return ResponseEntity.ok(invoiceService.getLockedInvoices());
    }

    @PostMapping
    @Operation(summary = "Create invoice", description = "Registers a new invoice including its items")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Invoice created"),
            @ApiResponse(responseCode = "400", description = "Validation error")
    })
    public ResponseEntity<Invoice> createInvoice(@Valid @RequestBody Invoice invoice) {
        return ResponseEntity.status(HttpStatus.CREATED).body(invoiceService.createInvoice(invoice));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update invoice", description = "Replaces fields on an existing invoice")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Invoice updated"),
            @ApiResponse(responseCode = "400", description = "Validation error"),
            @ApiResponse(responseCode = "404", description = "Invoice not found")
    })
    public ResponseEntity<Invoice> updateInvoice(@PathVariable Long id, @Valid @RequestBody Invoice invoice) {
        return ResponseEntity.ok(invoiceService.updateInvoice(id, invoice));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete invoice", description = "Removes an invoice and detaches related data")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Invoice deleted"),
            @ApiResponse(responseCode = "404", description = "Invoice not found")
    })
    public ResponseEntity<Void> deleteInvoice(@PathVariable Long id) {
        invoiceService.deleteInvoice(id);
        return ResponseEntity.noContent().build();
    }
}
