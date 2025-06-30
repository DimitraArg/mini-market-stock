package com.example.mini_market.Mapper;

import com.example.mini_market.Model.Transaction;
import org.junit.jupiter.api.Test;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TransactionRowMapperTest {

    @Test
    public void testMapRow() throws Exception {
        ResultSet rs = mock(ResultSet.class);
        TransactionRowMapper mapper = new TransactionRowMapper();

        when(rs.getInt("transaction_id")).thenReturn(1);
        when(rs.getString("type")).thenReturn("IN");
        when(rs.getInt("quantity")).thenReturn(100);
        when(rs.getTimestamp("timestamp")).thenReturn(Timestamp.valueOf(LocalDateTime.of(2024, 5, 5, 10, 0)));

        when(rs.getInt("product_id")).thenReturn(10);
        when(rs.getString("product_name")).thenReturn("Milk");
        when(rs.getString("barcode")).thenReturn("555");
        when(rs.getBigDecimal("price")).thenReturn(java.math.BigDecimal.valueOf(1.99));
        when(rs.getBigDecimal("vat")).thenReturn(java.math.BigDecimal.valueOf(0.13));
        when(rs.getBoolean("product_active")).thenReturn(true);

        when(rs.getInt("supplier_id")).thenReturn(20);
        when(rs.getString("supplier_name")).thenReturn("Dairy Co");

        Transaction t = mapper.mapRow(rs, 0);

        assertEquals(1, t.getId());
        assertEquals("IN", t.getType());
        assertEquals(100, t.getQuantity());
        assertEquals(10, t.getProduct().getId());
        assertEquals("Milk", t.getProduct().getName());
        assertEquals("555", t.getProduct().getBarcode());
        assertEquals(java.math.BigDecimal.valueOf(1.99), t.getProduct().getPrice());
        assertEquals(20, t.getSupplierId());
        assertEquals("Dairy Co", t.getSupplier().getName());
    }
}
