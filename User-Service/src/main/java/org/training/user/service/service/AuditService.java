package org.training.user.service.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.training.user.service.model.entity.AuditLog;
import org.training.user.service.repository.AuditLogRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuditService {

    private final AuditLogRepository auditLogRepository;

    public void logAuthenticationEvent(String eventType, String authId, Long userId, 
                                       String ipAddress, String userAgent, 
                                       Boolean success, String details) {
        try {
            AuditLog auditLog = AuditLog.builder()
                    .eventType(eventType)
                    .authId(authId)
                    .userId(userId)
                    .ipAddress(ipAddress)
                    .userAgent(userAgent)
                    .success(success)
                    .details(details)
                    .build();
            
            auditLogRepository.save(auditLog);
            log.info("Audit log created: eventType={}, authId={}, success={}", 
                    eventType, authId, success);
        } catch (Exception e) {
            log.error("Failed to create audit log", e);
        }
    }

    public void logUserCreation(String authId, String email, Boolean success) {
        logAuthenticationEvent("USER_REGISTRATION", authId, null, null, null, success, 
                "User registration: " + email);
    }

    public void logStatusChange(Long userId, String authId, String oldStatus, String newStatus) {
        logAuthenticationEvent("USER_STATUS_CHANGE", authId, userId, null, null, true, 
                String.format("Status changed from %s to %s", oldStatus, newStatus));
    }

    public void logPasswordChange(String authId, Long userId, Boolean success) {
        logAuthenticationEvent("PASSWORD_CHANGE", authId, userId, null, null, success, 
                "Password change attempt");
    }
}
