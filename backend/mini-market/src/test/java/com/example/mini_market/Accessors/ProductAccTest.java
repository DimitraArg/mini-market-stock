package com.example.mini_market.Accessors;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;

import com.example.mini_market.Accessor.ProductAcc;
import com.example.mini_market.Model.Product;

@JdbcTest
@Sql(scripts = {"/schema.sql", "/test-data/products.sql"})
class ProductAccTest {
    @Autowired
    private JdbcTemplate jdbc;

    @Test
    void testFindAll() {
        ProductAcc acc = new ProductAcc(jdbc);
        assertEquals(2, acc.findAll().size());
    }

    @Test
    void testAddAndUpdateQuantity() {
        ProductAcc acc = new ProductAcc(jdbc);
        Product p = new Product(0, "TestProduct", 10, BigDecimal.valueOf(5), BigDecimal.valueOf(24), 1, "ABC123", true);
        acc.add(p);
        assertEquals(3, acc.findAll().size());
        acc.updateQuantity(3, 99);
        assertEquals(99, acc.findById(3).getQuantity());
    }
}