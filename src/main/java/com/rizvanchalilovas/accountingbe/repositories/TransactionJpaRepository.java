package com.rizvanchalilovas.accountingbe.repositories;

import com.rizvanchalilovas.accountingbe.models.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionJpaRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findTransactionsByCategoryId(Long id);
}
