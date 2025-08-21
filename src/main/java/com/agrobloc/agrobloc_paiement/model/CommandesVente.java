package com.agrobloc.agrobloc_paiement.model;

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
@Table(name = "commandes_vente")
public class CommandesVente {
    @Id
    @ColumnDefault("uuid_generate_v4()")
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column(name = "quantite", precision = 10, scale = 2)
    private BigDecimal quantite;

    @Column(name = "prix_total", precision = 12, scale = 2)
    private BigDecimal prixTotal;

    @ColumnDefault("now()")
    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "livraison_deadline")
    private Instant livraisonDeadline;

    @Column(name = "reception_deadline")
    private Instant receptionDeadline;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "annonces_vente_id")
    private AnnoncesVente annoncesVente;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "acheteur_id")
    private User acheteur;

    @ManyToOne(fetch = FetchType.LAZY)
    @ColumnDefault("auth.uid()")
    @JoinColumn(name = "types_paiement_id")
    private TypesPaiement typesPaiement;

    @OneToOne(mappedBy = "commandesVente", cascade = CascadeType.ALL, fetch = FetchType.LAZY, optional = true)
    private CommandesVentePaiement paiement;

    @JoinColumn(name = "statut")
    private String statut;

/*
 TODO [Reverse Engineering] create field to map the 'statut' column
 Available actions: Define target Java type | Uncomment as is | Remove column mapping
    @Column(name = "statut", columnDefinition = "statut_commande")
    private Object statut;
*/
}