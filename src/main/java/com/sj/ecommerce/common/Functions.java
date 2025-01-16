package com.sj.ecommerce.common;

import java.security.SecureRandom;
import java.util.UUID;

public class Functions {

    private static final String LOWERCASE = "abcdefghijklmnopqrstuvwxyz";
    private static final String UPPERCASE = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String DIGITS = "0123456789";
    private static final String SPECIAL_CHARACTERS = "!@#$%&*=+";
    private static final String ALL_CHARACTERS = LOWERCASE + UPPERCASE + DIGITS + SPECIAL_CHARACTERS;

    public static String generatePassword() {
        StringBuilder password = new StringBuilder();
        for (int i = 0; i < 8; i++) {
            int index = (int) (Math.random() * ALL_CHARACTERS.length());
            password.append(ALL_CHARACTERS.charAt(index));
        }
        return password.toString();
    }

    public static String randomIdGenerator() {
        return UUID.randomUUID().toString().replace("-", "").substring(0, 25);
    }

    public static String generateOtp() {
        SecureRandom random = new SecureRandom();
        int otp = 100000 + random.nextInt(900000);
        return String.valueOf(otp);
    }

    public static void main(String[] args) {
        System.out.println("randomIdGenerator = " + randomIdGenerator());
        System.out.println("generateOtp = " + generateOtp());
    }


}
