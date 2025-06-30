package com.example.mini_market.Service;


import com.example.mini_market.Accessor.ProductAcc;
import com.example.mini_market.Accessor.TransactionAcc;
import com.example.mini_market.Exceptions.NotFoundException;
import com.example.mini_market.Model.Product;
import com.example.mini_market.Model.Transaction;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TransactionServ {

    private final TransactionAcc transAcc;
    private final ProductAcc prodAcc;
    private final ProductServ prodServ;

    public TransactionServ(TransactionAcc transAcc, ProductAcc prodAcc, ProductServ prodServ) {
        this.transAcc = transAcc;
        this.prodAcc = prodAcc;
        this.prodServ = prodServ;
    }

    public List<Transaction> getAllTransactions(){
        return transAcc.findAll();
    }

    public Transaction findTransactionById(int id){
        return transAcc.findById(id)
                   .orElseThrow(() -> new NotFoundException("Transaction", id));
    }

    public List<Transaction> findTransByDateRange(LocalDateTime start, LocalDateTime end){
        return transAcc.findByDateRange(start,end);
    }

    public void fixProductQuantity(Product prod, String type, int delta){
        int newQu = prod.getQuantity();

        if(type.equalsIgnoreCase("IN")){
            newQu += delta;
        } else if (type.equalsIgnoreCase("OUT")) {
            if(delta > newQu){
                throw new IllegalStateException("Not enough stock");
            }
            newQu -= delta;

        }else{
            throw new IllegalArgumentException("Type must be IN or OUT");
        }

        prod.setQuantity(newQu);
        prodAcc.update(prod);
    }

    public void createTransaction(Transaction trans){
        Product prod = prodAcc.findById(trans.getProductId());
        if(prod == null){
            throw  new NotFoundException("Product", trans.getProductId());
        }
        fixProductQuantity(prod,trans.getType(),trans.getQuantity());
        // prodAcc.update(prod);
        trans.setTimestamp(LocalDateTime.now());
        transAcc.add(trans);
    }

    public void updateTransaction(int id, Transaction transNew){
        Transaction transOld = findTransactionById(id);
        if(transOld == null){
            throw new NotFoundException("Transaction", id);
        }
        Product prod = prodAcc.findById(transNew.getProductId());
        if(prod == null){
            throw new NotFoundException("Product", transNew.getProductId());
        }
        fixProductQuantity(prod,transOld.getType(),-transOld.getQuantity());
        fixProductQuantity(prod,transNew.getType(),transNew.getQuantity());

        transNew.setId(id);
        transNew.setTimestamp(LocalDateTime.now());
        transAcc.update(transNew);
    }

    // public void deleteTransaction(int id){
    //     Transaction trans = findTransactionById(id);
    //     if(trans == null){
    //         throw new NotFoundException("Transaction", id);
    //     }
    //     Product prod = prodAcc.findById(trans.getProductId());
    //     if(prod == null) {
    //         throw new NotFoundException("Product", trans.getProductId());
    //     }
    //     fixProductQuantity(prod,trans.getType(),trans.getQuantity());
    //     transAcc.delete(id);
    // }
    
    public void deactivateTransaction(int id) {
        Transaction t = findTransactionById(id);
        Product p = prodServ.getProductById(t.getProductId());

        if (t.getType().equalsIgnoreCase("IN")) {
            p.setQuantity(p.getQuantity() - t.getQuantity());
        } else if (t.getType().equalsIgnoreCase("OUT")) {
            p.setQuantity(p.getQuantity() + t.getQuantity());
        }

        prodServ.updateQuantity(p.getId(), p.getQuantity());
        transAcc.deactivate(id);
    }

    public void reactivateTransaction(int id) {
        Transaction t = findTransactionById(id);
        Product p = prodServ.getProductById(t.getProductId());

        if (t.getType().equalsIgnoreCase("IN")) {
            p.setQuantity(p.getQuantity() + t.getQuantity());
        } else if (t.getType().equalsIgnoreCase("OUT")) {
            if (t.getQuantity() > p.getQuantity()) {
                throw new IllegalStateException("Not enough stock to reactivate this OUT transaction");
            }
            p.setQuantity(p.getQuantity() - t.getQuantity());
        }

        prodServ.updateQuantity(p.getId(), p.getQuantity());
        transAcc.reactivate(id);
    }
}
