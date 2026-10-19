package com.rbac.model;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Role entity representing a set of permissions.
 * // OOP: Inheritance – extends BaseEntity.
 */
public class Role extends BaseEntity {
    // OOP: Encapsulation
    private String roleName;
    private String description;
    // Store permission IDs for simplicity
    private List<Long> permissionIds; // OOP: Polymorphism when loading Permission objects

    public Role() {
        super();
    }

    public Role(Long id, LocalDateTime createdAt, String roleName, String description, List<Long> permissionIds) {
        super(id, createdAt);
        this.roleName = roleName;
        this.description = description;
        this.permissionIds = permissionIds;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Long> getPermissionIds() {
        return permissionIds;
    }

    public void setPermissionIds(List<Long> permissionIds) {
        this.permissionIds = permissionIds;
    }
}
