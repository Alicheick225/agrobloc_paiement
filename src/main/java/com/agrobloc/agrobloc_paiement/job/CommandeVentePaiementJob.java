package com.agrobloc.agrobloc_paiement.job;

import com.agrobloc.agrobloc_paiement.service.CommandesVentePaiementService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class CommandeVentePaiementJob {

    private final CommandesVentePaiementService commandesVentePaiementService;

    public CommandeVentePaiementJob(CommandesVentePaiementService commandesVentePaiementService) {
        this.commandesVentePaiementService = commandesVentePaiementService;

    }

    // S'exécute tous les jours à minuit
    @Scheduled(cron = "0 0 0 * * ?")
    public void libererFonds() {
        commandesVentePaiementService.libererFondsApres30Jours();
    }
}
