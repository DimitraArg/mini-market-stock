package com.example.mini_market.Controller;

import com.example.mini_market.Exceptions.AlreadyExistsException;
import com.example.mini_market.Exceptions.EmptyInputException;
import com.example.mini_market.Exceptions.NotFoundException;
import com.example.mini_market.Model.Supplier;
import com.example.mini_market.Service.SupplierServ;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/suppliers")
public class SupplierCtrl {

    private final SupplierServ serv;

    public SupplierCtrl(SupplierServ serv) {
        this.serv = serv;
    }

    @GetMapping
    public List<Supplier> getAllSuppliers(){
        return serv.getAllSuppliers();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Supplier> getSupplierById (@PathVariable int id){
        Supplier sup = serv.getSupplierById(id);
        if (sup == null) {
            throw new NotFoundException("Supplier", id);
        }
        return ResponseEntity.ok(sup);
    }

    @PostMapping
    public ResponseEntity<String> createSupplier(@RequestBody Supplier sup){
        Optional<Supplier> inactive = serv.findInactiveByName(sup.getName());
        if (inactive.isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("INACTIVE_EXISTS:" + inactive.get().getId());
        }
        if (serv.existsByName(sup.getName())) {
            throw new AlreadyExistsException("Supplier", "name", sup.getName());
        }

        serv.createSupplier(sup);
        return ResponseEntity.ok("Supplier created successfully.");
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<String> updateSupplier(@PathVariable int id , @RequestBody Supplier sup ){
        if(sup.getName() == null || sup.getName().isEmpty()){
            throw new EmptyInputException("name");
        }
        sup.setId(id);
        serv.updateSupplier(sup);
        return ResponseEntity.ok("Supplier updated successfully.");
    }

    // @DeleteMapping("/{id}")
    // public ResponseEntity<String> deleteSupplier(@PathVariable int id){
    //     Supplier exist = serv.getSupplierById(id);
    //     if(exist == null){
    //         throw new NotFoundException("Supplier",id);
    //     }
    //     serv.deleteSupplier(id);
    //     return ResponseEntity.ok("Supplier deleted successfully");
    // }

    @PutMapping("/{id}/deactivate")
    public ResponseEntity<?> deactivateSupplier(@PathVariable int id) {
        try {
            serv.deactivateSupplier(id);
            return ResponseEntity.ok().build();
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                .body("Cannot deactivate supplier: still in use.");
        }
    }

    @PutMapping("/{id}/reactivate")
    public ResponseEntity<?> reactivateSupplier(@PathVariable int id) {
        serv.reactivateSupplier(id);
        return ResponseEntity.ok().build();
    }
}

