package com.rbac.service;

import com.rbac.exception.InvalidCredentialsException; // Not used here but keep imports tidy
import com.rbac.model.User;
import com.rbac.repository.UserRepository;
import com.rbac.repository.RoleRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.Optional;

/**
 * Service containing business logic for user management.
 * // OOP: Encapsulation – only services expose operations.
 * // OOP: Transaction – @Transactional ensures atomic multi‑step changes.
 */
@Service
@Validated
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final AuditService auditService;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(); // OOP: Polymorphism

    public UserService(UserRepository userRepository,
                       RoleRepository roleRepository,
                       AuditService auditService) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.auditService = auditService;
    }

    /** List all users */
    public List<User> listUsers() {
        return userRepository.findAll();
    }

    /** Create a new user with a BCrypt‑hashed password */
    @Transactional
    public User createUser(String username, String rawPassword, String fullName, String email, boolean isActive) {
        String hash = passwordEncoder.encode(rawPassword);
        User user = new User();
        user.setUsername(username);
        user.setPasswordHash(hash);
        user.setFullName(fullName);
        user.setEmail(email);
        user.setIsActive(isActive);
        Long id = userRepository.save(user);
        user.setId(id);
        auditService.log(id, "USER_CREATE", "Created user " + username, com.rbac.model.AuditLog.Status.SUCCESS);
        return user;
    }

    /** Enable or disable a user */
    @Transactional
    public void setActive(Long userId, boolean active) {
        userRepository.setActive(userId, active);
        auditService.log(userId, "USER_DISABLE", "Set active=" + active, com.rbac.model.AuditLog.Status.SUCCESS);
    }

    /** Assign a role to a user */
    @Transactional
    public void assignRole(Long userId, Long roleId) {
        roleRepository.assignRoleToUser(userId, roleId);
        auditService.log(userId, "ROLE_ASSIGN", "Assigned roleId=" + roleId, com.rbac.model.AuditLog.Status.SUCCESS);
    }

    /** Remove a role from a user */
    @Transactional
    public void removeRole(Long userId, Long roleId) {
        roleRepository.removeRoleFromUser(userId, roleId);
        auditService.log(userId, "ROLE_REMOVE", "Removed roleId=" + roleId, com.rbac.model.AuditLog.Status.SUCCESS);
    }

    /** Find a user by id (optional) */
    public Optional<User> findById(Long id) {
        try {
            return Optional.ofNullable(userRepository.findById(id));
        } catch (Exception e) {
            return Optional.empty();
        }
    }
}
