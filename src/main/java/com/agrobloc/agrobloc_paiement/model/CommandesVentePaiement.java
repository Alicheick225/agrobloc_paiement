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
@Table(name = "commandes_vente_paiement")
public class CommandesVentePaiement {
    @Id
    @ColumnDefault("uuid_generate_v4()")
    @Column(name = "id", nullable = false)
    private UUID id;


    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "commandes_vente_id", unique = true)
    private CommandesVente commandesVente;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "moyens_paiement_id")
    private MoyensPaiement moyensPaiement;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "compte_source_id", nullable = false)
    private Compte compteSource;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "compte_destination_id", nullable = false)
    private Compte compteDestination;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "wallet_source_id")
    private UserWallet walletSource;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "wallet_destination_id")
    private UserWallet walletDestination;

    @Column(name = "montant", nullable = false, precision = 12, scale = 2)
    private BigDecimal montant;

    @Column(name = "date_creation", nullable = false)
    private Instant dateCreation;

    @JoinColumn(name = "date_success")
    private Instant dateSuccess;

    @Column(name = "type", length = 20)
    private String type = TypeTransaction.PAIEMENT.getLibelle();

    @Column(name = "status", length = 20, nullable = false)
    private String statut = StatusTransaction.PENDING.getLibelle();

}