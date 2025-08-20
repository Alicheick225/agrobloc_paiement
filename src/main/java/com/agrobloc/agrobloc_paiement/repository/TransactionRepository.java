package com.agrobloc.agrobloc_paiement.repository;

import com.agrobloc.agrobloc_paiement.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface TransactionRepository extends JpaRepository<Transaction, UUID> {
    Optional<Transaction> findByNumeroTransaction(String numero);

}
