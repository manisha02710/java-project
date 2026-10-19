package com.rbac.model;

import java.time.LocalDateTime;

/**
 * Permission entity representing a single permission.
 * // OOP: Inheritance – extends BaseEntity.
 */
public class Permission extends BaseEntity {
    // OOP: Encapsulation
    private String permissionName;
    private String description;

    public Permission() {
        super();
    }

    public Permission(Long id, LocalDateTime createdAt, String permissionName, String description) {
        super(id, createdAt);
        this.permissionName = permissionName;
        this.description = description;
    }

    public String getPermissionName() {
        return permissionName;
    }

    public void setPermissionName(String permissionName) {
        this.permissionName = permissionName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
