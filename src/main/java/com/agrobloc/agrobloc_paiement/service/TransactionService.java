package com.agrobloc.agrobloc_paiement.service;

import com.agrobloc.agrobloc_paiement.model.Compte;
import com.agrobloc.agrobloc_paiement.model.Transaction;
import com.agrobloc.agrobloc_paiement.repository.CompteRepository;
import com.agrobloc.agrobloc_paiement.repository.TransactionRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Service
public class TransactionService {

    @Autowired
    private CompteRepository compteRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    /**
     * Créditer un compte depuis le compte séquestre
     */
    @Transactional
    public Compte creditFromEscrow(String numeroCompteDestination, BigDecimal montant) {
        Compte compteEscrow = compteRepository.findByNumeroCompte("AGROBLOC-ESCROW-0001")
                .orElseThrow(() -> new RuntimeException("Compte séquestre non trouvé"));

        Compte compteDestination = compteRepository.findByNumeroCompte(numeroCompteDestination)
                .orElseThrow(() -> new RuntimeException("Compte destinataire non trouvé"));

        if (compteEscrow.getSolde().compareTo(montant) < 0) {
            throw new RuntimeException("Solde séquestre insuffisant !");
        }

        // Débit séquestre
        compteEscrow.setSolde(compteEscrow.getSolde().subtract(montant));

        // Crédit destinataire
        compteDestination.setSolde(compteDestination.getSolde().add(montant));

        // Sauvegarde comptes
        compteRepository.save(compteEscrow);
        compteRepository.save(compteDestination);

        // Enregistrer transaction
        Transaction transaction = new Transaction();
        transaction.setNumeroTransaction(UUID.randomUUID().toString());
        transaction.setCompteSource(compteEscrow);
        transaction.setCompteDestination(compteDestination);
        transaction.setMontant(montant);
        transaction.setType("CREDIT");
        transaction.setStatut(Transaction.StatusTransaction.SUCCESS);
        transaction.setDate(Instant.now());
        transactionRepository.save(transaction);

        return compteDestination;
    }


    /**
     * Débiter un compte vers le séquestre
     */
    @Transactional
    public Compte debitToEscrow(String numeroCompteSource, BigDecimal montant) {
        Compte compteSource = compteRepository.findByNumeroCompte(numeroCompteSource)
                .orElseThrow(() -> new RuntimeException("Compte source non trouvé"));

        Compte compteEscrow = compteRepository.findByNumeroCompte("AGROBLOC-ESCROW-0001")
                .orElseThrow(() -> new RuntimeException("Compte séquestre non trouvé"));

        if (compteSource.getSolde().compareTo(montant) < 0) {
            throw new RuntimeException("Solde du compte insuffisant !");
        }

        // Débit compte source
        compteSource.setSolde(compteSource.getSolde().subtract(montant));

        // Crédit séquestre
        compteEscrow.setSolde(compteEscrow.getSolde().add(montant));

        // Sauvegarde comptes
        compteRepository.save(compteSource);
        compteRepository.save(compteEscrow);

        // Enregistrer transaction
        Transaction transaction = new Transaction();
        transaction.setNumeroTransaction(UUID.randomUUID().toString());
        transaction.setCompteSource(compteSource);
        transaction.setCompteDestination(compteEscrow);
        transaction.setMontant(montant);
        transaction.setType("DEBIT");
        transaction.setStatut(Transaction.StatusTransaction.SUCCESS);
        transaction.setDate(Instant.now());
        transactionRepository.save(transaction);

        return compteSource;
    }
}
