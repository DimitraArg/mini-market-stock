package com.example.mini_market.Accessor;

import com.example.mini_market.Model.Transaction;

import com.example.mini_market.Mapper.TransactionRowMapper;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public class TransactionAcc {

    private final JdbcTemplate jdbc;

    public TransactionAcc(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public List<Transaction> findAll(){
        String sql = "SELECT t.id AS transaction_id, t.type, t.quantity, t.timestamp, " +
                    "p.id AS product_id, p.name AS product_name, p.barcode, p.active AS product_active, " +
                    "p.price, p.vat, " + 
                    "s.id AS supplier_id, s.name AS supplier_name " +
                "FROM transaction t " + 
                "JOIN product p ON t.product_id = p.id " +
                "JOIN supplier s ON t.supplier_id = s.id " +
                "WHERE t.active = true AND p.active = true AND s.active = true";
        return jdbc.query(sql, new TransactionRowMapper());
    }

    public Optional<Transaction> findById(int id) {
        String sql = "SELECT * FROM transaction WHERE id = ?";
        try {
            return Optional.of(
                jdbc.queryForObject(sql, new BeanPropertyRowMapper<>(Transaction.class), id)
            );
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public List<Transaction> findByDateRange(LocalDateTime start, LocalDateTime end){
        String sql = "SELECT * FROM transaction WHERE timestamp BETWEEN ? AND ?";
        return jdbc.query(sql, new BeanPropertyRowMapper<>(Transaction.class), start, end);
    }

    public int add(Transaction trans){
        String sql = "INSERT INTO transaction (product_id, supplier_id, type, quantity, timestamp) VALUES(?,?,?,?,?)";
        return jdbc.update(sql, trans.getProductId(),trans.getSupplierId(),trans.getType(),trans.getQuantity(),trans.getTimestamp());
    }

    public int update(Transaction trans){
        String sql = "UPDATE transaction SET product_id = ?, supplier_id = ?, type = ?, quantity = ? , timestamp = ? WHERE id = ?";
        return jdbc.update(sql,trans.getProductId(),trans.getSupplierId(),trans.getType(),trans.getQuantity(),trans.getTimestamp(),trans.getId());
    }

    // public int delete(int id){
    //     String sql = "DELETE FROM transaction WHERE id = ?";
    //     return jdbc.update(sql,id);
    // }

    public boolean existsByProductId(int productId) {
        String sql = "SELECT COUNT(*) FROM transaction WHERE product_id = ?";
        Integer count = jdbc.queryForObject(sql, Integer.class, productId);
        return count != null && count > 0;
    }

    public void deactivate(int id) {
        String sql = "UPDATE transaction SET active = false WHERE id = ?";
        jdbc.update(sql, id);
    }

    public void reactivate(int id) {
        String sql = "UPDATE transaction SET active = true WHERE id = ?";
        jdbc.update(sql, id);
    }
}
