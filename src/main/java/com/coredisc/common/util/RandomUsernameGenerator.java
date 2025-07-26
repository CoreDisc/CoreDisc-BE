package com.coredisc.common.util;

import java.security.SecureRandom;

public class RandomUsernameGenerator {

    private static final String CHARACTERS = "abcdefghijklmnopqrstuvwxyz0123456789";
    private static final SecureRandom random = new SecureRandom();

    public static String generateRandomUsername() {

        int length = random.nextInt(11) + 5; // 5 ~ 15
        StringBuilder username = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            int idx = random.nextInt(CHARACTERS.length());
            username.append(CHARACTERS.charAt(idx));
        }

        return username.toString();
    }
}
