package com.quantumsoft.hrms.Human_Resource_Website.serviceImpl;

import com.quantumsoft.hrms.Human_Resource_Website.entity.Employee;
import com.quantumsoft.hrms.Human_Resource_Website.entity.SalaryStructure;
import com.quantumsoft.hrms.Human_Resource_Website.enums.Status;
import com.quantumsoft.hrms.Human_Resource_Website.repository.EmployeeRepository;
import com.quantumsoft.hrms.Human_Resource_Website.repository.SalaryStructureRepository;
import com.quantumsoft.hrms.Human_Resource_Website.service.SalaryStructureService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class SalaryStructureServiceImpl implements SalaryStructureService {

    @Autowired
    private SalaryStructureRepository salaryStructureRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Transactional
    @Override
    public SalaryStructure createSalaryStructure(Long employeeId, SalaryStructure newSalaryStructure) {

        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        double totalEarnings = newSalaryStructure.getBasicSalary()
                + newSalaryStructure.getHra()
                + newSalaryStructure.getSpecialAllowance()
                + newSalaryStructure.getBonus();
        double totalDeductions = newSalaryStructure.getPfDeduction() + newSalaryStructure.getTaxDeduction();

        if (totalEarnings <= totalDeductions) {
            throw new IllegalArgumentException("Total earnings must exceed total deductions.");
        }
        salaryStructureRepository.findByEmployeeAndStatus(employee, Status.ACTIVE)
                .ifPresent(existing -> {
                    existing.setEffectiveTo(LocalDate.now());
                    existing.setStatus(Status.INACTIVE);
                    salaryStructureRepository.save(existing);
                });

        newSalaryStructure.setEmployee(employee);
        newSalaryStructure.setStatus(Status.ACTIVE);

        return salaryStructureRepository.save(newSalaryStructure);
    }

    public List<SalaryStructure> getAll() {
        return salaryStructureRepository.findAll();
    }

    public List<SalaryStructure> getStructuresForEmployee(Long employeeId) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found"));
        return salaryStructureRepository.findByEmployee(employee);
    }

    public SalaryStructure getCurrentStructureForEmployee(Long employeeId) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found"));
        return (SalaryStructure) salaryStructureRepository.findByEmployeeAndStatus(employee, Status.ACTIVE)
                .orElseThrow(() -> new RuntimeException("No active salary structure found for employee"));
    }

    public void deleteStructure(Long id) {
        salaryStructureRepository.deleteById(id);
    }

    @Override
    public Optional<SalaryStructure> getActiveStructuresForEmployee(Long employeeId) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found"));
        return salaryStructureRepository.findByEmployeeAndStatus(employee, Status.ACTIVE);
    }

}
