package com.rbac.repository;

import com.rbac.model.Attendance;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Repository for {@link Attendance} entities using JdbcTemplate.
 * // OOP: Encapsulation – private JdbcTemplate.
 */
@Repository // OOP: Abstraction
public class AttendanceRepository {

    private final JdbcTemplate jdbcTemplate;

    public AttendanceRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private static final RowMapper<Attendance> ATTENDANCE_MAPPER = new RowMapper<Attendance>() {
        @Override
        public Attendance mapRow(ResultSet rs, int rowNum) throws SQLException {
            Attendance a = new Attendance();
            a.setId(rs.getLong("attendance_id"));
            a.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
            a.setStudentId(rs.getLong("student_id"));
            a.setSubject(rs.getString("subject"));
            a.setTotalClasses(rs.getInt("total_classes"));
            a.setAttendedClasses(rs.getInt("attended_classes"));
            return a;
        }
    };

    /** Find all attendance records for a student */
    public List<Attendance> findByStudentId(Long studentId) {
        String sql = "SELECT * FROM attendance WHERE student_id = ?";
        return jdbcTemplate.query(sql, ATTENDANCE_MAPPER, studentId);
    }

    /** Insert a new attendance record */
    public Long save(Attendance attendance) {
        String sql = "INSERT INTO attendance (student_id, subject, total_classes, attended_classes) VALUES (?, ?, ?, ?)";
        try {
            jdbcTemplate.update(sql, attendance.getStudentId(), attendance.getSubject(), attendance.getTotalClasses(), attendance.getAttendedClasses());
        } catch (DuplicateKeyException e) {
            throw e;
        }
        return jdbcTemplate.queryForObject("SELECT LAST_INSERT_ID()", Long.class);
    }

    /** Update an existing attendance record */
    public int update(Attendance attendance) {
        String sql = "UPDATE attendance SET subject = ?, total_classes = ?, attended_classes = ? WHERE attendance_id = ?";
        return jdbcTemplate.update(sql, attendance.getSubject(), attendance.getTotalClasses(), attendance.getAttendedClasses(), attendance.getId());
    }

    /** Delete an attendance record */
    public int delete(Long attendanceId) {
        String sql = "DELETE FROM attendance WHERE attendance_id = ?";
        return jdbcTemplate.update(sql, attendanceId);
    }
}
