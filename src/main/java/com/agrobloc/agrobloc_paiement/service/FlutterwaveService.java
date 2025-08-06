package com.agrobloc.agrobloc_paiement.service;

import com.agrobloc.agrobloc_paiement.config.FlutterwaveConfig;
import com.agrobloc.agrobloc_paiement.dto.PaymentRequest;
import com.agrobloc.agrobloc_paiement.util.HeaderUtil;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class FlutterwaveService {

    private final RestTemplate restTemplate;
    private final FlutterwaveConfig config;

    public FlutterwaveService(RestTemplate restTemplate, FlutterwaveConfig config) {
        this.restTemplate = restTemplate;
        this.config = config;
    }

    // ... insert methods createCustomer(), createPaymentMethod(), createPayment() ici

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


}
