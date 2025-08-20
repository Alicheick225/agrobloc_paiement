package com.agrobloc.agrobloc_paiement.service;

import com.agrobloc.agrobloc_paiement.dto.TransactionDto;
import com.agrobloc.agrobloc_paiement.enums.StatusTransaction;
import com.agrobloc.agrobloc_paiement.enums.TypeTransaction;
import com.agrobloc.agrobloc_paiement.model.Compte;
import com.agrobloc.agrobloc_paiement.model.Transaction;
import com.agrobloc.agrobloc_paiement.repository.CompteRepository;
import com.agrobloc.agrobloc_paiement.repository.TransactionRepository;
import com.paydunya.neptune.PaydunyaSetup;
import com.paydunya.neptune.PaydunyaCheckoutStore;
import com.paydunya.neptune.PaydunyaCheckoutInvoice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Service
public class PaiementService {

        @Autowired
        private CompteService compteService;

        @Autowired
        private UserWalletService userWalletService;

        @Autowired
        private TransactionService transactionService;

        @Autowired
        private RealPaymentApiService realPaymentApi;// interface vers Moov, Orange, banque...

    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private CompteRepository compteRepository;

    public TransactionDto effectuerPaiement(String numeroCompteAcheteur,
                                            String walletProducteur,
                                            BigDecimal montant) {
            // 1️⃣ Débit compte réel via API externe
            boolean debitOk = realPaymentApi.debiter(numeroCompteAcheteur, montant);
            if(!debitOk) {
                throw new RuntimeException("Débit du compte réel échoué !");
            }

            // 2️⃣ Créditer le compte séquestre global
            Compte compteEscrow = compteService.findEscrowAccount();
            compteEscrow.setSolde(compteEscrow.getSolde().add(montant));
            compteService.save(compteEscrow);

            // 3️⃣ Créer transaction PENDING
            Transaction tx = new Transaction();
            tx.setNumeroTransaction(UUID.randomUUID().toString());
            tx.setCompteSource(compteRepository.findByNumeroCompte(numeroCompteAcheteur).orElse(null));
            tx.setCompteDestination(compteEscrow);
            tx.setMontant(montant);
            tx.setType(TypeTransaction.PAIEMENT.getLibelle());
            tx.setStatut(StatusTransaction.PENDING.getLibelle());
            tx.setDate(Instant.now());
            transactionRepository.save(tx);

            return TransactionDto.fromEntity(tx);
        }

        // Méthode pour valider la livraison
        public void validerLivraison(Transaction tx) {
            tx.setStatut(StatusTransaction.SUCCESS.getLibelle());
            transactionRepository.save(tx);

            // Transférer vers wallet producteur (non utilisable)
            userWalletService.creditWalletEnAttente(tx.getWalletDestination().getId(), tx.getMontant());
        }

        // Méthode pour libérer fonds après délai
        public void libererFonds(Transaction tx) {
            tx.setStatut(StatusTransaction.FREE.getLibelle());
            transactionRepository.save(tx);

            userWalletService.rendreFondsUtilisables(tx.getWalletDestination().getId());
        }
    }

