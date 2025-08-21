package com.agrobloc.agrobloc_paiement.repository;

import com.agrobloc.agrobloc_paiement.model.CommandesVente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CommandesVenteRepository extends JpaRepository<CommandesVente, UUID> {
}
