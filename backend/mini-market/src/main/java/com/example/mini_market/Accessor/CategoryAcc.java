package com.example.mini_market.Accessor;

import com.example.mini_market.Model.Category;


import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class CategoryAcc {
    private final JdbcTemplate jdbc;


    public CategoryAcc(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public List<Category> findAll(){
        String sql = "SELECT * FROM category WHERE active = true";
        return jdbc.query(sql, new BeanPropertyRowMapper<>(Category.class));
    }

    public Category findById(int id){
        String sql = "SELECT * FROM category WHERE id = ?";
        return jdbc.queryForObject(sql, new BeanPropertyRowMapper<>(Category.class), id);
    }

    public int add(Category cat){
        String sql = "INSERT INTO category (name) VALUES (?)";
        return jdbc.update(sql, cat.getName());
    }

    public int update(Category cat){
        String sql = "UPDATE category SET name = ? WHERE id = ?";
        return jdbc.update(sql,cat.getName(),cat.getId());
    }

    // public int delete(int id){
    //     String sql = "DELETE FROM category WHERE id = ?";
    //     return jdbc.update(sql,id);
    // }

    public void deactivate(int id) {
        String sql = "UPDATE category SET active = false WHERE id = ?";
        jdbc.update(sql, id);
    }

    public boolean nameAlreadyExists(String name){
        String sql = "SELECT COUNT(*) FROM category WHERE name = ?";
        Integer count = jdbc.queryForObject(sql, Integer.class, name);
        return count != null && count > 0;
    }

    public boolean idAlreadyExists(int id) {
        String sql = "SELECT COUNT(*) FROM category WHERE id = ?";
        Integer count = jdbc.queryForObject(sql, Integer.class, id);
        return count != null && count > 0;
    }

    public Optional<Category> findInactiveByName(String name) {
        String sql = "SELECT * FROM category WHERE name = ? AND active = false";
        List<Category> result = jdbc.query(sql, new BeanPropertyRowMapper<>(Category.class), name);
        return result.stream().findFirst();
    }

    public void reactivate(int id) {
        String sql = "UPDATE category SET active = true WHERE id = ?";
        jdbc.update(sql, id);
    }
    
}
