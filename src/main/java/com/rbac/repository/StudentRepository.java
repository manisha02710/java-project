package com.rbac.repository;

import com.rbac.model.Student;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Repository for {@link Student} entities using JdbcTemplate.
 * // OOP: Encapsulation – private JdbcTemplate field.
 */
@Repository // OOP: Abstraction – Spring hides implementation details.
public class StudentRepository {

    private final JdbcTemplate jdbcTemplate;

    public StudentRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private static final RowMapper<Student> STUDENT_MAPPER = new RowMapper<Student>() {
        @Override
        public Student mapRow(ResultSet rs, int rowNum) throws SQLException {
            Student s = new Student();
            s.setId(rs.getLong("student_id"));
            s.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
            s.setUserId(rs.getLong("user_id"));
            s.setRollNo(rs.getString("roll_no"));
            s.setDepartment(rs.getString("department"));
            return s;
        }
    };

    /** Find all students */
    public List<Student> findAll() {
        String sql = "SELECT * FROM students";
        return jdbcTemplate.query(sql, STUDENT_MAPPER);
    }

    /** Find a student by its primary key */
    public Student findById(Long studentId) {
        String sql = "SELECT * FROM students WHERE student_id = ?";
        return jdbcTemplate.queryForObject(sql, STUDENT_MAPPER, studentId);
    }

    /** Find a student by the linked user_id (one‑to‑one) */
    public Student findByUserId(Long userId) {
        String sql = "SELECT * FROM students WHERE user_id = ?";
        return jdbcTemplate.queryForObject(sql, STUDENT_MAPPER, userId);
    }

    /** Insert a new student record */
    public Long save(Student student) {
        String sql = "INSERT INTO students (user_id, roll_no, department) VALUES (?, ?, ?)";
        try {
            jdbcTemplate.update(sql, student.getUserId(), student.getRollNo(), student.getDepartment());
        } catch (DuplicateKeyException e) {
            throw e;
        }
        return jdbcTemplate.queryForObject("SELECT LAST_INSERT_ID()", Long.class);
    }
}
