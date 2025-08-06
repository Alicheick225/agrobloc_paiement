package com.agrobloc.agrobloc_paiement.util;

import javax.crypto.*;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Random;

public class EncryptionService {

    private static final String AES = "AES";
    private static final String AES_ALGORITHM = "AES/GCM/NoPadding";
    private static final int TAG_LENGTH_BIT = 128;

    public static String encrypt(String plainText, String nonce, String b64EncodedKey) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        if (plainText.isEmpty()) {
            throw new IllegalArgumentException("Must provide valid plainText");
        }
        if (b64EncodedKey.isEmpty()) {
            throw new IllegalArgumentException("Must provide valid b64EncodedAESKey");
        }
        if (nonce.isEmpty()) {
            throw new IllegalArgumentException("Must provide valid nonce");
        }

        Cipher cipher = Cipher.getInstance(AES_ALGORITHM);
        GCMParameterSpec gcmSpec = new GCMParameterSpec(TAG_LENGTH_BIT, nonce.getBytes());
        byte[] decodedKeyBytes = Base64.getDecoder().decode(b64EncodedKey);
        SecretKey key = new SecretKeySpec(decodedKeyBytes, AES);
        cipher.init(Cipher.ENCRYPT_MODE, key, gcmSpec);
        byte[] encryptedBytes = cipher.doFinal(plainText.getBytes());

        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    public static String generateNonce(int length) {
        return new Random().ints(length, 0, 62)
                .mapToObj("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"::charAt)
                .collect(StringBuilder::new, StringBuilder::append, StringBuilder::append)
                .toString();
    }


    public static void main(String[] args) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException {
        String b64EncodedAESKey = "Bk7UYqRWwv8/GJRBU9L0o2V63INIy6SIM75Mh1evaTI="; // récupérée du dashboard Flutterwave
        String nonce = EncryptionService.generateNonce(12); // doit être alphanumérique de longueur 12
        String encryptedCardNumber = EncryptionService.encrypt("5531886652142950", nonce, b64EncodedAESKey);
        String encryptedCVV = EncryptionService.encrypt("564", nonce, b64EncodedAESKey);
        String encryptedExpiryMonth = EncryptionService.encrypt("09", nonce, b64EncodedAESKey);
        String encryptedExpiryYear = EncryptionService.encrypt("32", nonce, b64EncodedAESKey);

        System.out.println("Encrypted Payment Data:");
        System.out.println("Nonce: " + nonce);
        System.out.println("Card Number: " + encryptedCardNumber);
        System.out.println("CVV: " + encryptedCVV);
        System.out.println("Expiry Month: " + encryptedExpiryMonth);
        System.out.println("Expiry Year: " + encryptedExpiryYear);
    }


}