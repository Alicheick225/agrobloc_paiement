package com.agrobloc.agrobloc_paiement.job;

import com.agrobloc.agrobloc_paiement.enums.StatusTransaction;
import com.agrobloc.agrobloc_paiement.model.Transaction;
import com.agrobloc.agrobloc_paiement.repository.TransactionRepository;
import com.agrobloc.agrobloc_paiement.service.TransactionService;
import com.agrobloc.agrobloc_paiement.service.UserWalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;

@Component
public class SoldeEnAttenteJob {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private UserWalletService userWalletService;

    @Autowired
    private TransactionService transactionService;

    // Exemple : toutes les 5 minutes
    @Scheduled(fixedRate = 5 * 60 * 1000)
    public void mettreSoldeEnAttenteJob() {
        // Récupérer toutes les transactions confirmées mais pas encore passées à SUCCESS
        List<Transaction> transactions = transactionRepository
                .findByStatut(StatusTransaction.PENDING.getLibelle()); // ou autre filtre selon ta logique

        for (Transaction tx : transactions) {
            if (livraisonConfirmee(tx)) { // méthode pour vérifier si livraison validée
                transactionService.mettreSoldeEnAttente(tx);
            }
        }
    }

    private boolean livraisonConfirmee(Transaction tx) {
        // TODO : ajouter ta logique de confirmation (via API, webhook, ou champ dans la commande)
        return true;
    }
}

