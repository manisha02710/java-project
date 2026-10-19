package com.rbac.repository;

import com.rbac.model.Permission;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Repository for {@link Permission} using JdbcTemplate.
 * // OOP: Encapsulation – private fields accessed via methods.
 */
@Repository // OOP: Abstraction
public class PermissionRepository {

    private final JdbcTemplate jdbcTemplate;

    public PermissionRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private static final RowMapper<Permission> PERM_MAPPER = new RowMapper<Permission>() {
        @Override
        public Permission mapRow(ResultSet rs, int rowNum) throws SQLException {
            Permission perm = new Permission();
            perm.setId(rs.getLong("permission_id"));
            perm.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
            perm.setPermissionName(rs.getString("permission_name"));
            perm.setDescription(rs.getString("description"));
            return perm;
        }
    };

    /** Find all permissions. */
    public List<Permission> findAll() {
        String sql = "SELECT * FROM permissions";
        return jdbcTemplate.query(sql, PERM_MAPPER);
    }

    /** Find permissions assigned to a specific role. */
    public List<Permission> findPermissionsByRoleId(Long roleId) {
        String sql = "SELECT p.* FROM permissions p " +
                     "JOIN role_permissions rp ON p.permission_id = rp.permission_id " +
                     "WHERE rp.role_id = ?";
        return jdbcTemplate.query(sql, PERM_MAPPER, roleId);
    }

    /** Find permission names (String) that a user has via role assignments. */
    public List<String> findPermissionNamesByUserId(Long userId) {
        String sql = "SELECT DISTINCT p.permission_name " +
                     "FROM permissions p " +
                     "JOIN role_permissions rp ON p.permission_id = rp.permission_id " +
                     "JOIN user_roles ur ON rp.role_id = ur.role_id " +
                     "WHERE ur.user_id = ?";
        return jdbcTemplate.queryForList(sql, String.class, userId);
    }

    /** Assign a permission to a role. */
    public int assignPermissionToRole(Long roleId, Long permissionId) {
        String sql = "INSERT INTO role_permissions (role_id, permission_id) VALUES (?, ?)";
        try {
            return jdbcTemplate.update(sql, roleId, permissionId);
        } catch (DuplicateKeyException e) {
            // To be wrapped later
            throw e;
        }
    }

    /** Remove a permission from a role. */
    public int removePermissionFromRole(Long roleId, Long permissionId) {
        String sql = "DELETE FROM role_permissions WHERE role_id = ? AND permission_id = ?";
        return jdbcTemplate.update(sql, roleId, permissionId);
    }
}
