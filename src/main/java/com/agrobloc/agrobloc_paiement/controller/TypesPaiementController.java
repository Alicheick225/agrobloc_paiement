package com.agrobloc.agrobloc_paiement.controller;


import com.agrobloc.agrobloc_paiement.model.TypesPaiement;
import com.agrobloc.agrobloc_paiement.service.TypesPaiementService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/typesPaiement")
public class TypesPaiementController {

    private final TypesPaiementService typesPaiementService;

    public TypesPaiementController(TypesPaiementService typesPaiementService) {
        this.typesPaiementService = typesPaiementService;
    }

    @GetMapping
    public List<TypesPaiement> getAllTypesPaiement() {
        return typesPaiementService.getAll();
    }
}
