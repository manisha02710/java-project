package com.rbac.controller;

import com.rbac.annotation.RequiresPermission;
import com.rbac.model.Permission;
import com.rbac.model.Role;
import com.rbac.service.RoleService;
import com.rbac.service.PermissionService;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Administrative UI for managing roles and their permissions.
 * // OOP: Encapsulation – delegates to RoleService & PermissionService.
 */
@Controller
public class RoleAdminController {

    private final RoleService roleService;
    private final PermissionService permissionService;

    public RoleAdminController(RoleService roleService, PermissionService permissionService) {
        this.roleService = roleService;
        this.permissionService = permissionService;
    }

    @RequiresPermission("ROLE_READ")
    @GetMapping("/admin/roles")
    public String listRoles(Model model) {
        List<Role> roles = roleService.listRoles();
        model.addAttribute("roles", roles);
        return "admin/roles"; // Thymeleaf template
    }

    @RequiresPermission("ROLE_CREATE")
    @GetMapping("/admin/roles/create")
    public String createRoleForm() {
        return "admin/createRole";
    }

    @RequiresPermission("ROLE_CREATE")
    @PostMapping("/admin/roles/create")
    public String createRole(@RequestParam @NotBlank @Size(min = 3, max = 30) String roleName,
                             @RequestParam String description,
                             RedirectAttributes redirectAttrs) {
        try {
            // Assuming the creator's userId is stored in session; retrieve via a helper (skipped here)
            Long creatorId = null; // placeholder – in real code pull from SessionUser
            roleService.createRole(roleName, description, creatorId);
            redirectAttrs.addFlashAttribute("success", "Role created.");
        } catch (Exception e) {
            redirectAttrs.addFlashAttribute("error", "Failed to create role: " + e.getMessage());
        }
        return "redirect:/admin/roles";
    }

    /**
     * View and edit permissions for a specific role.
     * Permissions are displayed as checkboxes; submitted IDs are the selected set.
     */
    @RequiresPermission("PERMISSION_ASSIGN")
    @GetMapping("/admin/roles/{roleId}/permissions")
    public String editRolePermissions(@RequestParam Long roleId, Model model) {
        Role role = roleService.listRoles().stream()
                .filter(r -> r.getId().equals(roleId))
                .findFirst()
                .orElse(null);
        if (role == null) {
            model.addAttribute("error", "Role not found");
            return "admin/roles";
        }
        List<Permission> allPermissions = permissionService.listAll();
        List<Permission> assigned = roleService.listPermissionsForRole(roleId);
        Set<Long> assignedIds = assigned.stream().map(Permission::getId).collect(Collectors.toSet());
        model.addAttribute("role", role);
        model.addAttribute("allPermissions", allPermissions);
        model.addAttribute("assignedIds", assignedIds);
        return "admin/rolePermissions";
    }

    @RequiresPermission("PERMISSION_ASSIGN")
    @PostMapping("/admin/roles/{roleId}/permissions")
    public String updateRolePermissions(@RequestParam Long roleId,
                                         @RequestParam(required = false) List<Long> permissionIds,
                                         RedirectAttributes redirectAttrs) {
        try {
            // First, remove all existing permissions for the role
            List<Permission> current = roleService.listPermissionsForRole(roleId);
            for (Permission p : current) {
                roleService.removePermission(roleId, p.getId(), null);
            }
            // Then, assign the selected ones (permissionIds may be null if none checked)
            if (permissionIds != null) {
                for (Long pid : permissionIds) {
                    roleService.assignPermission(roleId, pid, null);
                }
            }
            redirectAttrs.addFlashAttribute("success", "Permissions updated.");
        } catch (Exception e) {
            redirectAttrs.addFlashAttribute("error", "Failed to update permissions: " + e.getMessage());
        }
        return "redirect:/admin/roles";
    }
}
