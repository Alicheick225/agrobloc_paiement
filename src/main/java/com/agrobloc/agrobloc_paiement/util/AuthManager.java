package com.agrobloc.agrobloc_paiement.util;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Scanner;

import org.json.JSONObject;

import java.io.OutputStream;

public class AuthManager {

    private String clientId;
    private String clientSecret;
    private String accessToken;
    private LocalDateTime expiryTime;

    public AuthManager() {
        this.clientId = System.getenv("flutterwave.public-key");
        this.clientSecret = System.getenv("flutterwave.secret-key");
        this.accessToken = null;
        this.expiryTime = null;
    }

    public synchronized String getAccessToken() throws IOException {
        if (accessToken == null || expiryTime == null || LocalDateTime.now().isAfter(expiryTime.minusMinutes(1))) {
            generateAccessToken();
        }
        return accessToken;
    }

    private void generateAccessToken() throws IOException {
        URL url = new URL("https://idp.flutterwave.com/realms/flutterwave/protocol/openid-connect/token");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();

        con.setRequestMethod("POST");
        con.setDoOutput(true);
        con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

        String data = "client_id=" + clientId +
                "&client_secret=" + clientSecret +
                "&grant_type=client_credentials";

        try (OutputStream os = con.getOutputStream()) {
            os.write(data.getBytes());
            os.flush();
        }

        int responseCode = con.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            String response = new Scanner(con.getInputStream()).useDelimiter("\\A").next();
            JSONObject json = new JSONObject(response);

            this.accessToken = json.getString("access_token");
            int expiresIn = json.getInt("expires_in"); // in seconds
            this.expiryTime = LocalDateTime.now().plus(expiresIn, ChronoUnit.SECONDS);

        } else {
            throw new IOException("Failed to fetch access token, HTTP code: " + responseCode);
        }
    }
}
