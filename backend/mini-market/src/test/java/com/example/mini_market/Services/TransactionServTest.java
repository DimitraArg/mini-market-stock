package com.example.mini_market.Services;


import com.example.mini_market.Accessor.ProductAcc;
import com.example.mini_market.Accessor.TransactionAcc;
import com.example.mini_market.Model.Product;
import com.example.mini_market.Model.Transaction;
import com.example.mini_market.Service.ProductServ;
import com.example.mini_market.Service.TransactionServ;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TransactionServTest {

    private TransactionAcc transactionAcc;
    private ProductAcc productAcc;
    private ProductServ productServ;
    private TransactionServ transactionServ;

    @BeforeEach
    public void setup() {
        transactionAcc = mock(TransactionAcc.class);
        productAcc = mock(ProductAcc.class);
        productServ = mock(ProductServ.class);
        transactionServ = new TransactionServ(transactionAcc, productAcc, productServ);
    }

    @Test
    public void testCreateTransaction_IN() {
        Product mockProduct = new Product();
        mockProduct.setId(1);
        mockProduct.setQuantity(10);

        Transaction transaction = new Transaction();
        transaction.setProductId(1);
        transaction.setSupplierId(2);
        transaction.setType("IN");
        transaction.setQuantity(5);

        when(productAcc.findById(1)).thenReturn(mockProduct);

        transactionServ.createTransaction(transaction);

        assertEquals(15, mockProduct.getQuantity());
        verify(productAcc).update(mockProduct);
        verify(transactionAcc).add(any(Transaction.class));
    }

    @Test
    public void testCreateTransaction_OUT_ThrowsIfNotEnoughStock() {
        Product mockProduct = new Product();
        mockProduct.setId(1);
        mockProduct.setQuantity(3);

        Transaction transaction = new Transaction();
        transaction.setProductId(1);
        transaction.setSupplierId(2);
        transaction.setType("OUT");
        transaction.setQuantity(5);

        when(productAcc.findById(1)).thenReturn(mockProduct);

        Exception ex = assertThrows(IllegalStateException.class, () -> {
            transactionServ.createTransaction(transaction);
        });

        assertEquals("Not enough stock", ex.getMessage());
        verify(transactionAcc, never()).add(any());
    }

    @Test
    public void testUpdateTransaction_AdjustsQuantities() {
        Transaction oldTransaction = new Transaction();
        oldTransaction.setId(1);
        oldTransaction.setProductId(1);
        oldTransaction.setType("IN");
        oldTransaction.setQuantity(5);

        Transaction newTransaction = new Transaction();
        newTransaction.setProductId(1);
        newTransaction.setType("OUT");
        newTransaction.setQuantity(3);

        Product product = new Product();
        product.setId(1);
        product.setQuantity(10);

        when(transactionAcc.findById(1)).thenReturn(Optional.of(oldTransaction));
        when(productAcc.findById(1)).thenReturn(product);

        transactionServ.updateTransaction(1, newTransaction);

        // IN 5 OUT 3 => new quantity = 2
        assertEquals(2, product.getQuantity());
        verify(productAcc, times(2)).update(product);
        verify(transactionAcc).update(any());
    }

    @Test
    public void testDeactivateTransaction_UpdatesProductQuantity() {
        Transaction t = new Transaction();
        t.setId(1);
        t.setProductId(1);
        t.setType("IN");
        t.setQuantity(5);

        Product p = new Product();
        p.setId(1);
        p.setQuantity(10);

        when(transactionAcc.findById(1)).thenReturn(Optional.of(t));
        when(productServ.getProductById(1)).thenReturn(p);

        transactionServ.deactivateTransaction(1);

        assertEquals(5, p.getQuantity());
        verify(productServ).updateQuantity(1, 5);
        verify(transactionAcc).deactivate(1);
    }

    @Test
    public void testReactivateTransaction_OUT_NotEnoughStock() {
        Transaction t = new Transaction();
        t.setId(1);
        t.setProductId(1);
        t.setType("OUT");
        t.setQuantity(10);

        Product p = new Product();
        p.setId(1);
        p.setQuantity(5);

        when(transactionAcc.findById(1)).thenReturn(Optional.of(t));
        when(productServ.getProductById(1)).thenReturn(p);

        Exception ex = assertThrows(IllegalStateException.class, () -> {
            transactionServ.reactivateTransaction(1);
        });

        assertEquals("Not enough stock to reactivate this OUT transaction", ex.getMessage());
        verify(transactionAcc, never()).reactivate(1);
    }
}

