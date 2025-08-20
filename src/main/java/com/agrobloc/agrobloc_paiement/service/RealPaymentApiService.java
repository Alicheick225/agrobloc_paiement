package com.agrobloc.agrobloc_paiement.service;


import com.agrobloc.agrobloc_paiement.model.Compte;
import com.agrobloc.agrobloc_paiement.repository.CompteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class RealPaymentApiService {

    @Autowired
    private final CompteRepository compteRepository;

    public RealPaymentApiService(CompteRepository compteRepository) {
        this.compteRepository = compteRepository;
    }

    @Transactional
    public boolean debiter(String numeroCompte, BigDecimal montant) {
        Optional<Compte> compteOpt = compteRepository.findByNumeroCompte(numeroCompte);

        if (compteOpt.isPresent()) {
            Compte compte = compteOpt.get();

            if (compte.getSolde().compareTo(montant) >= 0) {
                compte.setSolde(compte.getSolde().subtract(montant));
                compteRepository.save(compte);
                return true;
            } else {
                return false; // solde insuffisant
            }
        }
        return false; // compte introuvable
    }

    @Transactional
    public boolean crediter(String numeroCompte, BigDecimal montant) {
        Optional<Compte> compteOpt = compteRepository.findByNumeroCompte(numeroCompte);

        if (compteOpt.isPresent()) {
            Compte compte = compteOpt.get();
            compte.setSolde(compte.getSolde().add(montant));
            compteRepository.save(compte);
            return true;
        }
        return false; // compte introuvable
    }
}


