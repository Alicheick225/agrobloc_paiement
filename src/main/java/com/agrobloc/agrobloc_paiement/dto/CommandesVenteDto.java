package com.agrobloc.agrobloc_paiement.dto;

import com.agrobloc.agrobloc_paiement.model.CommandesVente;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public class CommandesVenteDto {
    private UUID id;
    private BigDecimal quantite;
    private BigDecimal prixTotal;
    private Instant createdAt;
    private Instant livraisonDeadline;
    private Instant receptionDeadline;
    private String statut; // on expose le statut sous forme de String

    // --- Méthode utilitaire pour convertir Entity -> DTO
    public static CommandesVenteDto fromEntity(CommandesVente entity) {
        CommandesVenteDto dto = new CommandesVenteDto();
        dto.setId(entity.getId());
        dto.setQuantite(entity.getQuantite());
        dto.setPrixTotal(entity.getPrixTotal());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setLivraisonDeadline(entity.getLivraisonDeadline());
        dto.setReceptionDeadline(entity.getReceptionDeadline());

        // ⚠️ le champ statut est commenté dans ton entity
        // donc ici on le set à null pour l’instant
        dto.setStatut(null);

        return dto;
    }

    // --- Méthode utilitaire pour convertir DTO -> Entity
    public CommandesVente toEntity() {
        CommandesVente entity = new CommandesVente();
        entity.setId(this.id);
        entity.setQuantite(this.quantite);
        entity.setPrixTotal(this.prixTotal);
        entity.setCreatedAt(this.createdAt);
        entity.setLivraisonDeadline(this.livraisonDeadline);
        entity.setReceptionDeadline(this.receptionDeadline);

        // idem pour statut, si tu le rajoutes en enum par exemple
        return entity;
    }

    // --- Getters / Setters
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public BigDecimal getQuantite() { return quantite; }
    public void setQuantite(BigDecimal quantite) { this.quantite = quantite; }

    public BigDecimal getPrixTotal() { return prixTotal; }
    public void setPrixTotal(BigDecimal prixTotal) { this.prixTotal = prixTotal; }

    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }

    public Instant getLivraisonDeadline() { return livraisonDeadline; }
    public void setLivraisonDeadline(Instant livraisonDeadline) { this.livraisonDeadline = livraisonDeadline; }

    public Instant getReceptionDeadline() { return receptionDeadline; }
    public void setReceptionDeadline(Instant receptionDeadline) { this.receptionDeadline = receptionDeadline; }

    public String getStatut() { return statut; }
    public void setStatut(String statut) { this.statut = statut; }
}
