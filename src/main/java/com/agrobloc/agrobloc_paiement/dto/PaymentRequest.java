package com.agrobloc.agrobloc_paiement.dto;

import com.agrobloc.agrobloc_paiement.model.UserWallet;

import java.math.BigDecimal;
import java.util.UUID;

public class PaymentRequest {
    public String getNumeroCompteAcheteur() {
        return numeroCompteAcheteur;
    }

    public void setNumeroCompteAcheteur(String numeroCompteAcheteur) {
        this.numeroCompteAcheteur = numeroCompteAcheteur;
    }

    public UserWallet getWalletProducteur() {
        return walletProducteur;
    }

    public void setWalletProducteur(UserWallet walletProducteur) {
        this.walletProducteur = walletProducteur;
    }

    public BigDecimal getMontant() {
        return montant;
    }

    public void setMontant(BigDecimal montant) {
        this.montant = montant;
    }

    private String numeroCompteAcheteur; // compte réel
    private UserWallet walletProducteur;     // wallet interne du producteur
    private BigDecimal montant;
    private UUID commandeId;

    public UUID getCommandeId() {
        return commandeId;
    }

    public void setCommandeId(UUID commandeId) {
        this.commandeId = commandeId;
    }


}




