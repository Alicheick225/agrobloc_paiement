package com.agrobloc.agrobloc_paiement.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/webhook")
public class PaydunyaWebhookController {

    @PostMapping("/paydunya")
    public ResponseEntity<String> handlePaydunyaWebhook(@RequestBody Map<String, Object> payload) {

        System.out.println("📩 Webhook PayDunya reçu : " + payload);

        // Accéder à la clé "data"
        Map<String, Object> data = (Map<String, Object>) payload.get("data");

        if (data != null) {
            String status = (String) data.get("status");
            String mode = (String) data.get("mode");
            Map<String, Object> customer = (Map<String, Object>) data.get("customer");

            if ("completed".equalsIgnoreCase(status)) {
                System.out.println("✅ Paiement confirmé");
                System.out.println("Client : " + customer.get("name"));
                System.out.println("Montant total : " + data.get("total_amount"));

                // ➡️ Ici : mettre à jour ta base de données, marquer la commande comme payée
            } else {
                System.out.println("❌ Paiement échoué ou en attente");
            }
        }

        // Toujours répondre 200 OK à PayDunya pour éviter les retries
        return ResponseEntity.ok("Webhook reçu");
    }
}
