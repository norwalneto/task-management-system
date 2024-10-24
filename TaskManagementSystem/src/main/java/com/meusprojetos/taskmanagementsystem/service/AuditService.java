package com.meusprojetos.taskmanagementsystem.service;

import com.meusprojetos.taskmanagementsystem.model.AuditLog;
import com.meusprojetos.taskmanagementsystem.repository.AuditLogRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class AuditService {
    private final AuditLogRepository auditLogRepository;

    public AuditService(AuditLogRepository auditLogRepository) {
        this.auditLogRepository = auditLogRepository;
    }

    public void logAction(String action) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        AuditLog log = new AuditLog(username, action);
        auditLogRepository.save(log);
    }
}
