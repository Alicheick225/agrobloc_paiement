package com.agrobloc.agrobloc_paiement.controller;


import com.agrobloc.agrobloc_paiement.model.Banque;
import com.agrobloc.agrobloc_paiement.service.BanqueService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/banques")
public class BanqueController  {

    private final BanqueService banqueService;

    public BanqueController(BanqueService banqueService) {
        this.banqueService = banqueService;
    }

    @GetMapping
    public List<Banque> getAllBanques() {
        return banqueService.getAll();
    }

    @PostMapping
    public Banque createBanque(@RequestBody Banque banque) {
        return banqueService.save(banque);
    }

}




