package com.agrobloc.agrobloc_paiement.service;

import com.agrobloc.agrobloc_paiement.model.PreferencesPaiement;
import com.agrobloc.agrobloc_paiement.repository.PreferencesPaiementRepository;

import java.util.List;

public class PreferencesPaiementService {
    
    private final PreferencesPaiementRepository preferencesPaiementRepository;

    public PreferencesPaiementService(PreferencesPaiementRepository preferencesPaiementRepository) {
        this.preferencesPaiementRepository = preferencesPaiementRepository;
    }

    public List<PreferencesPaiement> getAll() {
        return preferencesPaiementRepository.findAll();
    }

    public PreferencesPaiement save(PreferencesPaiement preferencesPaiement) {
        return preferencesPaiementRepository.save(preferencesPaiement);
    }
}
