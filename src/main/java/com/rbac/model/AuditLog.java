package com.rbac.model;

import java.time.LocalDateTime;

/**
 * AuditLog entity representing an action performed in the system.
 * // OOP: Inheritance – extends BaseEntity.
 */
public class AuditLog extends BaseEntity {
    // OOP: Encapsulation – private fields.
    private Long userId; // Nullable: system actions may have no user.
    private String action;
    private String details;
    private AuditStatus status;

    public AuditLog() {
        super();
    }

    public AuditLog(Long id, LocalDateTime createdAt, Long userId, String action, String details, AuditStatus status) {
        super(id, createdAt);
        this.userId = userId;
        this.action = action;
        this.details = details;
        this.status = status;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public AuditStatus getStatus() {
        return status;
    }

    public void setStatus(AuditStatus status) {
        this.status = status;
    }
}
