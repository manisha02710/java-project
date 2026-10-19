package com.rbac.service;

import com.rbac.model.Permission;
import com.rbac.repository.PermissionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service handling permission‑related business logic.
 * // OOP: Encapsulation – service isolates repository usage.
 */
@Service
public class PermissionService {

    private final PermissionRepository permissionRepository;
    private final AuditService auditService;

    public PermissionService(PermissionRepository permissionRepository, AuditService auditService) {
        this.permissionRepository = permissionRepository;
        this.auditService = auditService;
    }

    /** List all permissions in the system */
    public List<Permission> listAll() {
        return permissionRepository.findAll();
    }

    /** Assign a permission to a role */
    @Transactional
    public void assignPermission(Long roleId, Long permissionId, Long operatorId) {
        permissionRepository.assignPermissionToRole(roleId, permissionId);
        auditService.log(operatorId, "PERMISSION_ASSIGN", "Assigned permissionId=" + permissionId + " to roleId=" + roleId, com.rbac.model.AuditLog.Status.SUCCESS);
    }

    /** Remove a permission from a role */
    @Transactional
    public void removePermission(Long roleId, Long permissionId, Long operatorId) {
        permissionRepository.removePermissionFromRole(roleId, permissionId);
        auditService.log(operatorId, "PERMISSION_REMOVE", "Removed permissionId=" + permissionId + " from roleId=" + roleId, com.rbac.model.AuditLog.Status.SUCCESS);
    }
}
