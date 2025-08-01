package com.agrobloc.agrobloc_paiement.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
@Table(name = "users")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class User {
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    @Id
    @ColumnDefault("uuid_generate_v4()")
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column(name = "nom", length = 100)
    private String nom;

    @Column(name = "email")
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "numero_tel", length = 20)
    private String numeroTel;

    @Column(name = "adresse", length = Integer.MAX_VALUE)
    private String adresse;

    @Column(name = "numero_cni", length = 50)
    private String numeroCni;

    @Column(name = "numero_carte_planteur", length = 50)
    private String numeroCartePlanteur;

    @Column(name = "numero_rccm", length = 50)
    private String numeroRccm;

    @Column(name = "photo_carte_cni", length = Integer.MAX_VALUE)
    private String photoCarteCni;

    @Column(name = "photo_planteur", length = Integer.MAX_VALUE)
    private String photoPlanteur;

    @Column(name = "photo_carte_planteur", length = Integer.MAX_VALUE)
    private String photoCartePlanteur;

    @ColumnDefault("false")
    @Column(name = "certifie_bio")
    private Boolean certifieBio;

    @Column(name = "wallet_adress")
    private String walletAdress;

    @ColumnDefault("false")
    @Column(name = "is_profile_completed")
    private Boolean isProfileCompleted;

    @ColumnDefault("'valide'")
    @Column(name = "statut", length = 20)
    private String statut;

    @ColumnDefault("now()")
    @Column(name = "created_at")
    private Instant createdAt;

    @ColumnDefault("now()")
    @Column(name = "updated_at")
    private Instant updatedAt;

}