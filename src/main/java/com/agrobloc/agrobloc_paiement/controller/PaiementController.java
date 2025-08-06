package com.agrobloc.agrobloc_paiement.controller;

import com.agrobloc.agrobloc_paiement.config.FlutterwaveConfig;
import com.agrobloc.agrobloc_paiement.dto.PaymentRequest;
import com.agrobloc.agrobloc_paiement.util.AuthManager;
import com.agrobloc.agrobloc_paiement.util.EncryptionService;
import com.agrobloc.agrobloc_paiement.util.HeaderUtil;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

@RestController
@RequestMapping("api/paiements/")
public class PaiementController {

    private final RestTemplate restTemplate;
    private final FlutterwaveConfig config;
    private final AuthManager authManager = new AuthManager();

    public PaiementController(RestTemplate restTemplate, FlutterwaveConfig config) throws IOException {
        this.restTemplate = restTemplate;
        this.config = config;
    }

    public String accessToken = authManager.getAccessToken();
    public String traceId = HeaderUtil.generateTraceId();

//    String b64EncodedAESKey = "Bk7UYqRWwv8/GJRBU9L0o2V63INIy6SIM75Mh1evaTI="; // récupérée du dashboard Flutterwave
//    String nonce = EncryptionService.generateNonce(12); // doit être alphanumérique de longueur 12
//    String encryptedCardNumber = EncryptionService.encrypt("5531886652142950", nonce, b64EncodedAESKey);
//    String encryptedCVV = EncryptionService.encrypt("564", nonce, b64EncodedAESKey);
//    String encryptedExpiryMonth = EncryptionService.encrypt("09", nonce, b64EncodedAESKey);
//    String encryptedExpiryYear = EncryptionService.encrypt("32", nonce, b64EncodedAESKey);

@PostMapping("customers")
    public ResponseEntity<String> createCustomer(String accessToken, String traceId) {
        String url = "https://api.flutterwave.com/v3/customers";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);
        headers.set("Content-Type", "application/json");
        headers.set("X-Trace-Id", traceId);
        headers.set("X-Idempotency-Key", HeaderUtil.generateIdempotencyKey());

        Map<String, Object> body = Map.of(
                "first_name", "John",
                "last_name", "Doe",
                "email", "john.doe@example.com"
        );

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);
        return restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
    }

@PostMapping("payment-methods")
    public ResponseEntity<String> createPaymentMethod(String accessToken, String traceId, String nonce,
                                                      String encryptedCard, String encryptedMonth,
                                                      String encryptedYear, String encryptedCVV) {
        String url = "https://api.flutterwave.com/developersandbox/payment-methods";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);
        headers.set("Content-Type", "application/json");
        headers.set("X-Trace-Id", traceId);
        headers.set("X-Idempotency-Key", HeaderUtil.generateIdempotencyKey());

        Map<String, Object> card = Map.of(
                "nonce", nonce,
                "encrypted_card_number", encryptedCard,
                "encrypted_expiry_month", encryptedMonth,
                "encrypted_expiry_year", encryptedYear,
                "encrypted_cvv", encryptedCVV
        );

        Map<String, Object> body = Map.of(
                "type", "card",
                "card", card
        );

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);
        return restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
    }

@PostMapping("payments")
    public ResponseEntity<String> createPayment(String accessToken, String traceId) {
        String url = "https://api.flutterwave.com/v3/payments";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);
        headers.set("Content-Type", "application/json");
        headers.set("X-Trace-Id", traceId);
        headers.set("X-Idempotency-Key", HeaderUtil.generateIdempotencyKey());

        Map<String, Object> body = Map.of(
                "amount", 500,
                "currency", "XOF",
                "payment_method", "card",
                "redirect_url", "https://yourdomain.com/payment/success",
                "description", "Test payment with encrypted card",
                "customer", Map.of(
                        "email", "john.doe@example.com"
                )
        );

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);
        return restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
    }

//    public ResponseEntity<String> orchestratePayment(PaymentRequest request) {
//
//
//        createCustomer(accessToken, traceId, request);
//        createPaymentMethod(accessToken, traceId, request);
//        return createPayment(accessToken, traceId, request);
//    }
}
