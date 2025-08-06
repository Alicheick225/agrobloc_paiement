package com.agrobloc.agrobloc_paiement.repository;

import com.agrobloc.agrobloc_paiement.model.TypesPaiement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TypesPaiementRepository extends JpaRepository<TypesPaiement, UUID> {
}
