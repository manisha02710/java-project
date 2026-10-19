package com.rbac.model;

import java.util.Set;

/**
 * A lightweight object stored in HttpSession after successful login.
 * // OOP: Encapsulation – fields are private with getters.
 */
public class SessionUser {
    private final Long userId;
    private final String username;
    private final String fullName;
    private final Set<String> permissions;

    public SessionUser(Long userId, String username, String fullName, Set<String> permissions) {
        this.userId = userId;
        this.username = username;
        this.fullName = fullName;
        this.permissions = permissions;
    }

    public Long getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public String getFullName() {
        return fullName;
    }

    public Set<String> getPermissions() {
        return permissions;
    }
}
