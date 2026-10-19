package com.rbac.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * Simple utility to generate BCrypt hashes from a plain password.
 * Run it from the command line:
 *   java -cp target/classes com.rbac.util.BCryptGenerator "Admin@123"
 * The hash will be printed to STDOUT – copy it into <code>seed.sql</code>.
 * // OOP: Encapsulation – only one public static method.
 */
public class BCryptGenerator {
    private static final BCryptPasswordEncoder ENCODER = new BCryptPasswordEncoder();

    public static void main(String[] args) {
        if (args.length != 1) {
            System.err.println("Usage: BCryptGenerator <plain-password>");
            System.exit(1);
        }
        String plain = args[0];
        String hash = ENCODER.encode(plain);
        System.out.println("BCrypt hash: " + hash);
    }
}
