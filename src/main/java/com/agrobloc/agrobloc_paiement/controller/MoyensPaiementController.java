package com.agrobloc.agrobloc_paiement.controller;


import com.agrobloc.agrobloc_paiement.model.MoyensPaiement;
import com.agrobloc.agrobloc_paiement.service.MoyensPaiementService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/moyensPaiement")
public class MoyensPaiementController {

    private final MoyensPaiementService moyensPaiementService;

    public MoyensPaiementController(MoyensPaiementService moyensPaiementService) {
        this.moyensPaiementService = moyensPaiementService;
    }

    @GetMapping
    public List<MoyensPaiement> getAllMoyensPaiement() {
        return moyensPaiementService.getAll();
    }
}
