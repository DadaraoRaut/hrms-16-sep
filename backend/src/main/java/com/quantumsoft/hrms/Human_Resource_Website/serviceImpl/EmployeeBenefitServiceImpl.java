package com.quantumsoft.hrms.Human_Resource_Website.serviceImpl;

import com.quantumsoft.hrms.Human_Resource_Website.controller.NotificationController;
import com.quantumsoft.hrms.Human_Resource_Website.entity.Employee;
import com.quantumsoft.hrms.Human_Resource_Website.entity.EmployeeBenefit;
import com.quantumsoft.hrms.Human_Resource_Website.enums.BenefitType;
import com.quantumsoft.hrms.Human_Resource_Website.enums.Role;
import com.quantumsoft.hrms.Human_Resource_Website.enums.Status;
import com.quantumsoft.hrms.Human_Resource_Website.repository.EmployeeBenefitRepository;
import com.quantumsoft.hrms.Human_Resource_Website.service.EmployeeBenefitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;


@Service
public class EmployeeBenefitServiceImpl implements EmployeeBenefitService {

    @Autowired
    private EmployeeBenefitRepository employeeBenefitRepository;

    @Autowired
    private NotificationController notificationController;


    @Override
    public EmployeeBenefit assignBenefit(EmployeeBenefit employeeBenefit) {
        Long employeeId = employeeBenefit.getEmployee().getEmpId();
        BenefitType benefitType = employeeBenefit.getBenefit().getType();

        List<EmployeeBenefit> existingBenefits = employeeBenefitRepository
                .findByEmployee_EmpIdAndBenefit_TypeAndStatus(employeeId, benefitType, Status.ACTIVE);

        for (EmployeeBenefit existing : existingBenefits) {
            existing.setEffectiveTo(LocalDateTime.now());
            existing.setStatus(Status.INACTIVE);
            employeeBenefitRepository.save(existing);
        }

        EmployeeBenefit savedBenefit = employeeBenefitRepository.save(employeeBenefit);

        String message = "New benefit assigned to Employee ID: " + savedBenefit.getEmployee().getEmpId();
        notificationController.sendBenefitAssignedNotification(message);


        return savedBenefit;
    }

    @Override
    public EmployeeBenefit updateEmployeeBenefit(Long id, EmployeeBenefit employeeBenefit) {
        employeeBenefit.setEmployeeBenefitId(id);
        return employeeBenefitRepository.save(employeeBenefit);
    }

    @Override
    public List<EmployeeBenefit> getBenefitsByEmployeeId(Long employeeId) {
        return employeeBenefitRepository.findByEmployee_EmpId(employeeId);
    }

    @Override
    public void deleteEmployeeBenefit(Long id) {
        employeeBenefitRepository.deleteById(id);
    }

    @Override
    public void softDeleteEmployeeBenefit(Long id) {
        EmployeeBenefit benefit = employeeBenefitRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("EmployeeBenefit not found with id: " + id));

        benefit.setStatus(Status.INACTIVE);
        employeeBenefitRepository.save(benefit);
    }


}
