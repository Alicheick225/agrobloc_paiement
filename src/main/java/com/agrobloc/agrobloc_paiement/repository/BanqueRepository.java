package com.agrobloc.agrobloc_paiement.repository;

import com.agrobloc.agrobloc_paiement.model.Banque;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface BanqueRepository extends JpaRepository<Banque, UUID> {
}
