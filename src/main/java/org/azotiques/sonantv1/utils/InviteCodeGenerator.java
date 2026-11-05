package org.azotiques.sonantv1.utils;

import java.security.SecureRandom;
import java.util.stream.Collectors;

public class InviteCodeGenerator {
    private static final String ALPHABET = "23456789ABCDEFGHJKLMNPQRSTUVWXYZ";
    private static final int CODE_LENGTH = 6;
    private static final SecureRandom RANDOM = new SecureRandom();

    public static String generate() {
        return RANDOM.ints(CODE_LENGTH, 0, ALPHABET.length())
                .mapToObj(ALPHABET::charAt)
                .map(Object::toString)
                .collect(Collectors.joining());
    }
}
