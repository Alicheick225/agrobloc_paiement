package com.agrobloc.agrobloc_paiement.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class FlutterwaveConfig {

    @Value("${flutterwave.secret-key}")
    private String secretKey;

    @Value("${flutterwave.base-url}")
    private String baseUrl;

    public String getAccessToken() {
        return secretKey;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
