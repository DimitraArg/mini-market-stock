package com.example.mini_market.Services;

import com.example.mini_market.Accessor.ProductAcc;
import com.example.mini_market.Exceptions.NotFoundException;
import com.example.mini_market.Model.Product;
import com.example.mini_market.Service.ProductServ;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Arrays;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ProductServTest {

    private ProductAcc productAcc;
    private ProductServ productServ;

    @BeforeEach
    public void setUp() {
        productAcc = mock(ProductAcc.class);
        productServ = new ProductServ(productAcc);
    }

    @Test
    public void testGetAllProducts() {
        when(productAcc.findAll()).thenReturn(Arrays.asList(new Product(), new Product()));
        assertEquals(2, productServ.getAllProducts().size());
    }

    @Test
    public void testGetProductById_Found() {
        Product p = new Product();
        p.setId(1);
        when(productAcc.findById(1)).thenReturn(p);
        assertEquals(1, productServ.getProductById(1).getId());
    }

    @Test
    public void testGetProductById_NotFound() {
        when(productAcc.findById(2)).thenReturn(null); 
        assertThrows(NotFoundException.class, () -> productServ.getProductById(2));
    }

    @Test
    public void testAddProduct() {
        Product p = new Product();
        p.setBarcode("123456");
        p.setName("Test Product");
        p.setPrice(BigDecimal.valueOf(10.0));
        p.setVat(BigDecimal.valueOf(24.0));
        p.setQuantity(100);
        p.setCategoryId(1); 
        productServ.createProduct(p);
        verify(productAcc).add(p);
    }

    @Test
    public void testUpdateProduct() {
        Product p = new Product();
        p.setId(5);
        when(productAcc.findById(5)).thenReturn(p);
        productServ.updateProduct(p);
        verify(productAcc).update(p);
    }

    // @Test
    // public void testDeleteProduct() {
    //     productServ.deleteProduct(3);
    //     verify(productAcc).delete(3);
    // }

    @Test
    public void testDeactivateAndReactivateProduct() {
        productServ.deActivate(4);
        verify(productAcc).deActivate(4);

        productServ.reactivateProduct(4);
        verify(productAcc).reactivate(4);
    }

    @Test
    public void testUpdateQuantity() {
        productServ.updateQuantity(1, 15);
        verify(productAcc).updateQuantity(1, 15);
    }
}
