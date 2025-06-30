package com.example.mini_market.DTOs;


import org.junit.jupiter.api.Test;

import com.example.mini_market.DataTransfer.ProductDTO;

import static org.junit.jupiter.api.Assertions.*;

public class ProductDTOTest {

    @Test
    public void testProductDTO_GettersSetters() {
        ProductDTO.SimpleEntity category = new ProductDTO.SimpleEntity(1, "Beverages");

        ProductDTO product = new ProductDTO(100, "Cola", 20, 1.5, 0.24, "1234567890", category);

        assertEquals(100, product.getId());
        assertEquals("Cola", product.getName());
        assertEquals(20, product.getQuantity());
        assertEquals(1.5, product.getPrice());
        assertEquals(0.24, product.getVat());
        assertEquals("1234567890", product.getBarcode());
        assertEquals("Beverages", product.getCategory().getName());
    }

    @Test
    public void testSimpleEntity() {
        ProductDTO.SimpleEntity entity = new ProductDTO.SimpleEntity(5, "Snacks");
        assertEquals(5, entity.getId());
        assertEquals("Snacks", entity.getName());
    }
}
