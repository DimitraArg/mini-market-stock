package com.example.mini_market.DTOs;

import org.junit.jupiter.api.Test;

import com.example.mini_market.DataTransfer.ProductDTO;
import com.example.mini_market.DataTransfer.TransactionDTO;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class TransactionDTOTest {

    @Test
    public void testTransactionDTO_GettersSetters() {
        ProductDTO.SimpleEntity supplier = new ProductDTO.SimpleEntity(2, "Coca Cola SA");
        TransactionDTO.ProductSummary summary = new TransactionDTO.ProductSummary(
                1, "Cola", "1234567890", 1.5, 0.24, supplier
        );

        TransactionDTO dto = new TransactionDTO();
        dto.setId(10);
        dto.setType("IN");
        dto.setQuantity(50);
        dto.setTimestamp(LocalDateTime.of(2023, 1, 1, 12, 0));
        dto.setProduct(summary);
        dto.setSupplier(supplier);

        assertEquals(10, dto.getId());
        assertEquals("IN", dto.getType());
        assertEquals(50, dto.getQuantity());
        assertEquals(LocalDateTime.of(2023, 1, 1, 12, 0), dto.getTimestamp());
        assertEquals("Cola", dto.getProduct().getName());
        assertEquals("Coca Cola SA", dto.getSupplier().getName());
    }

    @Test
    public void testProductSummary() {
        ProductDTO.SimpleEntity supplier = new ProductDTO.SimpleEntity(1, "Test Supplier");
        TransactionDTO.ProductSummary ps = new TransactionDTO.ProductSummary(10, "Product A", "999", 2.0, 0.2, supplier);

        assertEquals(10, ps.getId());
        assertEquals("Product A", ps.getName());
        assertEquals("999", ps.getBarcode());
        assertEquals(2.0, ps.getPrice());
        assertEquals(0.2, ps.getVat());
        assertEquals("Test Supplier", ps.getSupplier().getName());
    }
}

