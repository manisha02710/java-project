package com.rbac.service;

import com.rbac.model.Mark;
import com.rbac.model.Student;
import com.rbac.model.Attendance;
import com.rbac.repository.StudentRepository;
import com.rbac.repository.MarkRepository;
import com.rbac.repository.AttendanceRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

/**
 * Business logic for faculty actions.
 * // OOP: Encapsulation – service hides repository details.
 */
@Service
public class FacultyService {

    private final StudentRepository studentRepository;
    private final MarkRepository markRepository;
    private final AttendanceRepository attendanceRepository;
    private final AuditService auditService;

    public FacultyService(StudentRepository studentRepository,
                          MarkRepository markRepository,
                          AttendanceRepository attendanceRepository,
                          AuditService auditService) {
        this.studentRepository = studentRepository;
        this.markRepository = markRepository;
        this.attendanceRepository = attendanceRepository;
        this.auditService = auditService;
    }

    /** List all students – permission STUDENT_READ */
    public List<Student> listAllStudents() {
        return studentRepository.findAll();
    }

    /** Get marks for a specific student – permission MARKS_READ */
    public List<Mark> getMarksForStudent(Long studentId) {
        return markRepository.findByStudentId(studentId);
    }

    /** Add a new mark for a student – permission MARKS_ADD */
    @Transactional
    public Mark addMark(Long studentId, String subject, Integer marks, Long facultyId) {
        Mark m = new Mark();
        m.setStudentId(studentId);
        m.setSubject(subject);
        m.setMarks(marks);
        Long id = markRepository.save(m);
        m.setId(id);
        auditService.log(facultyId, "MARKS_ADD", "Added mark for studentId=" + studentId, com.rbac.model.AuditLog.Status.SUCCESS);
        return m;
    }

    /** List attendance records for a student – permission ATTENDANCE_VIEW */
    public List<Attendance> getAttendanceForStudent(Long studentId) {
        return attendanceRepository.findByStudentId(studentId);
    }

    /** Add or update attendance for a student – permission ATTENDANCE_MANAGE */
    @Transactional
    public Attendance saveAttendance(Long studentId, String subject, Integer total, Integer attended, Long facultyId) {
        // Simple implementation: try to find existing record for subject, else insert new
        List<Attendance> existing = attendanceRepository.findByStudentId(studentId);
        Attendance att = null;
        for (Attendance a : existing) {
            if (a.getSubject().equals(subject)) {
                att = a;
                break;
            }
        }
        if (att == null) {
            att = new Attendance();
            att.setStudentId(studentId);
            att.setSubject(subject);
            att.setTotalClasses(total);
            att.setAttendedClasses(attended);
            Long id = attendanceRepository.save(att);
            att.setId(id);
        } else {
            att.setTotalClasses(total);
            att.setAttendedClasses(attended);
            attendanceRepository.update(att);
        }
        auditService.log(facultyId, "ATTENDANCE_MANAGE", "Managed attendance for studentId=" + studentId, com.rbac.model.AuditLog.Status.SUCCESS);
        return att;
    }
}
