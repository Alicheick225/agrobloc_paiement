package com.agrobloc.agrobloc_paiement.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.GenericGenerator;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "compte")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Compte {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;

    @Column(name = "numero_compte", length = 50)
    private String numeroCompte;

    public void setSolde(BigDecimal solde) {
        this.solde = solde;
    }

    public void setTypeCompte(TypeCompte typeCompte) {
        this.typeCompte = typeCompte;
    }

    @ColumnDefault("0.00")
    @Column(name = "solde", nullable = false, precision = 12, scale = 2)
    private BigDecimal solde;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "moyens_paiement_id", nullable = false)
    private MoyensPaiement moyensPaiement;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public enum TypeCompte {
        BUYER,
        MERCHANT,
        ESCROW
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "type_compte", nullable = false)
    private TypeCompte typeCompte;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getNumeroCompte() {
        return numeroCompte;
    }

    public void setNumeroCompte(String numeroCompte) {
        this.numeroCompte = numeroCompte;
    }

    public BigDecimal getSolde() {
        return solde;
    }

    public void setMontant(BigDecimal montant) {
        this.solde = montant;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public MoyensPaiement getMoyensPaiement() {
        return moyensPaiement;
    }

    public void setMoyensPaiement(MoyensPaiement moyensPaiement) {
        this.moyensPaiement = moyensPaiement;
    }

}