package com.example.mini_market.Accessors;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;

import com.example.mini_market.Accessor.TransactionAcc;
import com.example.mini_market.Model.Transaction;

@JdbcTest
@Sql(scripts = {"/schema.sql", "/test-data/transactions.sql"})
class TransactionAccTest {
    @Autowired
    private JdbcTemplate jdbc;

    @Test
    void testAddTransaction() {
        TransactionAcc acc = new TransactionAcc(jdbc);
        Transaction t = new Transaction();
        t.setProductId(1);
        t.setSupplierId(1);
        t.setQuantity(10);
        t.setType("IN");
        t.setTimestamp(LocalDateTime.now());
        acc.add(t);
        assertTrue(acc.findAll().size() >= 1);
    }

    @Test
    void testDeactivateReactivate() {
        TransactionAcc acc = new TransactionAcc(jdbc);
        acc.deactivate(1);
        assertEquals(0, acc.findAll().size());
        acc.reactivate(1);
        assertEquals(1, acc.findAll().size());
    }
}