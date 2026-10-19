package com.rbac.model;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Represents an application user.
 * // OOP: Inheritance – extends BaseEntity to reuse id and createdAt.
 */
public class User extends BaseEntity {
    // OOP: Encapsulation – private fields with getters/setters.
    private String username;
    private String passwordHash;
    private String fullName;
    private String email;
    private Boolean isActive;
    // For simplicity we store role ids; a real app would use a collection of Role objects.
    private List<Long> roleIds; // OOP: Polymorphism can be applied when loading roles.

    public User() {
        super();
    }

    public User(Long id, LocalDateTime createdAt, String username, String passwordHash,
                String fullName, String email, Boolean isActive, List<Long> roleIds) {
        super(id, createdAt);
        this.username = username;
        this.passwordHash = passwordHash;
        this.fullName = fullName;
        this.email = email;
        this.isActive = isActive;
        this.roleIds = roleIds;
    }

    // Getters & Setters
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public List<Long> getRoleIds() {
        return roleIds;
    }

    public void setRoleIds(List<Long> roleIds) {
        this.roleIds = roleIds;
    }
}
