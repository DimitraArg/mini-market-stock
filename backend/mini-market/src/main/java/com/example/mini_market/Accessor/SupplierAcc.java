package com.example.mini_market.Accessor;

import com.example.mini_market.Model.Supplier;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class SupplierAcc {

    private final JdbcTemplate jdbc;

    public SupplierAcc(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public List<Supplier> findAll(){
        String sql = "SELECT * FROM supplier WHERE active = true";
        return jdbc.query(sql, new BeanPropertyRowMapper<>(Supplier.class));
    }

    public Supplier findById(int id){
        String sql = "SELECT * FROM supplier WHERE id = ?";
        return jdbc.queryForObject(sql, new BeanPropertyRowMapper<>(Supplier.class), id);
    }

    public int add(Supplier sup){
        String sql = "INSERT INTO supplier (name,phone,email) VALUES (?,?,?)";
        return jdbc.update(sql,sup.getName(),sup.getPhone(),sup.getEmail());
    }

    public int update(Supplier sup){
        String sql = "UPDATE supplier SET name = ?, phone = ?, email = ? WHERE id = ?";
        return jdbc.update(sql,sup.getName(),sup.getPhone(),sup.getEmail(),sup.getId());
    }

    // public int delete(int id){
    //     String sql = "DELETE FROM supplier WHERE id = ?";
    //     return jdbc.update(sql,id);
    // }

    public boolean nameAlreadyExists(String name){
        String sql = "SELECT COUNT(*) FROM supplier WHERE name = ?";
        Integer count = jdbc.queryForObject(sql, Integer.class, name);
        return count != null && count > 0;
    }

    public Optional<Supplier> findInactiveByName(String name) {
        String sql = "SELECT * FROM supplier WHERE name = ? AND active = false";
        List<Supplier> result = jdbc.query(sql, new BeanPropertyRowMapper<>(Supplier.class), name);
        return result.stream().findFirst();
    }

    public void deactivate(int id) {
        String sql = "UPDATE supplier SET active = false WHERE id = ?";
        jdbc.update(sql, id);
    }

    public void reactivate(int id) {
        String sql = "UPDATE supplier SET active = true WHERE id = ?";
        jdbc.update(sql, id);
    }
}
