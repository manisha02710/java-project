package com.rbac.service;

import com.rbac.exception.InvalidCredentialsException;
import com.rbac.model.SessionUser;
import com.rbac.model.User;
import com.rbac.repository.PermissionRepository;
import com.rbac.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Service handling authentication logic.
 * // OOP: Encapsulation – private dependencies and method.
 */
@Service // OOP: Abstraction – Spring hides wiring details
public class AuthService {

    private final UserRepository userRepository;
    private final PermissionRepository permissionRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(); // OOP: Polymorphism – BCryptPasswordEncoder implements PasswordEncoder

    public AuthService(UserRepository userRepository, PermissionRepository permissionRepository) {
        this.userRepository = userRepository;
        this.permissionRepository = permissionRepository;
    }

    /**
     * Authenticate a user and return a {@link SessionUser} ready for HttpSession storage.
     * Throws {@link InvalidCredentialsException} for any failure (unknown user,
     * disabled account, wrong password). The message never reveals the cause.
     */
    public SessionUser authenticate(String username, String rawPassword) {
        User user = userRepository.findByUsername(username);
        if (user == null || Boolean.FALSE.equals(user.getIsActive())) {
            // Do not disclose whether username exists or account is disabled
            throw new InvalidCredentialsException();
        }
        if (!passwordEncoder.matches(rawPassword, user.getPasswordHash())) {
            throw new InvalidCredentialsException();
        }
        // Gather permissions via joins (user -> roles -> permissions)
        List<String> permNames = permissionRepository.findPermissionNamesByUserId(user.getId());
        Set<String> perms = new HashSet<>(permNames);
        return new SessionUser(user.getId(), user.getUsername(), user.getFullName(), perms);
    }
}
