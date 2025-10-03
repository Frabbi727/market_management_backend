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
@RequestMapping("/api/invoice-adjustments")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Tag(name = "Invoice Adjustments", description = "Track and maintain manual invoice overrides")
public class InvoiceAdjustmentController {

    private final InvoiceAdjustmentService invoiceAdjustmentService;

    @GetMapping
    @Operation(summary = "List adjustments", description = "Returns all invoice adjustments")
    public ResponseEntity<List<InvoiceAdjustment>> getAllInvoiceAdjustments() {
        return ResponseEntity.ok(invoiceAdjustmentService.getAllInvoiceAdjustments());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Find adjustment", description = "Retrieves an adjustment by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Adjustment found"),
            @ApiResponse(responseCode = "404", description = "Adjustment not found")
    })
    public ResponseEntity<InvoiceAdjustment> getInvoiceAdjustmentById(@PathVariable Long id) {
        return invoiceAdjustmentService.getInvoiceAdjustmentById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/invoice/{invoiceId}")
    @Operation(summary = "List adjustments by invoice", description = "Returns adjustments belonging to an invoice")
    public ResponseEntity<List<InvoiceAdjustment>> getInvoiceAdjustmentsByInvoiceId(@PathVariable Long invoiceId) {
        return ResponseEntity.ok(invoiceAdjustmentService.getInvoiceAdjustmentsByInvoiceId(invoiceId));
    }

    @GetMapping("/item-type/{itemType}")
    @Operation(summary = "List adjustments by item type", description = "Filters adjustments by adjustment item type")
    public ResponseEntity<List<InvoiceAdjustment>> getInvoiceAdjustmentsByItemType(@PathVariable ItemType itemType) {
        return ResponseEntity.ok(invoiceAdjustmentService.getInvoiceAdjustmentsByItemType(itemType));
    }

    @PostMapping
    @Operation(summary = "Create adjustment", description = "Adds a new adjustment record")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Adjustment created"),
            @ApiResponse(responseCode = "400", description = "Validation error")
    })
    public ResponseEntity<InvoiceAdjustment> createInvoiceAdjustment(@Valid @RequestBody InvoiceAdjustment invoiceAdjustment) {
        return ResponseEntity.status(HttpStatus.CREATED).body(invoiceAdjustmentService.createInvoiceAdjustment(invoiceAdjustment));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update adjustment", description = "Updates an existing adjustment by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Adjustment updated"),
            @ApiResponse(responseCode = "400", description = "Validation error"),
            @ApiResponse(responseCode = "404", description = "Adjustment not found")
    })
    public ResponseEntity<InvoiceAdjustment> updateInvoiceAdjustment(@PathVariable Long id, @Valid @RequestBody InvoiceAdjustment invoiceAdjustment) {
        return ResponseEntity.ok(invoiceAdjustmentService.updateInvoiceAdjustment(id, invoiceAdjustment));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete adjustment", description = "Removes an adjustment permanently")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Adjustment deleted"),
            @ApiResponse(responseCode = "404", description = "Adjustment not found")
    })
    public ResponseEntity<Void> deleteInvoiceAdjustment(@PathVariable Long id) {
        invoiceAdjustmentService.deleteInvoiceAdjustment(id);
        return ResponseEntity.noContent().build();
    }
}
