package org.training.user.service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.training.user.service.model.entity.AuditLog;

import java.time.LocalDateTime;
import java.util.List;

public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {
    
    List<AuditLog> findByUserIdOrderByCreatedAtDesc(Long userId);
    
    List<AuditLog> findByAuthIdOrderByCreatedAtDesc(String authId);
    
    List<AuditLog> findByCreatedAtBetween(LocalDateTime start, LocalDateTime end);
}
