package com.agrobloc.agrobloc_paiement.enums;

public enum StatusTransaction {
    PENDING("En attente"),
    SUCCESS("Succès"),
    FAILED("Échec"),
    FREE("Libéré");

    private final String libelle;

    // Constructeur
    StatusTransaction(String libelle) {
        this.libelle = libelle;
    }

    // Getter
    public String getLibelle() {
        return libelle;
    }
}
