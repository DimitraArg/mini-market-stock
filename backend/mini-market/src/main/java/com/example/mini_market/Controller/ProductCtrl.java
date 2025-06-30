package com.example.mini_market.Controller;

import com.example.mini_market.DataTransfer.ProductDTO;
import com.example.mini_market.Exceptions.AlreadyExistsException;
import com.example.mini_market.Exceptions.NotFoundException;
import com.example.mini_market.Model.Category;
import com.example.mini_market.Model.Product;
import com.example.mini_market.Service.CategoryServ;
import com.example.mini_market.Service.ProductServ;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/products")
public class ProductCtrl {

    private final ProductServ prodServ;
    private final CategoryServ catServ;

    public ProductCtrl(ProductServ prodServ, CategoryServ catServ) {
        this.prodServ = prodServ;
        this.catServ = catServ;
    }

    @GetMapping
    public ResponseEntity<List<ProductDTO>> getAllProducts() {
        List<ProductDTO> dtos = prodServ.getAllProducts().stream().map(p -> {
            Category c = catServ.getCategoryById(p.getCategoryId());
            return toDTO(p, c);
        }).collect(Collectors.toList());

        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> getProductById(@PathVariable int id) {
        Product p = prodServ.getProductById(id);
        if (p == null) throw new NotFoundException("Product", id);

        Category c = catServ.getCategoryById(p.getCategoryId());
        return ResponseEntity.ok(toDTO(p, c));
    }

    @GetMapping("/barcode/{code}")
    public ResponseEntity<ProductDTO> getByBarcode(@PathVariable String code) {
        Optional<Product> optionalProduct = prodServ.getByBarcode(code);
        if (optionalProduct.isEmpty()) {
            throw new NotFoundException("Product", code);
        }
        Product p = optionalProduct.get();
        Category c = catServ.getCategoryById(p.getCategoryId());
        return ResponseEntity.ok(toDTO(p, c));
    }

    @PostMapping
    public ResponseEntity<String> createProduct(@RequestBody Product p) {
        Optional<Product> inactive = prodServ.findInactiveByBarcode(p.getBarcode());
        if (inactive.isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("INACTIVE_EXISTS:" + inactive.get().getId());
        }

        if (prodServ.getByBarcode(p.getBarcode()).isPresent()) {
            throw new AlreadyExistsException("Product", "barcode", p.getName());
        }

        prodServ.createProduct(p);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateProduct(@PathVariable int id, @RequestBody Product p) {
        if (prodServ.getProductById(id) == null) {
            throw new NotFoundException("Product", id);
        }
        p.setId(id);
        prodServ.updateProduct(p);
        return ResponseEntity.ok("Product updated successfully.");
    }

    // @DeleteMapping("/{id}")
    // public ResponseEntity<String> deleteProduct(@PathVariable int id) {
    //     if (prodServ.getProductById(id) == null) {
    //         throw new NotFoundException("Product", id);
    //     }
    //     prodServ.deleteProduct(id);
    //     return ResponseEntity.ok("Product deleted successfully.");
    // }

    @PutMapping("/{id}/deactivate")
    public ResponseEntity<?> deactivateProduct(@PathVariable int id){
        prodServ.deActivate(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/reactivate")
    public ResponseEntity<?> reactivateProduct(@PathVariable int id) {
        prodServ.reactivateProduct(id);
        return ResponseEntity.ok().build();
    }


    private ProductDTO toDTO(Product p, Category c) {
        return new ProductDTO(
                p.getId(),
                p.getName(),
                p.getQuantity(),
                p.getPrice().doubleValue(),
                p.getVat().doubleValue(),
                p.getBarcode(),
                new ProductDTO.SimpleEntity(c.getId(), c.getName())
        );
    }
}
