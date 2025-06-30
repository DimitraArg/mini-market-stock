package com.example.mini_market.Service;

import com.example.mini_market.Accessor.SupplierAcc;
import com.example.mini_market.Exceptions.AlreadyExistsException;
import com.example.mini_market.Exceptions.NotFoundException;
import com.example.mini_market.Model.Supplier;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SupplierServ {

    private final SupplierAcc supAcc;

    public SupplierServ(SupplierAcc supAcc) {
        this.supAcc = supAcc;
    }

    public List<Supplier> getAllSuppliers(){
        return supAcc.findAll();
    }

    public  Supplier getSupplierById(int id){
        Supplier s = supAcc.findById(id);
        if (s == null) {
            throw new NotFoundException("Supplier", id);
        }
        return s; 
    }

    public boolean existsByName(String name) {
        return supAcc.nameAlreadyExists(name);
    }

    public void createSupplier(Supplier sup){
        if (existsByName(sup.getName())) {
            throw new AlreadyExistsException("Supplier", "name", sup.getName());
        }
        supAcc.add(sup);
    }

    public void updateSupplier(Supplier sup){
        supAcc.update(sup);
    }

    // public void deleteSupplier(int id){
    //     supAcc.delete(id);
    // }
    
    public Optional<Supplier> findInactiveByName(String name) {
        return supAcc.findInactiveByName(name);
    }

    public void deactivateSupplier(int id) {
        supAcc.deactivate(id);
    }

    public void reactivateSupplier(int id) {
        supAcc.reactivate(id);
    }
}
