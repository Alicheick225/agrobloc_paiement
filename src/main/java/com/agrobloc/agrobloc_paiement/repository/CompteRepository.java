package com.agrobloc.agrobloc_paiement.repository;

import com.agrobloc.agrobloc_paiement.model.Compte;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface CompteRepository extends JpaRepository<Compte, UUID> {
    Optional<Compte> findByNumeroCompte(String numeroCompte);
}
