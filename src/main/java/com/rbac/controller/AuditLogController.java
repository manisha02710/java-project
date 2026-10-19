package com.rbac.controller;

import com.rbac.model.AuditLog;
import com.rbac.repository.AuditLogRepository;
import com.rbac.annotation.RequiresPermission;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Controller for viewing audit logs with optional status filter.
 * // OOP: Encapsulation – controller delegates to repository.
 */
@Controller
public class AuditLogController {

    private final AuditLogRepository auditLogRepository;

    public AuditLogController(AuditLogRepository auditLogRepository) {
        this.auditLogRepository = auditLogRepository;
    }

    @RequiresPermission("AUDIT_VIEW")
    @GetMapping("/audit")
    public String viewAudit(@RequestParam(required = false) String status, Model model) {
        List<AuditLog> logs = auditLogRepository.findAll();
        if (status != null && !status.isEmpty()) {
            logs = logs.stream()
                    .filter(l -> l.getStatus().name().equalsIgnoreCase(status))
                    .collect(Collectors.toList());
        }
        model.addAttribute("logs", logs);
        model.addAttribute("selectedStatus", status);
        return "audit"; // Thymeleaf template
    }
}
