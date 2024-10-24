package com.meusprojetos.taskmanagementsystem.repository;

import com.meusprojetos.taskmanagementsystem.model.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {
}