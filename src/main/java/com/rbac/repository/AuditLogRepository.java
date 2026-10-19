package com.rbac.repository;

import com.rbac.model.AuditLog;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Repository for {@link AuditLog}.
 * // OOP: Inheritance – AuditLog extends BaseEntity.
 */
@Repository // OOP: Abstraction
public class AuditLogRepository {

    private final JdbcTemplate jdbcTemplate;

    public AuditLogRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private static final RowMapper<AuditLog> LOG_MAPPER = new RowMapper<AuditLog>() {
        @Override
        public AuditLog mapRow(ResultSet rs, int rowNum) throws SQLException {
            AuditLog log = new AuditLog();
            log.setId(rs.getLong("log_id"));
            log.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
            Long userId = rs.getObject("user_id", Long.class);
            log.setUserId(userId);
            log.setAction(rs.getString("action"));
            log.setDetails(rs.getString("details"));
            log.setStatus(AuditLog.Status.valueOf(rs.getString("status")));
            return log;
        }
    };

    /** Save a new audit log entry. */
    public Long save(AuditLog auditLog) {
        String sql = "INSERT INTO audit_logs (user_id, action, details, status) VALUES (?, ?, ?, ?)";
        try {
            jdbcTemplate.update(sql,
                    auditLog.getUserId(),
                    auditLog.getAction(),
                    auditLog.getDetails(),
                    auditLog.getStatus().name());
        } catch (DuplicateKeyException e) {
            // Will be wrapped later
            throw e;
        }
        return jdbcTemplate.queryForObject("SELECT LAST_INSERT_ID()", Long.class);
    }

    /** Find all audit logs ordered by newest first. */
    public List<AuditLog> findAll() {
        String sql = "SELECT * FROM audit_logs ORDER BY created_at DESC";
        return jdbcTemplate.query(sql, LOG_MAPPER);
    }
}
