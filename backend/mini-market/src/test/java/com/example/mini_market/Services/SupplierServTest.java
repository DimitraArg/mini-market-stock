package com.example.mini_market.Services;


import com.example.mini_market.Accessor.SupplierAcc;
import com.example.mini_market.Model.Supplier;
import com.example.mini_market.Service.SupplierServ;
import com.example.mini_market.Exceptions.NotFoundException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class SupplierServTest {

    private SupplierAcc supplierAcc;
    private SupplierServ supplierServ;

    @BeforeEach
    public void setUp() {
        supplierAcc = mock(SupplierAcc.class);
        supplierServ = new SupplierServ(supplierAcc);
    }

    @Test
    public void testGetAllSuppliers() {
        when(supplierAcc.findAll()).thenReturn(List.of(new Supplier(), new Supplier()));
        assertEquals(2, supplierServ.getAllSuppliers().size());
    }

    @Test
    public void testGetSupplierById_Found() {
        Supplier s = new Supplier();
        s.setId(1);
        when(supplierAcc.findById(1)).thenReturn(s);
        assertEquals(1, supplierServ.getSupplierById(1).getId());
    }

    @Test
    public void testGetSupplierById_NotFound() {
        when(supplierAcc.findById(999)).thenReturn(null);
        assertThrows(NotFoundException.class, () -> supplierServ.getSupplierById(999));
    }

    @Test
    public void testAddSupplier() {
        Supplier s = new Supplier();
        supplierServ.createSupplier(s);
        verify(supplierAcc).add(s);
    }

    @Test
    public void testUpdateSupplier() {
        Supplier s = new Supplier();
        s.setId(5);
        supplierServ.updateSupplier(s);
        assertEquals(5, s.getId());
        verify(supplierAcc).update(s);
    }

    // @Test
    // public void testDeleteSupplier() {
    //     supplierServ.deleteSupplier(3);
    //     verify(supplierAcc).delete(3);
    // }

    @Test
    public void testDeactivateReactivate() {
        supplierServ.deactivateSupplier(2);
        verify(supplierAcc).deactivate(2);

        supplierServ.reactivateSupplier(2);
        verify(supplierAcc).reactivate(2);
    }
}

