package com.quantumsoft.hrms.Human_Resource_Website.serviceImpl;

import com.quantumsoft.hrms.Human_Resource_Website.entity.Compliance;
import com.quantumsoft.hrms.Human_Resource_Website.exception.ResourceNotFoundException;
import com.quantumsoft.hrms.Human_Resource_Website.repository.ComplianceRepository;
import com.quantumsoft.hrms.Human_Resource_Website.service.ComplianceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ComplianceServiceImpl implements ComplianceService {

    @Autowired
    private ComplianceRepository complianceRepository;

    @Override
    public Compliance createCompliance(Compliance compliance) {
        compliance.setCreatedAt(LocalDateTime.now());
        compliance.setUpdatedAt(LocalDateTime.now());
        return complianceRepository.save(compliance);
    }

    @Override
    public List<Compliance> getAllCompliances() {
        return complianceRepository.findAll();
    }

    @Override
    public Compliance updateCompliance(Long id, Compliance complianceInput) {
        Compliance compliance = complianceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Compliance not found"));

        compliance.setName(complianceInput.getName());
        compliance.setDescription(complianceInput.getDescription());
        compliance.setType(complianceInput.getType());
        compliance.setFrequency(complianceInput.getFrequency());
        compliance.setDueDate(complianceInput.getDueDate());
        compliance.setPenalty(complianceInput.getPenalty());
        compliance.setDocumentRequired(complianceInput.getDocumentRequired());
        compliance.setIsActive(complianceInput.getIsActive());
        compliance.setUpdatedAt(LocalDateTime.now());

        return complianceRepository.save(compliance);
    }

    public Compliance getComplianceById(Long id) {
        return complianceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Compliance not found with id: " + id));
    }

    @Override
    public void deactivateCompliance(Long id) {
        Compliance compliance = complianceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Compliance not found"));
        compliance.setIsActive(false);
        compliance.setUpdatedAt(LocalDateTime.now());
        complianceRepository.save(compliance);
    }
}
