package com.agrobloc.agrobloc_paiement.enums;

public enum StatusCommandesVente {
        EN_ATTENTE_PAIEMENT("En attente de paiement"),
        EN_ATTENTE_LIVRAISON("En attente de livraison"),
        EN_ATTENTE_RECEPTION("En attente de réception"),
        ANNULEE("Annulée"),
        TERMINEE("Terminée");

        private final String libelle;

    StatusCommandesVente(String libelle) {
            this.libelle = libelle;
        }

        public String getLibelle() {
            return libelle;
        }

        @Override
        public String toString() {
            return libelle;
        }
    }

