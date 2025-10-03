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
@RequestMapping("/api/invoice-items")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Tag(name = "Invoice Items", description = "CRUD endpoints for invoice line items")
public class InvoiceItemController {

    private final InvoiceItemService invoiceItemService;

    @GetMapping
    @Operation(summary = "List invoice items", description = "Returns every invoice line item")
    public ResponseEntity<List<InvoiceItem>> getAllInvoiceItems() {
        return ResponseEntity.ok(invoiceItemService.getAllInvoiceItems());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Find invoice item", description = "Retrieves a line item by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Invoice item found"),
            @ApiResponse(responseCode = "404", description = "Invoice item not found")
    })
    public ResponseEntity<InvoiceItem> getInvoiceItemById(@PathVariable Long id) {
        return invoiceItemService.getInvoiceItemById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/invoice/{invoiceId}")
    @Operation(summary = "List items by invoice", description = "Returns items belonging to an invoice")
    public ResponseEntity<List<InvoiceItem>> getInvoiceItemsByInvoiceId(@PathVariable Long invoiceId) {
        return ResponseEntity.ok(invoiceItemService.getInvoiceItemsByInvoiceId(invoiceId));
    }

    @GetMapping("/item-type/{itemType}")
    @Operation(summary = "List items by type", description = "Filters invoice items by their type")
    public ResponseEntity<List<InvoiceItem>> getInvoiceItemsByItemType(@PathVariable ItemType itemType) {
        return ResponseEntity.ok(invoiceItemService.getInvoiceItemsByItemType(itemType));
    }

    @GetMapping("/overridden")
    @Operation(summary = "List overridden items", description = "Returns invoice items whose values were overridden")
    public ResponseEntity<List<InvoiceItem>> getOverriddenInvoiceItems() {
        return ResponseEntity.ok(invoiceItemService.getOverriddenInvoiceItems());
    }

    @PostMapping
    @Operation(summary = "Create invoice item", description = "Adds a new invoice line item")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Invoice item created"),
            @ApiResponse(responseCode = "400", description = "Validation error")
    })
    public ResponseEntity<InvoiceItem> createInvoiceItem(@Valid @RequestBody InvoiceItem invoiceItem) {
        return ResponseEntity.status(HttpStatus.CREATED).body(invoiceItemService.createInvoiceItem(invoiceItem));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update invoice item", description = "Updates an existing invoice item")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Invoice item updated"),
            @ApiResponse(responseCode = "400", description = "Validation error"),
            @ApiResponse(responseCode = "404", description = "Invoice item not found")
    })
    public ResponseEntity<InvoiceItem> updateInvoiceItem(@PathVariable Long id, @Valid @RequestBody InvoiceItem invoiceItem) {
        return ResponseEntity.ok(invoiceItemService.updateInvoiceItem(id, invoiceItem));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete invoice item", description = "Removes a line item")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Invoice item deleted"),
            @ApiResponse(responseCode = "404", description = "Invoice item not found")
    })
    public ResponseEntity<Void> deleteInvoiceItem(@PathVariable Long id) {
        invoiceItemService.deleteInvoiceItem(id);
        return ResponseEntity.noContent().build();
    }
}
