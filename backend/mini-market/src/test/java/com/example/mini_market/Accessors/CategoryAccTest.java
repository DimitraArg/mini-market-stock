package com.example.mini_market.Accessors;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;

import com.example.mini_market.Accessor.CategoryAcc;
import com.example.mini_market.Model.Category;


@JdbcTest
@Sql(scripts = {"/schema.sql", "/test-data/categories.sql"})
class CategoryAccTest {
    @Autowired
    private JdbcTemplate jdbc;

    @Test
    void testFindAll() {
        CategoryAcc acc = new CategoryAcc(jdbc);
        List<Category> all = acc.findAll();
        assertEquals(2, all.size());
    }

    @Test
    void testAddAndDeactivate() {
        CategoryAcc acc = new CategoryAcc(jdbc);
    
        Category newCat = new Category(0, "NewCategory", true);
        acc.add(newCat);
        assertEquals(3, acc.findAll().size());

        Category inserted = acc.findAll().stream()
            .filter(c -> c.getName().equals("NewCategory"))
            .findFirst()
            .orElseThrow();

        acc.deactivate(inserted.getId());

        assertEquals(2, acc.findAll().size());
    }
}