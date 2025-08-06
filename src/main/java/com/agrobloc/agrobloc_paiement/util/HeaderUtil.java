package com.agrobloc.agrobloc_paiement.util;


import java.util.UUID;

public class HeaderUtil {
    public static String generateTraceId() {
        return UUID.randomUUID().toString(); // ex: "9d30582d-1a38-4c6e-9932-3dff38de7f26"
    }

    public static String generateIdempotencyKey() {
        return UUID.randomUUID().toString(); // unique pour chaque appel POST
    }
}