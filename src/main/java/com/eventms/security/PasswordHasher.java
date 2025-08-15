package com.eventms.security;

import java.security.SecureRandom;
import java.util.Base64;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

/**
 * PBKDF2 password hasher. Format: pbkdf2$<iterations>$<salt_b64>$<hash_b64>
 */
public final class PasswordHasher {
    private static final int ITERATIONS = 150_000;
    private static final int KEY_LENGTH = 256; // bits
    private static final int SALT_LEN = 16; // bytes

    private PasswordHasher() {}

    public static String hash(String password) {
        try {
            byte[] salt = new byte[SALT_LEN];
            new SecureRandom().nextBytes(salt);
            byte[] derived = pbkdf2(password.toCharArray(), salt, ITERATIONS, KEY_LENGTH);
            return "pbkdf2$" + ITERATIONS + "$" + Base64.getEncoder().encodeToString(salt) + "$" + Base64.getEncoder().encodeToString(derived);
        } catch (Exception e) {
            throw new RuntimeException("Failed to hash password", e);
        }
    }

    public static boolean verify(String password, String stored) {
        try {
            if (stored == null || !stored.startsWith("pbkdf2$")) return false;
            String[] parts = stored.split("\\$");
            int iters = Integer.parseInt(parts[1]);
            byte[] salt = Base64.getDecoder().decode(parts[2]);
            byte[] hash = Base64.getDecoder().decode(parts[3]);
            byte[] derived = pbkdf2(password.toCharArray(), salt, iters, hash.length * 8);
            return constantTimeEquals(hash, derived);
        } catch (Exception e) {
            return false;
        }
    }

    private static byte[] pbkdf2(char[] password, byte[] salt, int iterations, int keyLen) throws Exception {
        PBEKeySpec spec = new PBEKeySpec(password, salt, iterations, keyLen);
        SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        return skf.generateSecret(spec).getEncoded();
    }

    private static boolean constantTimeEquals(byte[] a, byte[] b) {
        if (a.length != b.length) return false;
        int r = 0;
        for (int i = 0; i < a.length; i++) r |= a[i] ^ b[i];
        return r == 0;
    }
}
