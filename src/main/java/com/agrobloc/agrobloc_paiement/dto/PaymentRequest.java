package com.agrobloc.agrobloc_paiement.dto;

import java.math.BigDecimal;

public class PaymentRequest {
    public String getNumeroCompteAcheteur() {
        return numeroCompteAcheteur;
    }

    public void setNumeroCompteAcheteur(String numeroCompteAcheteur) {
        this.numeroCompteAcheteur = numeroCompteAcheteur;
    }

    public String getWalletProducteur() {
        return walletProducteur;
    }

    public void setWalletProducteur(String walletProducteur) {
        this.walletProducteur = walletProducteur;
    }

    public BigDecimal getMontant() {
        return montant;
    }

    public void setMontant(BigDecimal montant) {
        this.montant = montant;
    }

    private String numeroCompteAcheteur; // compte réel
    private String walletProducteur;     // wallet interne du producteur
    private BigDecimal montant;



}




