package com.rbac.interceptor;

import com.rbac.annotation.RequiresPermission;
import com.rbac.exception.AccessDeniedException;
import com.rbac.model.AuditLog;
import com.rbac.model.SessionUser;
import com.rbac.repository.AuditLogRepository;
import com.rbac.service.AuthorizationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * Interceptor that enforces authentication and permission checks.
 * // OOP: Abstraction – implements HandlerInterceptor interface.
 */
@Component
public class AuthInterceptor implements HandlerInterceptor {

    private final AuthorizationService authorizationService;
    private final AuditLogRepository auditLogRepository;

    public AuthInterceptor(AuthorizationService authorizationService,
                           AuditLogRepository auditLogRepository) {
        this.authorizationService = authorizationService;
        this.auditLogRepository = auditLogRepository;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession(false);
        SessionUser sessionUser = (session != null) ? (SessionUser) session.getAttribute("sessionUser") : null;

        // 1. No session -> redirect to login
        if (sessionUser == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return false;
        }

        // 2. If handler is a controller method, check @RequiresPermission
        if (handler instanceof HandlerMethod) {
            HandlerMethod hm = (HandlerMethod) handler;
            RequiresPermission rp = hm.getMethodAnnotation(RequiresPermission.class);
            if (rp != null) {
                String required = rp.value();
                boolean allowed = authorizationService.hasPermission(sessionUser.getUserId().intValue(), required);
                if (!allowed) {
                    // Log audit entry (DENIED)
                    AuditLog log = new AuditLog();
                    log.setUserId(sessionUser.getUserId());
                    log.setAction("ACCESS_DENIED");
                    log.setDetails("Attempted to access method " + hm.getMethod().getName() + " without permission " + required);
                    log.setStatus(AuditLog.Status.DENIED);
                    auditLogRepository.save(log);
                    // Throw exception to be handled globally
                    throw new AccessDeniedException("You do not have permission to perform this action.");
                }
            }
        }
        // All good – continue processing
        return true;
    }
}
