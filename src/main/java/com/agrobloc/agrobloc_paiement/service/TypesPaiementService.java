package com.agrobloc.agrobloc_paiement.service;


import com.agrobloc.agrobloc_paiement.model.TypesPaiement;
import com.agrobloc.agrobloc_paiement.repository.TypesPaiementRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TypesPaiementService {

    private final TypesPaiementRepository typesPaiementRepository;

    public TypesPaiementService(TypesPaiementRepository typesPaiementRepository) {
        this.typesPaiementRepository = typesPaiementRepository;
    }

    public List<TypesPaiement> getAll() {
        return typesPaiementRepository.findAll();
    }

    public TypesPaiement save(TypesPaiement typesPaiement) {
        return typesPaiementRepository.save(typesPaiement);
    }
}
