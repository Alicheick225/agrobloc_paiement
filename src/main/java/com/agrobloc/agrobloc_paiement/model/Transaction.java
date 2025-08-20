package com.agrobloc.agrobloc_paiement.model;

import com.agrobloc.agrobloc_paiement.enums.StatusTransaction;
import com.agrobloc.agrobloc_paiement.enums.TypeTransaction;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "transactions")
public class Transaction {
    @Id
    @ColumnDefault("uuid_generate_v4()")
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column(name = "numero_transaction", unique = true, nullable = false, length = 100)
    private String numeroTransaction;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "compte_source_id", nullable = false)
    private Compte compteSource;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "compte_destination_id", nullable = false)
    private Compte compteDestination;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "moyen_paiement_id")
    private MoyensPaiement moyenPaiement;

    @Column(name = "montant", nullable = false, precision = 12, scale = 2)
    private BigDecimal montant;

    @Column(name = "date", nullable = false)
    private Instant date = Instant.now();

    @Column(name = "status", length = 20, nullable = false)
    private String statut = StatusTransaction.PENDING.getLibelle();


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "wallet_source_id")
    private UserWallet walletSource;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "wallet_destination_id")
    private UserWallet walletDestination;

    @Column(name = "type", length = 20)
    private String type = TypeTransaction.PAIEMENT.getLibelle();

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getNumeroTransaction() {
        return numeroTransaction;
    }

    public void setNumeroTransaction(String numeroTransaction) {
        this.numeroTransaction = numeroTransaction;
    }

    public Compte getCompteSource() {
        return compteSource;
    }

    public void setCompteSource(Compte compteSource) {
        this.compteSource = compteSource;
    }

    public Compte getCompteDestination() {
        return compteDestination;
    }

    public void setCompteDestination(Compte compteDestination) {
        this.compteDestination = compteDestination;
    }

    public MoyensPaiement getMoyenPaiement() {
        return moyenPaiement;
    }

    public void setMoyenPaiement(MoyensPaiement moyenPaiement) {
        this.moyenPaiement = moyenPaiement;
    }

    public BigDecimal getMontant() {
        return montant;
    }

    public void setMontant(BigDecimal montant) {
        this.montant = montant;
    }

    public Instant getDate() {
        return date;
    }

    public void setDate(Instant date) {
        this.date = date;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

}