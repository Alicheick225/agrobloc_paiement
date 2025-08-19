package com.agrobloc.agrobloc_paiement.repository;

import com.agrobloc.agrobloc_paiement.model.MoyensPaiement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface MoyensPaiementRepository extends JpaRepository<MoyensPaiement, UUID> {
    Optional<MoyensPaiement> findByLibelle(String libelle);
}
