package org.example.security;

import java.util.Random;

public class OTPGenerator {

    private OTPGenerator() {
    }

    public static String generateOTP(int length) {
        String allowedChars = "0123456789";
        StringBuilder otp = new StringBuilder();
        Random random = new Random();

        for (int i = 0; i < length; i++) {
            int index = random.nextInt(allowedChars.length());
            char randomChar = allowedChars.charAt(index);
            otp.append(randomChar);
        }
        return otp.toString();
    }
}