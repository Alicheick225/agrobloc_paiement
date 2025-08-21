package com.agrobloc.agrobloc_paiement.service;


import com.agrobloc.agrobloc_paiement.enums.StatusTransaction;
import com.agrobloc.agrobloc_paiement.model.Compte;
import com.agrobloc.agrobloc_paiement.model.Transaction;
import com.agrobloc.agrobloc_paiement.repository.CompteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

@Service
public class RealPaymentApiService {

    @Autowired
    private final CompteRepository compteRepository;

    public RealPaymentApiService(CompteRepository compteRepository) {
        this.compteRepository = compteRepository;
    }


    /**
     * Méthode simulant le débit du compte réel.
     * Ici, tu peux remplacer par un vrai appel API mobile money / banque.
     */

    @Transactional
    public boolean debiter(Compte compte, BigDecimal montant) {

        if (compte != null) {
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

    /**
     * Méthode simulant le crédit du compte réel.
     * Ici, tu peux remplacer par un vrai appel API mobile money / banque.
     */
    @Transactional
    public boolean crediter(Compte compte, BigDecimal montant) {

        if (compte != null)  {
            compte.setSolde(compte.getSolde().add(montant));
            compteRepository.save(compte);
            return true;
        }
        return false; // compte introuvable
    }

}


