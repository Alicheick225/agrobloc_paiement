package com.agrobloc.agrobloc_paiement.controller;

import com.agrobloc.agrobloc_paiement.dto.CommandesVentePaiementDto;
import com.agrobloc.agrobloc_paiement.dto.PaymentRequest;
import com.agrobloc.agrobloc_paiement.enums.StatusCommandesVente;
import com.agrobloc.agrobloc_paiement.model.CommandesVente;
import com.agrobloc.agrobloc_paiement.repository.CommandesVentePaiementRepository;
import com.agrobloc.agrobloc_paiement.repository.CommandesVenteRepository;
import com.agrobloc.agrobloc_paiement.service.CommandesVentePaiementService;
import com.agrobloc.agrobloc_paiement.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/commandes-ventes")
public class CommandesVenteController {

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private CommandesVenteRepository commandesVenteRepository;

    @Autowired
    private CommandesVentePaiementRepository commandesVentePaiementRepository;
    @Autowired
    private CommandesVentePaiementService commandesVentePaiementService;


//    @PostMapping("/commande_enregistree")
//    public ResponseEntity<String> creerCommandeVentePaiement(@RequestBody CommandesVenteDto dto) {
//        try {
//            // 1️⃣ Vérifier que la commande existe bien
//            CommandesVente commande = commandesVenteRepository.findById(dto.getId())
//                    .orElseThrow(() -> new RuntimeException("Commande introuvable avec id: " + dto.getId()));
//
//            // 2️⃣ Créer une CommandeVentePaiement
//            CommandesVentePaiement paiement = new CommandesVentePaiement();
//            paiement.setId(UUID.randomUUID());
//            paiement.setCommandesVente(commande);
//
//            paiement.setCompteSource(null);
//            paiement.setCompteDestination(null);
//
//            paiement.setWalletSource(null);
//            paiement.setWalletDestination(null);
//
//            paiement.setMontant(commande.getPrixTotal());
//            paiement.setDateCreation(Instant.now());
//            paiement.setStatut(StatusTransaction.PENDING.getLibelle()); // par défaut en attente
//            paiement.setType(TypeTransaction.PAIEMENT.getLibelle());
//
//            // Sauvegarde
//            commandesVentePaiementRepository.save(paiement);
//
//            return ResponseEntity.ok("CommandeVentePaiement créée avec succès, id=" + paiement.getId());
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                    .body("Erreur lors de la création CommandeVentePaiement: " + e.getMessage());
//        }
//    }

    @PostMapping("/payer")
    public ResponseEntity<CommandesVentePaiementDto> payer(
            @RequestBody PaymentRequest request) {
        CommandesVentePaiementDto paiement = commandesVentePaiementService.effectuerPaiement(
                request.getNumeroCompteAcheteur(),
                request.getWalletProducteur(),
                request.getCommandeId()
        );
        return ResponseEntity.ok(paiement);
    }

    @GetMapping("/confirmer-reception")
    public ResponseEntity<CommandesVente> Confirmation(@RequestBody PaymentRequest request) {

            //Changer le statut de la commande concernée
            CommandesVente commandesVente = commandesVenteRepository.findById(request.getCommandeId())
                    .orElseThrow(() -> new RuntimeException("Compte non trouvé"));

            commandesVente.setStatut(StatusCommandesVente.TERMINEE.getLibelle());

            commandesVenteRepository.save(commandesVente);

            //Changer le statut de la transaction (commandeVentePaiement) et mettre à jour le wallet
             commandesVentePaiementService.mettreSoldeEnAttente(commandesVente.getPaiement());


             return ResponseEntity.ok(commandesVente);

    }




}




