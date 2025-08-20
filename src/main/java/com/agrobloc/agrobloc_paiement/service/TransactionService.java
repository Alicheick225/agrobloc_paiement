package com.agrobloc.agrobloc_paiement.service;

import com.agrobloc.agrobloc_paiement.enums.StatusTransaction;
import com.agrobloc.agrobloc_paiement.enums.TypeTransaction;
import com.agrobloc.agrobloc_paiement.model.Compte;
import com.agrobloc.agrobloc_paiement.model.Transaction;
import com.agrobloc.agrobloc_paiement.model.UserWallet;
import com.agrobloc.agrobloc_paiement.repository.CompteRepository;
import com.agrobloc.agrobloc_paiement.repository.TransactionRepository;
import com.agrobloc.agrobloc_paiement.repository.UserWalletRepository;
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

    @Autowired
    private UserWalletRepository userWalletRepository;

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
        transaction.setStatut(StatusTransaction.SUCCESS.getLibelle());
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
        transaction.setStatut(StatusTransaction.SUCCESS.getLibelle());
        transaction.setDate(Instant.now());
        transactionRepository.save(transaction);

        return compteSource;
    }

    @Transactional
    public Transaction rechargerWallet(UUID walletId, String numeroCompte, BigDecimal montant) {
        // 1️⃣ Récupérer le wallet interne
        UserWallet wallet = userWalletRepository.findById(walletId)
                .orElseThrow(() -> new RuntimeException("Wallet utilisateur introuvable"));

        // 2️⃣ Récupérer le compte réel
        Compte compteReel = compteRepository.findByNumeroCompte(numeroCompte)
                .orElseThrow(() -> new RuntimeException("Compte réel introuvable"));

        // 3️⃣ Débit du compte réel (ici simulation)
        boolean debitOk = simulerDebitCompteExterne(compteReel, montant);

        // 4️⃣ Crédit du wallet interne si débit réussi
        Transaction transaction = new Transaction();
        transaction.setWalletSource(null); // argent vient d'un compte réel
        transaction.setCompteSource(compteReel);
        transaction.setWalletDestination(wallet);
        transaction.setMontant(montant);
        transaction.setType(TypeTransaction.RECHARGEMENT.getLibelle());
        transaction.setDate(Instant.now());

        if (debitOk) {
            // Crédit du wallet
            wallet.setSoldeEnAttente(wallet.getSoldeEnAttente().add(montant));
            userWalletRepository.save(wallet);

            transaction.setStatut(StatusTransaction.SUCCESS.getLibelle());
        } else {
            transaction.setStatut(StatusTransaction.FAILED.getLibelle());
        }

        // 5️⃣ Enregistrement de la transaction
        transactionRepository.save(transaction);

        return transaction;
    }

    /**
     * Méthode simulant le débit du compte réel.
     * Ici, tu peux remplacer par un vrai appel API mobile money / banque.
     */
    private boolean simulerDebitCompteExterne(Compte compte, BigDecimal montant) {
        // Vérifier solde disponible
        if (compte.getSolde().compareTo(montant) < 0) {
            return false;
        }
        // Débit du compte réel simulé
        compte.setSolde(compte.getSolde().subtract(montant));
        compteRepository.save(compte);
        return true;
    }

    public void updateStatut(UUID transactionId, StatusTransaction newStatut) {
        Transaction tx = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new RuntimeException("Transaction introuvable"));
        tx.setStatut(newStatut.getLibelle());
        transactionRepository.save(tx);
    }

}
