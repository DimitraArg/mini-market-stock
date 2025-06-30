package com.example.mini_market.Service;

import com.example.mini_market.Accessor.ProductAcc;
import com.example.mini_market.Exceptions.AlreadyExistsException;
import com.example.mini_market.Exceptions.EmptyInputException;
import com.example.mini_market.Exceptions.NotFoundException;
import com.example.mini_market.Model.Product;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class ProductServ {
    private final ProductAcc prodAcc;


    public ProductServ(ProductAcc prodAcc) {
        this.prodAcc = prodAcc;
    }

    public List<Product> getAllProducts() {
        return prodAcc.findAll();
    }

    public Product getProductById(int id) {
        Product p = prodAcc.findById(id);
        if (p == null) {
            throw new NotFoundException("Product", id);
        }
        return p;
    }

    public Optional<Product> getByBarcode(String barcode) {
        return prodAcc.findByBarcode(barcode);
    }

    public boolean existsByName(String name) {
        return prodAcc.nameAlreadyExists(name);
    }


    public void createProduct(Product p) {
        if (existsByName(p.getName())) {
            throw new AlreadyExistsException("Category", "name", p.getName());
        }
        if (p.getBarcode() == null || p.getBarcode().isEmpty()) {
            throw new EmptyInputException("barcode");
        }

        if (prodAcc.barcodeAlreadyExists(p.getBarcode())) {
            throw new AlreadyExistsException("Product", "barcode", p.getBarcode());
        }
        prodAcc.add(p);
    }

    public void updateProduct(Product p) {
        prodAcc.update(p);
    }

    // public void deleteProduct(int id) {
    //     prodAcc.delete(id);
    // }

    public void deActivate(int id){
        prodAcc.deActivate(id);
    }

    public Optional<Product> findInactiveByBarcode(String barcode) {
        return prodAcc.findInactiveByBarcode(barcode);
    }

    public void reactivateProduct(int id) {
        prodAcc.reactivate(id);
    }

    public void updateQuantity(int id, int newQuantity) {
        prodAcc.updateQuantity(id, newQuantity);
    }
}
