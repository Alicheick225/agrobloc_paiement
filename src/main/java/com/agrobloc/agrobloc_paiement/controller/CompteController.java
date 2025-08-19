package com.agrobloc.agrobloc_paiement.controller;


import com.agrobloc.agrobloc_paiement.model.Compte;
import com.agrobloc.agrobloc_paiement.model.Transaction;
import com.agrobloc.agrobloc_paiement.repository.TransactionRepository;
import com.agrobloc.agrobloc_paiement.service.CompteService;
import com.agrobloc.agrobloc_paiement.service.TransactionService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/comptes")
public class CompteController {

    private final CompteService compteService;

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private TransactionRepository transactionRepository;

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


    @PutMapping("/credit/{numeroCompte}")
    public Compte creditCompte(@PathVariable String numeroCompte,
                               @RequestBody Map<String, String> request) {
        BigDecimal montant = new BigDecimal(request.get("montant"));
        return transactionService.creditFromEscrow(numeroCompte, montant);
    }

    @PutMapping("/debit/{numeroCompte}")
    public Compte debitCompte(@PathVariable String numeroCompte,
                              @RequestBody Map<String, String> request) {
        BigDecimal montant = new BigDecimal(request.get("montant"));
        return transactionService.debitToEscrow(numeroCompte, montant);
    }

    }


