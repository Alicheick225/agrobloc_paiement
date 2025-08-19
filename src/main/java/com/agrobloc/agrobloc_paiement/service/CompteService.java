package com.agrobloc.agrobloc_paiement.service;


import com.agrobloc.agrobloc_paiement.model.Compte;
import com.agrobloc.agrobloc_paiement.model.MoyensPaiement;
import com.agrobloc.agrobloc_paiement.model.User;
import com.agrobloc.agrobloc_paiement.repository.CompteRepository;
import com.agrobloc.agrobloc_paiement.repository.MoyensPaiementRepository;
import com.agrobloc.agrobloc_paiement.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Service
public class CompteService {

    @Autowired
    private final CompteRepository compteRepository;

    @Autowired
    private final MoyensPaiementRepository moyensPaiementRepository;

    @Autowired
    private final UserRepository userRepository;

    public CompteService(CompteRepository compteRepository, MoyensPaiementRepository moyensPaiementRepository, UserRepository userRepository) {
        this.compteRepository = compteRepository;
        this.moyensPaiementRepository = moyensPaiementRepository;
        this.userRepository = userRepository;
    }

    public List<Compte> getAll() {
        return compteRepository.findAll();
    }

    public Compte save(Compte compte) {
        if (compte.getMoyensPaiement() == null || compte.getMoyensPaiement().getId() == null) {
            throw new IllegalArgumentException("MoyensPaiement obligatoire");
        }
        if (compte.getUser() == null || compte.getUser().getId() == null) {
            throw new IllegalArgumentException("User obligatoire");
        }

        MoyensPaiement moyensPaiement = moyensPaiementRepository.findById(compte.getMoyensPaiement().getId())
                .orElseThrow(() -> new EntityNotFoundException("MoyensPaiement non trouvée"));
        User user = userRepository.findById(compte.getUser().getId())
                .orElseThrow(() -> new EntityNotFoundException("User non trouvé"));

        compte.setMoyensPaiement(moyensPaiement);
        compte.setUser(user);
        return compteRepository.save(compte);
    }


       public void delete(Compte compte) {
           compteRepository.delete(compte);
       }

        public Optional<Compte> findByNumeroCompte( String numeroCompte) {
            return compteRepository.findByNumeroCompte(numeroCompte);
        }

}
