package com.rbac.repository;

import com.rbac.model.Role;
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
 * Repository for {@link Role} using JdbcTemplate.
 * // OOP: Encapsulation – private fields accessed via methods.
 */
@Repository // OOP: Abstraction – Spring hides the implementation details.
public class RoleRepository {

    private final JdbcTemplate jdbcTemplate;

    public RoleRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private static final RowMapper<Role> ROLE_MAPPER = new RowMapper<Role>() {
        @Override
        public Role mapRow(ResultSet rs, int rowNum) throws SQLException {
            Role role = new Role();
            role.setId(rs.getLong("role_id"));
            role.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
            role.setRoleName(rs.getString("role_name"));
            role.setDescription(rs.getString("description"));
            // permissionIds can be loaded lazily elsewhere
            return role;
        }
    };

    private static final RowMapper<Permission> PERMISSION_MAPPER = new RowMapper<Permission>() {
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

    /** Find all roles. */
    public List<Role> findAll() {
        String sql = "SELECT * FROM roles";
        return jdbcTemplate.query(sql, ROLE_MAPPER);
    }

    /** Find role by its name. */
    public Role findByName(String roleName) {
        String sql = "SELECT * FROM roles WHERE role_name = ?";
        return jdbcTemplate.queryForObject(sql, ROLE_MAPPER, roleName);
    }

    /** Save a new role and return its generated ID. */
    public Long save(Role role) {
        String sql = "INSERT INTO roles (role_name, description) VALUES (?, ?)";
        try {
            jdbcTemplate.update(sql, role.getRoleName(), role.getDescription());
        } catch (DuplicateKeyException e) {
            // Will be wrapped later in a custom exception
            throw e;
        }
        return jdbcTemplate.queryForObject("SELECT LAST_INSERT_ID()", Long.class);
    }

    /** Find all roles assigned to a given user. */
    public List<Role> findRolesByUserId(Long userId) {
        String sql = "SELECT r.* FROM roles r " +
                     "JOIN user_roles ur ON r.role_id = ur.role_id " +
                     "WHERE ur.user_id = ?";
        return jdbcTemplate.query(sql, ROLE_MAPPER, userId);
    }

    /** Assign a role to a user (idempotent). */
    public int assignRoleToUser(Long userId, Long roleId) {
        String sql = "INSERT INTO user_roles (user_id, role_id) VALUES (?, ?)";
        return jdbcTemplate.update(sql, userId, roleId);
    }

    /** Remove a role from a user. */
    public int removeRoleFromUser(Long userId, Long roleId) {
        String sql = "DELETE FROM user_roles WHERE user_id = ? AND role_id = ?";
        return jdbcTemplate.update(sql, userId, roleId);
    }

    /** -------------------------------------------------------------------- */
    /** Permission‑related helpers for a role (used by the admin module). */

    /** Find permissions assigned to a specific role. */
    public List<Permission> findPermissionsByRoleId(Long roleId) {
        String sql = "SELECT p.* FROM permissions p " +
                     "JOIN role_permissions rp ON p.permission_id = rp.permission_id " +
                     "WHERE rp.role_id = ?";
        return jdbcTemplate.query(sql, PERMISSION_MAPPER, roleId);
    }

    /** Assign a permission to a role. */
    public int assignPermissionToRole(Long roleId, Long permissionId) {
        String sql = "INSERT INTO role_permissions (role_id, permission_id) VALUES (?, ?)";
        try {
            return jdbcTemplate.update(sql, roleId, permissionId);
        } catch (DuplicateKeyException e) {
            throw e; // will be wrapped later
        }
    }

    /** Remove a permission from a role. */
    public int removePermissionFromRole(Long roleId, Long permissionId) {
        String sql = "DELETE FROM role_permissions WHERE role_id = ? AND permission_id = ?";
        return jdbcTemplate.update(sql, roleId, permissionId);
    }
}
