package com.rbac.service;

import com.rbac.model.Role;
import com.rbac.repository.RoleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service containing business logic for role management.
 * // OOP: Encapsulation – only expose high‑level operations.
 */
@Service
public class RoleService {

    private final RoleRepository roleRepository;
    private final AuditService auditService;

    public RoleService(RoleRepository roleRepository, AuditService auditService) {
        this.roleRepository = roleRepository;
        this.auditService = auditService;
    }

    public List<Role> listRoles() {
        return roleRepository.findAll();
    }

    @Transactional
    public Role createRole(String roleName, String description, Long creatorId) {
        Role role = new Role();
        role.setRoleName(roleName);
        role.setDescription(description);
        Long id = roleRepository.save(role);
        role.setId(id);
        auditService.log(creatorId, "ROLE_CREATE", "Created role " + roleName, com.rbac.model.AuditLog.Status.SUCCESS);
        return role;
    }

    public List<com.rbac.model.Permission> listPermissionsForRole(Long roleId) {
        return roleRepository.findPermissionsByRoleId(roleId);
    }

    @Transactional
    public void assignPermission(Long roleId, Long permissionId, Long operatorId) {
        roleRepository.assignPermissionToRole(roleId, permissionId);
        auditService.log(operatorId, "PERMISSION_ASSIGN", "Assigned permissionId=" + permissionId + " to roleId=" + roleId, com.rbac.model.AuditLog.Status.SUCCESS);
    }

    @Transactional
    public void removePermission(Long roleId, Long permissionId, Long operatorId) {
        roleRepository.removePermissionFromRole(roleId, permissionId);
        auditService.log(operatorId, "PERMISSION_REMOVE", "Removed permissionId=" + permissionId + " from roleId=" + roleId, com.rbac.model.AuditLog.Status.SUCCESS);
    }
}
