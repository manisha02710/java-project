package com.rbac.controller;

import com.rbac.annotation.RequiresPermission;
import com.rbac.exception.InvalidCredentialsException;
import com.rbac.model.User;
import com.rbac.service.UserService;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.List;

/**
 * Administrative UI for managing users.
 * // OOP: Encapsulation – controller delegates to UserService.
 */
@Controller
public class UserAdminController {

    private final UserService userService;

    public UserAdminController(UserService userService) {
        this.userService = userService;
    }

    @RequiresPermission("USER_READ")
    @GetMapping("/admin/users")
    public String listUsers(Model model) {
        List<User> users = userService.listUsers();
        model.addAttribute("users", users);
        return "admin/users"; // Thymeleaf template
    }

    @RequiresPermission("USER_CREATE")
    @GetMapping("/admin/users/create")
    public String createUserForm() {
        return "admin/createUser";
    }

    @RequiresPermission("USER_CREATE")
    @PostMapping("/admin/users/create")
    public String createUser(
            @RequestParam @NotBlank @Size(min = 3, max = 20) String username,
            @RequestParam @NotBlank @Size(min = 6) String password,
            @RequestParam @NotBlank String fullName,
            @RequestParam @Email String email,
            @RequestParam(defaultValue = "true") boolean active,
            RedirectAttributes redirectAttrs) {
        try {
            userService.createUser(username, password, fullName, email, active);
            redirectAttrs.addFlashAttribute("success", "User created successfully.");
        } catch (Exception e) {
            redirectAttrs.addFlashAttribute("error", "Failed to create user: " + e.getMessage());
        }
        return "redirect:/admin/users";
    }

    @RequiresPermission("USER_DISABLE")
    @PostMapping("/admin/users/setActive")
    public String setActive(@RequestParam Long userId,
                            @RequestParam boolean active,
                            RedirectAttributes redirectAttrs) {
        try {
            userService.setActive(userId, active);
            redirectAttrs.addFlashAttribute("success", "User " + (active ? "enabled" : "disabled") + ".");
        } catch (Exception e) {
            redirectAttrs.addFlashAttribute("error", "Failed to update status: " + e.getMessage());
        }
        return "redirect:/admin/users";
    }

    @RequiresPermission("ROLE_ASSIGN")
    @PostMapping("/admin/users/assignRole")
    public String assignRole(@RequestParam Long userId,
                              @RequestParam Long roleId,
                              RedirectAttributes redirectAttrs) {
        try {
            userService.assignRole(userId, roleId);
            redirectAttrs.addFlashAttribute("success", "Role assigned.");
        } catch (Exception e) {
            redirectAttrs.addFlashAttribute("error", "Failed to assign role: " + e.getMessage());
        }
        return "redirect:/admin/users";
    }

    @RequiresPermission("ROLE_ASSIGN")
    @PostMapping("/admin/users/removeRole")
    public String removeRole(@RequestParam Long userId,
                              @RequestParam Long roleId,
                              RedirectAttributes redirectAttrs) {
        try {
            userService.removeRole(userId, roleId);
            redirectAttrs.addFlashAttribute("success", "Role removed.");
        } catch (Exception e) {
            redirectAttrs.addFlashAttribute("error", "Failed to remove role: " + e.getMessage());
        }
        return "redirect:/admin/users";
    }
}
