package com.quantumsoft.hrms.Human_Resource_Website.service;

import com.quantumsoft.hrms.Human_Resource_Website.entity.Compliance;

import java.util.List;

public interface ComplianceService {
    Compliance createCompliance(Compliance compliance);
    List<Compliance> getAllCompliances();
    Compliance updateCompliance(Long id, Compliance compliance);
    void deactivateCompliance(Long id);
    Compliance getComplianceById(Long complianceId);
}
