package com.agrobloc.agrobloc_paiement.dto;

import com.agrobloc.agrobloc_paiement.model.Transaction;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public class TransactionDto {
    private UUID id;
    private String numeroTransaction;
    private UUID walletSourceId;
    private UUID walletDestinationId;
    private UUID compteSourceId;
    private UUID compteDestinationId;
    private BigDecimal montant;
    private String type;
    private String statut;
    private Instant dateCreation;

    // --- Constructeurs ---
    public TransactionDto() {}

    public TransactionDto(UUID id, String numeroTransaction, UUID walletSourceId,
                          UUID walletDestinationId, UUID compteSourceId,
                          UUID compteDestinationId, BigDecimal montant,
                          String type, String statut, Instant dateCreation) {
        this.id = id;
        this.numeroTransaction = numeroTransaction;
        this.walletSourceId = walletSourceId;
        this.walletDestinationId = walletDestinationId;
        this.compteSourceId = compteSourceId;
        this.compteDestinationId = compteDestinationId;
        this.montant = montant;
        this.type = type;
        this.statut = statut;
        this.dateCreation = dateCreation;
    }

    // --- Getters & Setters ---
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public String getNumeroTransaction() { return numeroTransaction; }
    public void setNumeroTransaction(String numeroTransaction) { this.numeroTransaction = numeroTransaction; }

    public UUID getWalletSourceId() { return walletSourceId; }
    public void setWalletSourceId(UUID walletSourceId) { this.walletSourceId = walletSourceId; }

    public UUID getWalletDestinationId() { return walletDestinationId; }
    public void setWalletDestinationId(UUID walletDestinationId) { this.walletDestinationId = walletDestinationId; }

    public UUID getCompteSourceId() { return compteSourceId; }
    public void setCompteSourceId(UUID compteSourceId) { this.compteSourceId = compteSourceId; }

    public UUID getCompteDestinationId() { return compteDestinationId; }
    public void setCompteDestinationId(UUID compteDestinationId) { this.compteDestinationId = compteDestinationId; }

    public BigDecimal getMontant() { return montant; }
    public void setMontant(BigDecimal montant) { this.montant = montant; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getStatut() { return statut; }
    public void setStatut(String statut) { this.statut = statut; }

    public Instant getDateCreation() { return dateCreation; }
    public void setDateCreation(Instant dateCreation) { this.dateCreation = dateCreation; }

    // --- Conversion Entity -> DTO ---
    public static TransactionDto fromEntity(Transaction tx) {
        return new TransactionDto(
                tx.getId(),
                tx.getNumeroTransaction(),
                tx.getWalletSource() != null ? tx.getWalletSource().getId() : null,
                tx.getWalletDestination() != null ? tx.getWalletDestination().getId() : null,
                tx.getCompteSource() != null ? tx.getCompteSource().getId() : null,
                tx.getCompteDestination() != null ? tx.getCompteDestination().getId() : null,
                tx.getMontant(),
                tx.getType().toString(),
                tx.getStatut().toString(),
                tx.getDate()
        );
    }
}

