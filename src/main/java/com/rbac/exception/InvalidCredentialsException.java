package com.rbac.exception;

/**
 * Thrown when authentication fails due to invalid credentials or disabled account.
 * // OOP: Inheritance – extends RuntimeException.
 */
public class InvalidCredentialsException extends RuntimeException {
    public InvalidCredentialsException() {
        super("Invalid username or password"); // generic message, never reveals which part failed
    }
}
