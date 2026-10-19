package com.rbac.model;

import java.time.LocalDateTime;

/**
 * Attendance entity representing class attendance for a student.
 * // OOP: Inheritance – extends BaseEntity.
 */
public class Attendance extends BaseEntity {
    // OOP: Encapsulation
    private Long studentId;
    private String subject;
    private Integer totalClasses;
    private Integer attendedClasses;

    public Attendance() {
        super();
    }

    public Attendance(Long id, LocalDateTime createdAt, Long studentId, String subject, Integer totalClasses, Integer attendedClasses) {
        super(id, createdAt);
        this.studentId = studentId;
        this.subject = subject;
        this.totalClasses = totalClasses;
        this.attendedClasses = attendedClasses;
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

    public Integer getTotalClasses() {
        return totalClasses;
    }

    public void setTotalClasses(Integer totalClasses) {
        this.totalClasses = totalClasses;
    }

    public Integer getAttendedClasses() {
        return attendedClasses;
    }

    public void setAttendedClasses(Integer attendedClasses) {
        this.attendedClasses = attendedClasses;
    }
}
