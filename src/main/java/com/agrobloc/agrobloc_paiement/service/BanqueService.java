package com.agrobloc.agrobloc_paiement.service;


import com.agrobloc.agrobloc_paiement.model.Banque;
import com.agrobloc.agrobloc_paiement.repository.BanqueRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BanqueService {

    private final BanqueRepository banqueRepository;

    public BanqueService(BanqueRepository banqueRepository) {
        this.banqueRepository = banqueRepository;
    }

    public List<Banque> getAll() {
        return banqueRepository.findAll();
    }

    public Banque save(Banque banque) {
        return banqueRepository.save(banque);
    }
}
