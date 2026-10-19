package com.rbac.model;

import java.time.LocalDateTime;

/**
 * OOP Concept: Inheritance – Base class for common entity attributes.
 * OOP Concept: Encapsulation – fields are private with getters/setters.
 */
public abstract class BaseEntity {

    /** Primary key identifier. */
    private Integer id;

    /** Timestamp of creation. */
    private LocalDateTime createdAt;

    public BaseEntity() {
        this.createdAt = LocalDateTime.now();
    }

    public BaseEntity(Integer id, LocalDateTime createdAt) {
        this.id = id;
        this.createdAt = createdAt != null ? createdAt : LocalDateTime.now();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
