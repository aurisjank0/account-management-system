package com.aurimasjank.demo.repository;

import com.aurimasjank.demo.model.Account;
import com.aurimasjank.demo.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findTop10ByAccountOrderByTimestampDesc(Account account);
}