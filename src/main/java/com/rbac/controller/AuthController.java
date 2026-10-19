package com.rbac.controller;

import com.rbac.exception.InvalidCredentialsException;
import com.rbac.model.SessionUser;
import com.rbac.service.AuthService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Handles login and logout operations.
 * // OOP: Encapsulation – private AuthService dependency.
 */
@Controller // OOP: Abstraction – Spring MVC hides HTTP details
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping("/login")
    public String loginForm() {
        return "login"; // resolves to login.html
    }

    @PostMapping("/login")
    public String loginSubmit(@RequestParam String username,
                              @RequestParam String password,
                              Model model,
                              HttpSession session) {
        try {
            SessionUser sessionUser = authService.authenticate(username, password);
            // Store minimal user data in session – OOP: Composition (SessionUser).
            session.setAttribute("sessionUser", sessionUser);
            return "redirect:/"; // home or dashboard (you can adjust)
        } catch (InvalidCredentialsException e) {
            // Generic error message – never reveal which credential failed
            model.addAttribute("error", "Invalid username or password");
            return "login";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate(); // clear all session data
        return "redirect:/login";
    }
}
