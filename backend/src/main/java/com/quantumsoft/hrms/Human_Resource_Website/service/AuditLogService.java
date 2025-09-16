package com.quantumsoft.hrms.Human_Resource_Website.service;

public interface AuditLogService {

    void logEvent(String username, String action);
}
