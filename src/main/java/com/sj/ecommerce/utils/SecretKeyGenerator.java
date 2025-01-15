package com.sj.ecommerce.utils;

import javax.crypto.KeyGenerator;
import java.util.Base64;

public class SecretKeyGenerator {
    public static void main(String[] args) throws Exception {
        // Generate a 512-bit key for HS512 algorithm
        KeyGenerator keyGenerator = KeyGenerator.getInstance("HmacSHA512");
        keyGenerator.init(512); // 512-bit key
        byte[] secretKey = keyGenerator.generateKey().getEncoded();

        // Encode the key in Base64
        String base64Key = Base64.getEncoder().encodeToString(secretKey);

        // Output the generated key
        System.out.println("Generated Secret Key (Base64): " + base64Key);
    }
}
