package com.agrobloc.agrobloc_paiement.config;

import com.agrobloc.agrobloc_paiement.model.Compte;
import com.agrobloc.agrobloc_paiement.model.MoyensPaiement;
import com.agrobloc.agrobloc_paiement.model.User;
import com.agrobloc.agrobloc_paiement.repository.CompteRepository;
import com.agrobloc.agrobloc_paiement.repository.MoyensPaiementRepository;
import com.agrobloc.agrobloc_paiement.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private CompteRepository compteRepository;

    @Autowired
    private MoyensPaiementRepository moyensPaiementRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public void run(String... args) throws Exception {
        // vérifier si le compte séquestre existe déjà
        if (compteRepository.findByNumeroCompte("AGROBLOC-ESCROW-0001").isEmpty()) {
            User systemUser = userRepository.findByEmail("ali.agrobloc@gmail.com")
                    .orElseThrow(() -> new RuntimeException("Utilisateur système non trouvé !")); // compte systeme

            MoyensPaiement mp = moyensPaiementRepository.findByLibelle("ESCROW_ACCOUNT").orElseThrow();

            Compte escrow = new Compte();
            escrow.setNumeroCompte("AGROBLOC-ESCROW-0001"); // nom personnalisé
            escrow.setSolde(BigDecimal.ZERO);
            escrow.setMoyensPaiement(mp);
            escrow.setUser(systemUser);
            escrow.setTypeCompte(Compte.TypeCompte.ESCROW);


            compteRepository.save(escrow);
        }


    }
}
