package com.rbac.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Simple health check controller.
 * Executes a trivial SELECT 1 query to verify DB connectivity.
 */
@Controller // OOP: Abstraction (Spring MVC controller abstraction)
public class HomeController {

    private final JdbcTemplate jdbcTemplate;

    @Autowired // OOP: Dependency Injection (encapsulation of data source)
    public HomeController(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @GetMapping("/health")
    public String health(Model model) {
        Integer result = jdbcTemplate.queryForObject("SELECT 1", Integer.class);
        if (result != null && result == 1) {
            model.addAttribute("message", "Database connected");
        } else {
            model.addAttribute("message", "Database connection failed");
        }
        return "health"; // Thymeleaf template name
    }
}
