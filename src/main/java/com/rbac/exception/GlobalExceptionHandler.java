package com.rbac.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

/**
 * Centralized exception handling for the application.
 * // OOP: Abstraction – uses @ControllerAdvice to separate concerns.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * Handles access‑denied situations and shows a friendly 403 page.
     * // OOP: Polymorphism – different exception types handled by separate methods.
     */
    @ExceptionHandler(AccessDeniedException.class)
    public ModelAndView handleAccessDenied(AccessDeniedException ex, Model model) {
        logger.warn("Access denied: {}", ex.getMessage());
        model.addAttribute("errorMessage", ex.getMessage());
        return new ModelAndView("error/403"); // resolves to /templates/error/403.html
    }

    /**
     * Handles authentication failures and redirects back to login with an error.
     */
    @ExceptionHandler(InvalidCredentialsException.class)
    public ModelAndView handleInvalidCredentials(InvalidCredentialsException ex, Model model) {
        logger.info("Authentication failure: {}", ex.getMessage());
        model.addAttribute("error", ex.getMessage());
        return new ModelAndView("login");
    }

    /**
     * Fallback for any other uncaught runtime exception – shows a generic error page.
     */
    @ExceptionHandler(Exception.class)
    public ModelAndView handleGeneric(Exception ex, Model model) {
        logger.error("Unexpected error", ex);
        model.addAttribute("errorMessage", "An unexpected error occurred. Please contact support.");
        return new ModelAndView("error/generic");
    }
}
