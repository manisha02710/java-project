package com.rbac.model;

import java.time.LocalDateTime;

/**
 * AuditLog entity capturing actions performed in the system.
 * // OOP: Inheritance – extends BaseEntity to reuse id and createdAt.
 */
public class AuditLog extends BaseEntity {
    // OOP: Encapsulation – private fields with getters/setters.
    private Long userId; // nullable – system actions may have no user.
    private String action;
    private String details;
    private Status status;

    /**
     * Enumeration of possible audit outcomes.
     */
    public enum Status {
        SUCCESS,
        DENIED
    }

    public AuditLog() {
        super();
    }

    public AuditLog(Long id, LocalDateTime createdAt, Long userId, String action, String details, Status status) {
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

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
