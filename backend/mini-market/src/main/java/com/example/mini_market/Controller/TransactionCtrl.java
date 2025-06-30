package com.example.mini_market.Controller;

import com.example.mini_market.DataTransfer.ProductDTO;
import com.example.mini_market.DataTransfer.TransactionDTO;
import com.example.mini_market.Model.Product;
import com.example.mini_market.Model.Supplier;
import com.example.mini_market.Model.Transaction;
import com.example.mini_market.Service.ProductServ;
import com.example.mini_market.Service.TransactionServ;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/transactions")
public class TransactionCtrl {

    private final TransactionServ serv;
    private final ProductServ prodServ;
    // private final SupplierServ supServ;


    public TransactionCtrl(TransactionServ serv, ProductServ prodServ) {
        this.serv = serv;
        this.prodServ = prodServ;
    }

    @GetMapping
    public ResponseEntity<List<TransactionDTO>> getAllTransactions(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end
    ) {
        List<Transaction> trans;

        if ((start != null && end == null) || (start == null && end != null)) {
            throw new IllegalArgumentException("You must provide both 'start' and 'end' date parameters together.");
        }

        if (start != null && end != null) {
            trans = serv.findTransByDateRange(start, end);
        } else {
            trans = serv.getAllTransactions();
        }

        List<TransactionDTO> dtos = trans.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TransactionDTO> getTransactionById(@PathVariable int id) {
        Transaction t = serv.findTransactionById(id);
        return ResponseEntity.ok(toDTO(t));
    }

    @PostMapping
    public ResponseEntity<String> createTransaction(@RequestBody Transaction t) {
        if (t.getQuantity() <= 0) {
            throw new IllegalArgumentException("Quantity must be positive.");
        }

        Product p = prodServ.getProductById(t.getProductId());

        if (p == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Product with ID " + t.getProductId() + " not found.");
        }

        if (!p.isActive()) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("INACTIVE_PRODUCT:" + t.getProductId());
        }

        serv.createTransaction(t);
        return ResponseEntity.ok("Transaction created successfully.");
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateTransaction(@PathVariable int id, @RequestBody Transaction t) {
        serv.updateTransaction(id, t);
        return ResponseEntity.ok("Transaction updated successfully.");
    }

    // @DeleteMapping("/{id}")
    // public ResponseEntity<String> deleteTransaction(@PathVariable int id) {
    //     serv.deleteTransaction(id);
    //     return ResponseEntity.ok("Transaction deleted successfully.");
    // }

    @PutMapping("/{id}/deactivate")
    public ResponseEntity<?> deactivateTransaction(@PathVariable int id) {
        serv.deactivateTransaction(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/reactivate")
    public ResponseEntity<?> reactivateTransaction(@PathVariable int id) {
        serv.reactivateTransaction(id);
        return ResponseEntity.ok().build();
    }

    private TransactionDTO toDTO(Transaction t) {
        TransactionDTO dto = new TransactionDTO();

        dto.setId(t.getId());
        dto.setType(t.getType());
        dto.setQuantity(t.getQuantity());
        dto.setTimestamp(t.getTimestamp());

        ProductDTO.SimpleEntity supplierEntity = null;

        if (t.getSupplier() != null) {
            Supplier s = t.getSupplier();
            supplierEntity = new ProductDTO.SimpleEntity(s.getId(), s.getName());
            dto.setSupplier(supplierEntity);
        }

        if (t.getProduct() != null) {
            Product p = t.getProduct();

            double price = p.getPrice() != null ? p.getPrice().doubleValue() : 0.0;
            double vat = p.getVat() != null ? p.getVat().doubleValue() : 0.0;

            TransactionDTO.ProductSummary productSummary = new TransactionDTO.ProductSummary(
                p.getId(),
                p.getName(),
                p.getBarcode(),
                price,
                vat,
                supplierEntity
            );

            dto.setProduct(productSummary);
        }

        return dto;
    }
}
