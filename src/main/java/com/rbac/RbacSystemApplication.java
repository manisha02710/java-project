package com.rbac;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main entry point for the RBAC Management System Spring Boot application.
 */
@SpringBootApplication // OOP: Abstraction (Spring boot handles wiring)
public class RbacSystemApplication {
    public static void main(String[] args) {
        SpringApplication.run(RbacSystemApplication.class, args);
    }
}
