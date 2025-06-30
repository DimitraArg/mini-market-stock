package com.example.mini_market.Service;

import com.example.mini_market.Accessor.CategoryAcc;
import com.example.mini_market.Exceptions.AlreadyExistsException;
import com.example.mini_market.Exceptions.NotFoundException;
import com.example.mini_market.Model.Category;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryServ {
    private final CategoryAcc catAcc;

    public CategoryServ(CategoryAcc catAcc) {
        this.catAcc = catAcc;
    }

    public List<Category> getAllCategories(){
        return catAcc.findAll();
    }

    public  Category getCategoryById(int id){
        try {
            return catAcc.findById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException("Category", id);
        }
    }

    public boolean existsByName(String name) {
        return catAcc.nameAlreadyExists(name);
    }

    public void createCategory(Category cat){
        if (existsByName(cat.getName())) {
            throw new AlreadyExistsException("Category", "name", cat.getName());
        }
        catAcc.add(cat);
    }

    public void updateCategory(Category cat){
        catAcc.update(cat);
    }

    // public void deleteCategory(int id){
    //     catAcc.delete(id);
    // }
    
    public void deactivateCategory(int id) {
        catAcc.deactivate(id);
    }

    public void reactivateCategory(int id) {
        catAcc.reactivate(id);
    }

    public Optional<Category> findInactiveByName(String name) {
        return catAcc.findInactiveByName(name);
    }

}
