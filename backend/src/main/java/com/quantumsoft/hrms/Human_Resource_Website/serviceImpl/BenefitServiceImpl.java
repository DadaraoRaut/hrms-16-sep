package com.quantumsoft.hrms.Human_Resource_Website.serviceImpl;

import com.quantumsoft.hrms.Human_Resource_Website.entity.Benefit;
import com.quantumsoft.hrms.Human_Resource_Website.entity.User;
import com.quantumsoft.hrms.Human_Resource_Website.exception.ResourceNotFoundException;
import com.quantumsoft.hrms.Human_Resource_Website.repository.BenefitRepository;
import com.quantumsoft.hrms.Human_Resource_Website.repository.UserRepository;
import com.quantumsoft.hrms.Human_Resource_Website.service.AuditLogService;
import com.quantumsoft.hrms.Human_Resource_Website.service.BenefitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BenefitServiceImpl implements BenefitService {

    @Autowired
    private BenefitRepository benefitRepository;

    @Autowired
    private AuditLogService auditLogService;

    @Autowired
    private UserRepository userRepository;

    @Override
    public Benefit createBenefit(Benefit benefit) {
        if (benefitRepository.existsByName(benefit.getName())) {
            throw new IllegalArgumentException("Benefit with the same name already exists");
        }
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        auditLogService.logEvent(currentUsername, "CREATE BENEFIT");
        return benefitRepository.save(benefit);
    }

    @Override
    public Benefit updateBenefit(Long id, Benefit benefit) {

        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        benefit.setBenefitId(id);
        auditLogService.logEvent(currentUsername, "UPDATE BENEFIT");
        return benefitRepository.save(benefit);
    }

    @Override
    public List<Benefit> getAllBenefits() {
        return benefitRepository.findAll();
    }

    @Override
    public void deleteBenefit(Long id) {
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        auditLogService.logEvent(currentUsername, "DELETE BENEFIT");
        benefitRepository.deleteById(id);
    }

}
