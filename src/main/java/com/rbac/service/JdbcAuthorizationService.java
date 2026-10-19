package com.rbac.service;

import com.rbac.repository.PermissionRepository;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * JDBC‑based implementation of {@link AuthorizationService}.
 * // OOP: Abstraction – implements the interface.
 * // OOP: Collections – returns a HashSet of permission names.
 */
@Service // OOP: Abstraction – Spring will inject where needed
public class JdbcAuthorizationService implements AuthorizationService {

    private final PermissionRepository permissionRepository;

    public JdbcAuthorizationService(PermissionRepository permissionRepository) {
        this.permissionRepository = permissionRepository;
    }

    @Override
    public boolean hasPermission(int userId, String permission) {
        // Fail‑safe: any unexpected error results in false (deny)
        try {
            List<String> perms = permissionRepository.findPermissionNamesByUserId((long) userId);
            return perms != null && perms.contains(permission);
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public Set<String> getEffectivePermissions(int userId) {
        try {
            List<String> perms = permissionRepository.findPermissionNamesByUserId((long) userId);
            return new HashSet<>(perms);
        } catch (Exception e) {
            // Return empty set on error – safest default is no permissions
            return new HashSet<>();
        }
    }
}
