package com.agrobloc.agrobloc_paiement.repository;

import com.agrobloc.agrobloc_paiement.model.CommandesVentePaiement;
import com.agrobloc.agrobloc_paiement.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public interface CommandesVentePaiementRepository extends JpaRepository<CommandesVentePaiement, UUID> {

    List<CommandesVentePaiement> findByStatutAndDateSuccessBefore(String statut, Instant date);



}
