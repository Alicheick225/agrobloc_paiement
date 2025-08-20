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
@Table(name = "user_wallets")
public class UserWallet {
    @Id
    @ColumnDefault("uuid_generate_v4()")
    @Column(name = "id", nullable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ColumnDefault("0.00")
    @Column(name = "solde_disponible", precision = 12, scale = 2)
    private BigDecimal soldeDisponible;

    @ColumnDefault("0.00")
    @Column(name = "solde_en_attente", precision = 12, scale = 2)
    private BigDecimal soldeEnAttente;

    @ColumnDefault("now()")
    @Column(name = "created_at")
    private Instant createdAt;

    @ColumnDefault("now()")
    @Column(name = "updated_at")
    private Instant updatedAt;

}