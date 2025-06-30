package com.example.mini_market.Accessors;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;

import com.example.mini_market.Accessor.SupplierAcc;
import com.example.mini_market.Model.Supplier;

@JdbcTest
@Sql(scripts = {"/schema.sql", "/test-data/suppliers.sql"})
class SupplierAccTest {
    @Autowired
    private JdbcTemplate jdbc;

    @Test
    void testFindAll() {
        SupplierAcc acc = new SupplierAcc(jdbc);
        assertEquals(2, acc.findAll().size());
    }

    @Test
    void testAddAndReactivate() {
        SupplierAcc acc = new SupplierAcc(jdbc);
        Supplier s = new Supplier(0, "NewSupplier", 123, "test@mail.com", true);
        acc.add(s);
        assertEquals(3, acc.findAll().size());
        acc.deactivate(3);
        assertEquals(2, acc.findAll().size());
        acc.reactivate(3);
        assertEquals(3, acc.findAll().size());
    }
}