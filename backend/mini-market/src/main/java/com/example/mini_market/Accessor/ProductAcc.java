package com.example.mini_market.Accessor;


import com.example.mini_market.Model.Product;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class ProductAcc {
    private final JdbcTemplate jdbc;

    public ProductAcc(JdbcTemplate jdbc){
        this.jdbc = jdbc;
    }

    public List<Product> findAll(){
        String sql = "SELECT * FROM product WHERE active = true";
        return jdbc.query(sql,new BeanPropertyRowMapper<>(Product.class));
    }

    public Product findById(int id){
        String sql = "SELECT * FROM product WHERE id = ?" ;
        return jdbc.queryForObject(sql,new BeanPropertyRowMapper<>(Product.class),id);
    }

    public Optional<Product> findByBarcode(String barcode) {
        String sql = "SELECT * FROM product WHERE barcode = ?";
        List<Product> results = jdbc.query(sql, new BeanPropertyRowMapper<>(Product.class), barcode);
        return results.stream().findFirst();
    }

    public int add(Product prod) {
        String sql = "INSERT INTO product (name, quantity, price, vat, category_id, barcode) VALUES (?, ?, ?, ?,?,?)";
        return jdbc.update(sql, prod.getName(), prod.getQuantity(), prod.getPrice(), prod.getVat(), prod.getCategoryId(),  prod.getBarcode());
    }

    public int update(Product prod) {
        String sql = "UPDATE product SET name = ?, quantity = ?, price = ?, vat = ?, category_id = ?,  barcode = ? WHERE id = ?";
        return jdbc.update(sql, prod.getName(), prod.getQuantity(), prod.getPrice(), prod.getVat(), prod.getCategoryId(), prod.getBarcode(), prod.getId());
    }

    // public int delete(int id) {
    //     String sql = "DELETE FROM product WHERE id = ?";
    //     return jdbc.update(sql, id);
    // }

    public void deActivate(int id){
        String sql = "UPDATE product SET active = false WHERE id = ?";
        jdbc.update(sql, id);
    }

    public boolean nameAlreadyExists(String name){
        String sql = "SELECT COUNT(*) FROM product WHERE name = ?";
        Integer count = jdbc.queryForObject(sql, Integer.class, name);
        return count != null && count > 0;
    }

    public boolean barcodeAlreadyExists(String barcode) {
        String sql = "SELECT COUNT(*) FROM product WHERE barcode = ?";
        Integer count = jdbc.queryForObject(sql, Integer.class, barcode);
        return count != null && count > 0;
    }

    public Optional<Product> findInactiveByBarcode(String barcode) {
        String sql = "SELECT * FROM product WHERE barcode = ? AND active = false";
        List<Product> result = jdbc.query(sql, new BeanPropertyRowMapper<>(Product.class), barcode);
        return result.stream().findFirst();
    }

    public void reactivate(int id) {
        String sql = "UPDATE product SET active = true WHERE id = ?";
        jdbc.update(sql, id);
    }

    public void updateQuantity(int id, int newQuantity) {
        String sql = "UPDATE product SET quantity = ? WHERE id = ?";
        jdbc.update(sql, newQuantity, id);
    }


}
