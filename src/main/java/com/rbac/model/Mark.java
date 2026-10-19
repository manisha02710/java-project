package com.rbac.model;

import java.time.LocalDateTime;

/**
 * Marks entity representing a subject score for a student.
 * // OOP: Inheritance – extends BaseEntity.
 */
public class Mark extends BaseEntity {
    // OOP: Encapsulation
    private Long studentId;
    private String subject;
    private Integer marks;

    public Mark() {
        super();
    }

    public Mark(Long id, LocalDateTime createdAt, Long studentId, String subject, Integer marks) {
        super(id, createdAt);
        this.studentId = studentId;
        this.subject = subject;
        this.marks = marks;
    }

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public Integer getMarks() {
        return marks;
    }

    public void setMarks(Integer marks) {
        this.marks = marks;
    }
}
