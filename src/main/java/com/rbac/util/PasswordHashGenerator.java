package com.rbac.util;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * Utility component to generate BCrypt password hashes.
 * <p>
 * This class is only active when the {@code generate-hash} Spring profile is enabled.
 * Run the application with the profile to print BCrypt hashes for the passwords
 * passed as command‑line arguments.
 * <p>
 * Example command:
 * <pre>
 *   mvn spring-boot:run -Dspring-boot.run.profiles=generate-hash \
 *       -Dspring-boot.run.arguments="Admin@123 Faculty@123 Student1@123 Student2@123"
 * </pre>
 * The hashes will be printed to the console, one per line, in the order of the
 * supplied passwords.
 */
@Profile("generate-hash")
@Component
public class PasswordHashGenerator implements CommandLineRunner {

    /**
     * OOP Concept: Encapsulation – the password encoder is a private field.
     */
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public void run(String... args) throws Exception {
        if (args == null || args.length == 0) {
            System.out.println("No passwords provided. Pass passwords as command‑line arguments.");
            return;
        }
        System.out.println("Generated BCrypt hashes:");
        for (String rawPassword : args) {
            String hash = passwordEncoder.encode(rawPassword);
            System.out.println(hash);
        }
    }
}
