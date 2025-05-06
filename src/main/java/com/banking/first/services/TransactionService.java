package com.banking.first.services;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.banking.first.entities.AppUser;
import com.banking.first.entities.Transaction;
import com.banking.first.repositories.TransactionRepository;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

	public void addTransaction(AppUser appUser, String type, double amount) {
		  Transaction transaction = new Transaction();
	        transaction.setUser(appUser);
	        transaction.setType(type);
	        transaction.setAmount(amount);
	        transaction.setDate(LocalDateTime.now());
	        transactionRepository.save(transaction);		
	}

    public List<Transaction> getUserTransactions(AppUser user) {
        return transactionRepository.findByUserOrderByDateDesc(user);
    }

}
