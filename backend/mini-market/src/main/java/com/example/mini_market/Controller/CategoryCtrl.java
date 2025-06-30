package com.example.mini_market.Controller;


import com.example.mini_market.Exceptions.AlreadyExistsException;
import com.example.mini_market.Exceptions.EmptyInputException;
import com.example.mini_market.Exceptions.NotFoundException;
import com.example.mini_market.Model.Category;
import com.example.mini_market.Service.CategoryServ;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/categories")
public class CategoryCtrl {
    private final CategoryServ serv;

    public CategoryCtrl(CategoryServ serv) {
        this.serv = serv;
    }

    @GetMapping
    public List<Category> getAllCategories(){
        return serv.getAllCategories();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Category> getCategoryById (@PathVariable int id){
        Category cat = serv.getCategoryById(id);
        if (cat == null) {
            throw new NotFoundException("Category", id);
        }
        return ResponseEntity.ok(cat);
    }

    @PostMapping
    public ResponseEntity<String> createCategory(@RequestBody Category cat){
        Optional<Category> inactive = serv.findInactiveByName(cat.getName());
        if (inactive.isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("INACTIVE_EXISTS:" + inactive.get().getId());
        }

        
        if (serv.existsByName(cat.getName())) {
            throw new AlreadyExistsException("Category", "name", cat.getName());
        }

        serv.createCategory(cat);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }


    @PutMapping("/{id}")
    public ResponseEntity<String> updateCategory(@PathVariable int id , @RequestBody Category cat ){
        if(cat.getName() == null || cat.getName().isEmpty()){
            throw new EmptyInputException("name");
        }
        cat.setId(id);
        serv.updateCategory(cat);
        return ResponseEntity.ok("Category updated successfully.");
    }

    // @DeleteMapping("/{id}")
    // public ResponseEntity<String> deleteCategory(@PathVariable int id){
    //     Category exist = serv.getCategoryById(id);
    //     if(exist == null){
    //         throw new NotFoundException("Category",id);
    //     }
    //     serv.deleteCategory(id);
    //     return ResponseEntity.ok("Category deleted successfully");
    // }

    @PutMapping("/{id}/deactivate")
    public ResponseEntity<?> deactivateCategory(@PathVariable int id) {
        serv.deactivateCategory(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/reactivate")
    public ResponseEntity<?> reactivateCategory(@PathVariable int id) {
        serv.reactivateCategory(id);
        return ResponseEntity.ok().build();
    }
}
