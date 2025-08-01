package com.agrobloc.agrobloc_paiement.service;


import com.agrobloc.agrobloc_paiement.model.MoyensPaiement;
import com.agrobloc.agrobloc_paiement.repository.MoyensPaiementRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MoyensPaiementService {

    private final MoyensPaiementRepository moyensPaiementRepository;

    public MoyensPaiementService(MoyensPaiementRepository moyensPaiementRepository) {
        this.moyensPaiementRepository = moyensPaiementRepository;
    }

    public List<MoyensPaiement> getAll() {
        return moyensPaiementRepository.findAll();
    }

    public MoyensPaiement save(MoyensPaiement moyensPaiement) {
        return moyensPaiementRepository.save(moyensPaiement);
    }
}
