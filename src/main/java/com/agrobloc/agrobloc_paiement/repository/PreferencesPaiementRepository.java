package com.agrobloc.agrobloc_paiement.repository;

import com.agrobloc.agrobloc_paiement.model.MoyensPaiement;
import com.agrobloc.agrobloc_paiement.model.PreferencesPaiement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PreferencesPaiementRepository extends JpaRepository<PreferencesPaiement, UUID> {
}
