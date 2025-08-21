package com.agrobloc.agrobloc_paiement.service;

import com.agrobloc.agrobloc_paiement.dto.CommandesVentePaiementDto;
import com.agrobloc.agrobloc_paiement.enums.StatusTransaction;
import com.agrobloc.agrobloc_paiement.enums.TypeTransaction;
import com.agrobloc.agrobloc_paiement.model.*;
import com.agrobloc.agrobloc_paiement.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;

@Service
public class CommandesVentePaiementService {


    @Autowired
    private CompteService compteService;

    @Autowired
    private UserWalletService userWalletService;

    @Autowired
    private TransactionService transactionService;

    private CommandesVenteRepository commandesVenteRepository;

    @Autowired
    private RealPaymentApiService realPaymentApiService;// interface vers Moov, Orange, banque...


    @Autowired
    private CompteRepository compteRepository;

    @Autowired
    private CommandesVentePaiementRepository commandesVentePaiementRepository;

    @Autowired
    private UserWalletRepository userWalletRepository;


    public CommandesVentePaiementDto effectuerPaiement(String numeroCompteAcheteur,
                                                       UserWallet walletProducteur,
                                                        UUID commandesVenteId) {
        // Récupérer le compte
        Compte compteAcheteur = compteRepository.findByNumeroCompte(numeroCompteAcheteur)
                .orElseThrow(() -> new RuntimeException("Compte non trouvé"));

        // 1️⃣ Vérifier que la commande existe bien
        CommandesVente commande = commandesVenteRepository.findById(commandesVenteId)
                .orElseThrow(() -> new RuntimeException("Commande introuvable avec id: " + commandesVenteId));


        // 1️⃣ Débit compte réel via API externe
        boolean debitOk = realPaymentApiService.debiter(compteAcheteur, commande.getPrixTotal());

        if(!debitOk) {
            throw new RuntimeException("Débit du compte réel échoué !");
        }

        // 2️⃣ Créditer le compte séquestre global
        Compte compteEscrow = compteService.findEscrowAccount();
        compteEscrow.setSolde(compteEscrow.getSolde().add(commande.getPrixTotal()));
        compteService.save(compteEscrow);


        // 2️⃣ Créer une CommandeVentePaiement
        CommandesVentePaiement paiement = new CommandesVentePaiement();

        paiement.setCommandesVente(commande);

        paiement.setCompteSource(compteAcheteur);
        paiement.setCompteDestination(compteEscrow);

        paiement.setWalletSource(null);
        paiement.setWalletDestination(walletProducteur);

        paiement.setMontant(commande.getPrixTotal());
        paiement.setDateCreation(Instant.now());
        paiement.setStatut(StatusTransaction.PENDING.getLibelle()); // par défaut en attente
        paiement.setType(TypeTransaction.PAIEMENT.getLibelle());

        // Sauvegarde
        commandesVentePaiementRepository.save(paiement);

        return CommandesVentePaiementDto.fromEntity(paiement);
    }

        // Méthode pour changer le statut de la transaction et mettre les fonds en attente après validation de la livraison
        public void mettreSoldeEnAttente(CommandesVentePaiement paiement) {
            paiement.setStatut(StatusTransaction.SUCCESS.getLibelle());
            paiement.setDateSuccess(Instant.now());
            commandesVentePaiementRepository.save(paiement);

            // Transférer vers wallet producteur (non utilisable)
            userWalletService.creditWalletEnAttente(paiement.getWalletDestination(), paiement.getMontant());
        }


    // Méthode pour libérer les fonds après 30 jours
    public void libererFondsApres30Jours() {
        Instant maintenant = Instant.now();
        Instant limite = maintenant.minus(30, ChronoUnit.DAYS);

        // Récupérer les transactions qui ont déjà 30 jours en SUCCESS
        List<CommandesVentePaiement> transactionsEligibles =
                commandesVentePaiementRepository.findByStatutAndDateSuccessBefore(
                        StatusTransaction.SUCCESS.getLibelle(), limite
                );

        for (CommandesVentePaiement tx : transactionsEligibles) {
            UserWallet wallet = tx.getWalletDestination();

            if (wallet != null) {
                // Déplacer du solde en attente vers le solde disponible
                wallet.setSoldeEnAttente(wallet.getSoldeEnAttente().subtract(tx.getMontant()));
                wallet.setSoldeDisponible(wallet.getSoldeDisponible().add(tx.getMontant()));

                userWalletRepository.save(wallet);
            }

            // Mettre à jour le statut (optionnel : "RELEASED")
            tx.setStatut(StatusTransaction.FREE.getLibelle());
            commandesVentePaiementRepository.save(tx);
        }
    }

}
