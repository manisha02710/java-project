package com.rbac.config;

import com.rbac.model.User;
import com.rbac.model.Role;
import com.rbac.model.Permission;
import com.rbac.repository.UserRepository;
import com.rbac.repository.RoleRepository;
import com.rbac.repository.PermissionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Simple runner that verifies repository queries at application start.
 * // OOP: Polymorphism – implements CommandLineRunner interface.
 */
@Component
public class DataVerifier implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(DataVerifier.class);

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;

    public DataVerifier(UserRepository userRepository,
                         RoleRepository roleRepository,
                         PermissionRepository permissionRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.permissionRepository = permissionRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        logger.info("=== DataVerifier start ===");

        // 1. List all users
        List<User> users = userRepository.findAll();
        logger.info("Total users: {}", users.size());
        users.forEach(u -> logger.info("User: {} ({})", u.getUsername(), u.getEmail()));

        // 2. List all roles
        List<Role> roles = roleRepository.findAll();
        logger.info("Total roles: {}", roles.size());
        roles.forEach(r -> logger.info("Role: {}", r.getRoleName()));

        // 3. For the first user, print permission names
        if (!users.isEmpty()) {
            Long userId = users.get(0).getId();
            List<String> perms = permissionRepository.findPermissionNamesByUserId(userId);
            logger.info("Permissions for user {}: {}", users.get(0).getUsername(), perms);
        }

        logger.info("=== DataVerifier end ===");
    }
}
