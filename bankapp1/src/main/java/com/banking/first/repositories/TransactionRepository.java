package com.banking.first.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.banking.first.entities.AppUser;
import com.banking.first.entities.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByUserOrderByDateDesc(AppUser user);
}
