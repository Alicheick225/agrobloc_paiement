package com.agrobloc.agrobloc_paiement.controller;

import com.agrobloc.agrobloc_paiement.dto.PaymentRequest;
import com.agrobloc.agrobloc_paiement.dto.TransactionDto;
import com.agrobloc.agrobloc_paiement.service.PaiementService;
import com.paydunya.neptune.PaydunyaCheckoutInvoice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/paiements")
public class PaiementController {

    @Autowired
    private final PaiementService paiementService;

    public PaiementController(PaiementService paiementService) {
        this.paiementService = paiementService;
    }

        @PostMapping("/payer")
        public ResponseEntity<TransactionDto> payer(
                @RequestBody PaymentRequest request) {
            TransactionDto transaction = paiementService.effectuerPaiement(
                    request.getNumeroCompteAcheteur(),
                    request.getWalletProducteur(),
                    request.getMontant()
            );
            return ResponseEntity.ok(transaction);
        }

        
    }



