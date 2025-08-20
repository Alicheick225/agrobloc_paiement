package com.agrobloc.agrobloc_paiement.enums;

public enum TypeTransaction {
    RECHARGEMENT("Rechargement de wallet"),
    RETRAIT("Retrait vers compte réel"),
    PAIEMENT("Paiement achat"),
    TRANSFERT("Transfert interne");

    private final String libelle;

    TypeTransaction(String libelle) {
        this.libelle = libelle;
    }

    public String getLibelle() {
        return libelle;
    }
}