package com.example.mini_market.Services;

import com.example.mini_market.Accessor.CategoryAcc;
import com.example.mini_market.Model.Category;
import com.example.mini_market.Service.CategoryServ;
import com.example.mini_market.Exceptions.NotFoundException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.dao.EmptyResultDataAccessException;


import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CategoryServTest {

    private CategoryAcc categoryAcc;
    private CategoryServ categoryServ;

    @BeforeEach
    public void setUp() {
        categoryAcc = mock(CategoryAcc.class);
        categoryServ = new CategoryServ(categoryAcc);
    }

    @Test
    public void testGetAllCategories() {
        when(categoryAcc.findAll()).thenReturn(List.of(new Category(), new Category()));
        assertEquals(2, categoryServ.getAllCategories().size());
    }

    @Test
    public void testGetCategoryById_Found() {
        Category c = new Category();
        c.setId(1);
        when(categoryAcc.findById(1)).thenReturn(c);
        assertEquals(1, categoryServ.getCategoryById(1).getId());
    }

    @Test
    public void testGetCategoryById_NotFound() {
        when(categoryAcc.findById(123)).thenThrow(new EmptyResultDataAccessException(1));
        assertThrows(NotFoundException.class, () -> categoryServ.getCategoryById(123));
    }

    @Test
    public void testAddCategory() {
        Category c = new Category();
        categoryServ.createCategory(c);
        verify(categoryAcc).add(c);
    }

    @Test
    public void testUpdateCategory() {
        Category c = new Category();
        c.setId(5);
        c.setName("Updated");
        when(categoryAcc.update(c)).thenReturn(1);
        categoryServ.updateCategory(c);
        verify(categoryAcc, times(1)).update(c);
        assertEquals(5, c.getId()); 

    }

    // @Test
    // public void testDeleteCategory() {
    //     categoryServ.deleteCategory(3);
    //     verify(categoryAcc).delete(3);
    // }

    @Test
    public void testDeactivateReactivate() {
        categoryServ.deactivateCategory(2);
        verify(categoryAcc).deactivate(2);

        categoryServ.reactivateCategory(2);
        verify(categoryAcc).reactivate(2);
    }
}
