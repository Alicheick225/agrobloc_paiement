package com.agrobloc.agrobloc_paiement.service;

import com.agrobloc.agrobloc_paiement.model.UserWallet;
import com.agrobloc.agrobloc_paiement.repository.UserWalletRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;

@Service
public class UserWalletService {

        @Autowired
        private UserWalletRepository walletRepository;

        /**
         * Créditer un wallet en "attente de libération"
         */
        public void creditWalletEnAttente(UUID walletId, BigDecimal montant) {
            UserWallet wallet = walletRepository.findById(walletId)
                    .orElseThrow(() -> new RuntimeException("Wallet introuvable"));

            wallet.setSoldeEnAttente(wallet.getSoldeEnAttente().add(montant));
            walletRepository.save(wallet);
        }

        /**
         * Libérer les fonds en attente pour les rendre disponibles
         */
        public void rendreFondsUtilisables(UUID walletId) {
            UserWallet wallet = walletRepository.findById(walletId)
                    .orElseThrow(() -> new RuntimeException("Wallet introuvable"));

            wallet.setSoldeDisponible(wallet.getSoldeDisponible().add(wallet.getSoldeEnAttente()));
            wallet.setSoldeEnAttente(BigDecimal.ZERO);
            walletRepository.save(wallet);
        }

        /**
         * Débiter directement les fonds disponibles
         */
        public void debiterWallet(UUID walletId, BigDecimal montant) {
            UserWallet wallet = walletRepository.findById(walletId)
                    .orElseThrow(() -> new RuntimeException("Wallet introuvable"));

            if (wallet.getSoldeDisponible().compareTo(montant) < 0) {
                throw new RuntimeException("Solde insuffisant");
            }

            wallet.setSoldeDisponible(wallet.getSoldeDisponible().subtract(montant));
            walletRepository.save(wallet);
        }
    }

