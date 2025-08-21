package com.agrobloc.agrobloc_paiement.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "annonces_vente")
public class AnnoncesVente {
    @Id
    @ColumnDefault("uuid_generate_v4()")
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column(name = "photo", length = Integer.MAX_VALUE)
    private String photo;

    @Column(name = "statut", length = 20)
    private String statut;

    @Column(name = "quantite")
    private Float quantite;

    @Column(name = "prix_kg")
    private Float prixKg;

    @Column(name = "description", length = Integer.MAX_VALUE)
    private String description;

    @ColumnDefault("now()")
    @Column(name = "created_at")
    private Instant createdAt;

}