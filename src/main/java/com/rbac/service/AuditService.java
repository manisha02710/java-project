package com.rbac.service;

import com.rbac.model.AuditLog;
import com.rbac.repository.AuditLogRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service responsible for persisting audit log entries.
 * // OOP: Abstraction – simple façade over the repository.
 */
@Service
public class AuditService {

    private final AuditLogRepository auditLogRepository;

    public AuditService(AuditLogRepository auditLogRepository) {
        this.auditLogRepository = auditLogRepository;
    }

    /**
     * Record an audit event.
     * @param userId   ID of the user performing the action (nullable for system actions).
     * @param action   High‑level action name.
     * @param details  Free‑form details.
     * @param status   SUCCESS or DENIED.
     */
    @Transactional // OOP: Polymorphism – transaction management is handled by Spring.
    public void log(Long userId, String action, String details, AuditLog.Status status) {
        AuditLog log = new AuditLog();
        log.setUserId(userId);
        log.setAction(action);
        log.setDetails(details);
        log.setStatus(status);
        auditLogRepository.save(log);
    }
}
