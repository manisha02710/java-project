package com.rbac.repository;

import com.rbac.model.Mark;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Repository for {@link Mark} entities using JdbcTemplate.
 * // OOP: Encapsulation – private JdbcTemplate.
 */
@Repository // OOP: Abstraction
public class MarkRepository {

    private final JdbcTemplate jdbcTemplate;

    public MarkRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private static final RowMapper<Mark> MARK_MAPPER = new RowMapper<Mark>() {
        @Override
        public Mark mapRow(ResultSet rs, int rowNum) throws SQLException {
            Mark m = new Mark();
            m.setId(rs.getLong("mark_id"));
            m.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
            m.setStudentId(rs.getLong("student_id"));
            m.setSubject(rs.getString("subject"));
            m.setMarks(rs.getInt("marks"));
            return m;
        }
    };

    /** Find all marks for a given student */
    public List<Mark> findByStudentId(Long studentId) {
        String sql = "SELECT * FROM marks WHERE student_id = ?";
        return jdbcTemplate.query(sql, MARK_MAPPER, studentId);
    }

    /** Insert a new mark */
    public Long save(Mark mark) {
        String sql = "INSERT INTO marks (student_id, subject, marks) VALUES (?, ?, ?)";
        try {
            jdbcTemplate.update(sql, mark.getStudentId(), mark.getSubject(), mark.getMarks());
        } catch (DuplicateKeyException e) {
            throw e;
        }
        return jdbcTemplate.queryForObject("SELECT LAST_INSERT_ID()", Long.class);
    }

    /** Update an existing mark (by primary key) */
    public int update(Mark mark) {
        String sql = "UPDATE marks SET subject = ?, marks = ? WHERE mark_id = ?";
        return jdbcTemplate.update(sql, mark.getSubject(), mark.getMarks(), mark.getId());
    }

    /** Delete a mark */
    public int delete(Long markId) {
        String sql = "DELETE FROM marks WHERE mark_id = ?";
        return jdbcTemplate.update(sql, markId);
    }
}
