package com.agrobloc.agrobloc_paiement.dto;

import com.agrobloc.agrobloc_paiement.model.CommandesVentePaiement;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
public class CommandesVentePaiementDto {

    private UUID id;

    private UUID commandesVenteId;

    private UUID moyensPaiementId;

    private String statut;

    private UUID compteSourceId;

    private UUID compteDestinationId;

    private UUID walletSourceId;

    private UUID walletDestinationId;

    private BigDecimal montant;

    private Instant dateCreation;

    private Instant dateSuccess;

    private String type;

    // Méthode de conversion depuis l’entité
    public static CommandesVentePaiementDto fromEntity(CommandesVentePaiement entity) {
        CommandesVentePaiementDto dto = new CommandesVentePaiementDto();
        dto.setId(entity.getId());
        dto.setCommandesVenteId(entity.getCommandesVente() != null ? entity.getCommandesVente().getId() : null);
        dto.setMoyensPaiementId(entity.getMoyensPaiement() != null ? entity.getMoyensPaiement().getId() : null);
        dto.setStatut(entity.getStatut());
        dto.setCompteSourceId(entity.getCompteSource() != null ? entity.getCompteSource().getId() : null);
        dto.setCompteDestinationId(entity.getCompteDestination() != null ? entity.getCompteDestination().getId() : null);
        dto.setWalletSourceId(entity.getWalletSource() != null ? entity.getWalletSource().getId() : null);
        dto.setWalletDestinationId(entity.getWalletDestination() != null ? entity.getWalletDestination().getId() : null);
        dto.setMontant(entity.getMontant());
        dto.setDateCreation(entity.getDateCreation());
        dto.setDateSuccess(entity.getDateSuccess());
        dto.setType(entity.getType());
        return dto;
    }
}
