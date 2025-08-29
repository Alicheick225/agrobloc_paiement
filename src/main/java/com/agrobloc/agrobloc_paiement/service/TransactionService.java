package com.agrobloc.agrobloc_paiement.service;

import com.agrobloc.agrobloc_paiement.dto.CommandesVentePaiementDto;
import com.agrobloc.agrobloc_paiement.dto.TransactionDto;
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
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;

@Service
public class TransactionService {

    @Autowired
    private CompteRepository compteRepository;

    @Autowired
    private CompteService compteService;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private UserWalletRepository userWalletRepository;

    @Autowired
    private RealPaymentApiService realPaymentApiService;
    @Autowired
    private UserWalletService userWalletService;

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
        transaction.setDateCreation(Instant.now());
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
        transaction.setDateCreation(Instant.now());
        transactionRepository.save(transaction);

        return compteSource;
    }

    @Transactional
    public Transaction rechargerWallet(UUID walletId, String numeroCompte, BigDecimal montant) {
        // Récupérer le wallet interne
        UserWallet wallet = userWalletRepository.findById(walletId)
                .orElseThrow(() -> new RuntimeException("Wallet utilisateur introuvable"));

        // Récupérer le compte
        Compte compteReel = compteRepository.findByNumeroCompte(numeroCompte)
                .orElseThrow(() -> new RuntimeException("Compte non trouvé"));

        // Débit du compte réel
        boolean debitOk = realPaymentApiService.debiter(compteReel, montant);

        // Crédit du wallet interne si débit réussi
        Transaction transaction = new Transaction();
        transaction.setNumeroTransaction(UUID.randomUUID().toString());
        transaction.setWalletSource(null); // argent vient d'un compte réel
        transaction.setCompteSource(compteReel);
        transaction.setWalletDestination(wallet);
        transaction.setCompteDestination(null);
        transaction.setMoyenPaiement(compteReel.getMoyensPaiement());
        transaction.setMontant(montant);
        transaction.setType(TypeTransaction.RECHARGEMENT.getLibelle());
        transaction.setDateCreation(Instant.now());

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


    @Transactional
    public Transaction retirerDuWallet(UUID walletId, String numeroCompte, BigDecimal montant) {
        // 1️⃣ Récupérer le wallet interne
        UserWallet wallet = userWalletRepository.findById(walletId)
                .orElseThrow(() -> new RuntimeException("Wallet utilisateur introuvable"));

        // 2️⃣ Vérifier le solde disponible
        if (wallet.getSoldeDisponible().compareTo(montant) < 0) {
            throw new RuntimeException("Solde insuffisant pour effectuer le retrait");
        }

        // 3️⃣ Récupérer le compte réel destinataire
        Compte compteReel = compteRepository.findByNumeroCompte(numeroCompte)
                .orElseThrow(() -> new RuntimeException("Compte bancaire destinataire non trouvé"));

        // 4️⃣ Créer une transaction de retrait
        Transaction transaction = new Transaction();
        transaction.setNumeroTransaction(UUID.randomUUID().toString());
        transaction.setWalletSource(wallet);
        transaction.setCompteSource(null);
        transaction.setWalletDestination(null);
        transaction.setCompteDestination(compteReel);
        transaction.setMoyenPaiement(compteReel.getMoyensPaiement());
        transaction.setMontant(montant);
        transaction.setType(TypeTransaction.RETRAIT.getLibelle());
        transaction.setDateCreation(Instant.now());

        // 5️⃣ Débiter le wallet interne
        wallet.setSoldeDisponible(wallet.getSoldeDisponible().subtract(montant));

        // 6️⃣ Créditer le compte réel via API
        boolean creditOk = realPaymentApiService.crediter(compteReel, montant);

        if (creditOk) {
            userWalletRepository.save(wallet);
            transaction.setStatut(StatusTransaction.SUCCESS.getLibelle());
        } else {
            // rollback du débit si le virement échoue
            wallet.setSoldeDisponible(wallet.getSoldeDisponible().add(montant));
            transaction.setStatut(StatusTransaction.FAILED.getLibelle());
        }

        // 7️⃣ Enregistrer la transaction
        transactionRepository.save(transaction);

        return transaction;
    }

    // Méthode pour changer le statut de la transaction et mettre les fonds en attente après validation de la livraison
    public void mettreSoldeEnAttente(Transaction tx) {
        tx.setStatut(StatusTransaction.SUCCESS.getLibelle());
        tx.setDateSuccess(Instant.now());
        transactionRepository.save(tx);

        // Transférer vers wallet producteur (non utilisable)
        userWalletService.creditWalletEnAttente(tx.getWalletDestination(), tx.getMontant());
    }


    // Méthode pour libérer les fonds après délai
    public void libererTransactionsExpirees() {
        //Récupérer la liste des transactions qui ont 30 jours d'ancienneté
        List<Transaction> transactions = transactionRepository.findByStatutAndDateCreationBefore(
                StatusTransaction.SUCCESS.getLibelle(),
                Instant.now().minus(30, ChronoUnit.DAYS)
        );

        for(Transaction tx : transactions) {
            tx.setStatut(StatusTransaction.FREE.getLibelle());
            transactionRepository.save(tx);

            // Mise à jour du wallet
            UserWallet wallet = tx.getWalletDestination();
            wallet.setSoldeEnAttente(wallet.getSoldeEnAttente().subtract(tx.getMontant()));
            wallet.setSoldeDisponible(wallet.getSoldeDisponible().add(tx.getMontant()));
            userWalletRepository.save(wallet);
        }
    }



    public void updateStatut(UUID transactionId, StatusTransaction newStatut) {
        Transaction tx = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new RuntimeException("Transaction introuvable"));
        tx.setStatut(newStatut.getLibelle());
        transactionRepository.save(tx);
    }
}
