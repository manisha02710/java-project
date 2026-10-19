package com.rbac.service;

import com.rbac.model.Student;
import com.rbac.model.Mark;
import com.rbac.model.Attendance;
import com.rbac.repository.StudentRepository;
import com.rbac.repository.MarkRepository;
import com.rbac.repository.AttendanceRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

/**
 * Business logic for student actions (own data only).
 * // OOP: Encapsulation – services hide repository details.
 */
@Service
public class StudentService {

    private final StudentRepository studentRepository;
    private final MarkRepository markRepository;
    private final AttendanceRepository attendanceRepository;
    private final AuditService auditService;

    public StudentService(StudentRepository studentRepository,
                          MarkRepository markRepository,
                          AttendanceRepository attendanceRepository,
                          AuditService auditService) {
        this.studentRepository = studentRepository;
        this.markRepository = markRepository;
        this.attendanceRepository = attendanceRepository;
        this.auditService = auditService;
    }

    /** Find the Student entity that belongs to the given userId */
    public Student findStudentByUserId(Long userId) {
        return studentRepository.findByUserId(userId);
    }

    /** Get marks for the student (own) */
    public List<Mark> getOwnMarks(Long studentId) {
        return markRepository.findByStudentId(studentId);
    }

    /** Get attendance for the student (own) */
    public List<Attendance> getOwnAttendance(Long studentId) {
        return attendanceRepository.findByStudentId(studentId);
    }

    /** Update own profile details (e.g., full name, email) – demonstration */
    @Transactional
    public void updateProfile(Long studentId, String fullName, String email) {
        // In a real app you would also update the linked User record; omitted for brevity.
        // Here we just log the action.
        auditService.log(null, "OWN_PROFILE_UPDATE", "StudentId=" + studentId, com.rbac.model.AuditLog.Status.SUCCESS);
    }
}
