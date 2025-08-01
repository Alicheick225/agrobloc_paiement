package com.agrobloc.agrobloc_paiement.controller;


import com.agrobloc.agrobloc_paiement.model.Compte;
import com.agrobloc.agrobloc_paiement.service.CompteService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comptes")
public class CompteController {

    private final CompteService compteService;

    public CompteController(CompteService compteService) {
        this.compteService = compteService;
    }

    @GetMapping
    public List<Compte> getAllComptes() {
        return compteService.getAll();
    }

    @PostMapping
    public Compte createCompte(@RequestBody Compte compte) {
        return compteService.save(compte);
    }

}
