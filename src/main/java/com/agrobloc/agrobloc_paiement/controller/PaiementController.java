package com.agrobloc.agrobloc_paiement.controller;

import com.agrobloc.agrobloc_paiement.service.PaiementService;
import com.paydunya.neptune.PaydunyaCheckoutInvoice;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/paiement")
public class PaiementController {
    private final PaiementService paiementService;

    public PaiementController(PaiementService paiementService) {
        this.paiementService = paiementService;
    }

    // Exemple : créer une facture PayDunya
    @PostMapping("/create-invoice")
    public ResponseEntity<?> createInvoice(@RequestBody InvoiceRequest request) {
        try {
            PaydunyaCheckoutInvoice invoice = paiementService.createInvoice();

            // Ajouter des items à l'invoice
            invoice.addItem(request.getName(), request.getQuantity(), request.getUnitPrice(), request.getTotalPrice(), request.getDescription());

            // Définir le montant total ou laisser PayDunya le calculer
            invoice.setTotalAmount(request.getQuantity() * request.getUnitPrice());

            Map<String, Object> response = new HashMap<>();

            // Tentative de création de facture auprès de PayDunya
            if (invoice.create()) {
                // ✅ Facture créée avec succès
                response.put("status", invoice.getStatus());
                response.put("message", invoice.getResponseText());
                response.put("invoiceUrl", invoice.getInvoiceUrl()); // URL de redirection pour payer
                response.put("token", invoice.getToken());
            } else {
                // ❌ Échec
                response.put("status", "FAILED");
                response.put("message", invoice.getResponseText());
                response.put("code", invoice.getResponseCode());
            }

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erreur lors de la création de l'invoice : " + e.getMessage());
        }
    }

    // Classe interne pour simplifier la requête
    public static class InvoiceRequest {
        private String name;
        private int quantity;
        private double unitPrice;
        private double totalPrice;
        private String description;

        // Getters et setters
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public int getQuantity() { return quantity; }
        public void setQuantity(int quantity) { this.quantity = quantity; }
        public double getUnitPrice() { return unitPrice; }
        public void setUnitPrice(int unitPrice) { this.unitPrice = unitPrice; }
        public double getTotalPrice() { return totalPrice; }
        public void setTotalPrice(int totalPrice) { this.totalPrice = totalPrice; }
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
    }
}

