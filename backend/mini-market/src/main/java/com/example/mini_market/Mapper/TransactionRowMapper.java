package com.example.mini_market.Mapper;

import com.example.mini_market.Model.Product;
import com.example.mini_market.Model.Supplier;
import com.example.mini_market.Model.Transaction;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class TransactionRowMapper implements RowMapper<Transaction> {
    @Override
    public Transaction mapRow(ResultSet rs, int rowNum) throws SQLException {
        Transaction t = new Transaction();
        t.setId(rs.getInt("transaction_id"));
        t.setType(rs.getString("type"));
        t.setQuantity(rs.getInt("quantity"));
        t.setTimestamp(rs.getTimestamp("timestamp").toLocalDateTime());

        Product p = new Product();
        p.setId(rs.getInt("product_id"));
        p.setName(rs.getString("product_name"));
        p.setBarcode(rs.getString("barcode"));
        p.setPrice(rs.getBigDecimal("price"));
        p.setVat(rs.getBigDecimal("vat"));
        p.setActive(rs.getBoolean("product_active"));  

        t.setProduct(p);

        Supplier s = new Supplier();
        s.setId(rs.getInt("supplier_id"));
        s.setName(rs.getString("supplier_name"));

        t.setSupplierId(s.getId());
        t.setSupplier(s);

        return t;
    }
}
