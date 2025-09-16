package com.quantumsoft.hrms.Human_Resource_Website.repository;

import com.quantumsoft.hrms.Human_Resource_Website.entity.EmployeeBenefit;
import com.quantumsoft.hrms.Human_Resource_Website.enums.BenefitType;
import com.quantumsoft.hrms.Human_Resource_Website.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeBenefitRepository extends JpaRepository<EmployeeBenefit, Long> {

    List<EmployeeBenefit> findByEmployee_EmpId(Long employeeId);

    List<EmployeeBenefit> findByEmployee_EmpIdAndBenefit_TypeAndStatus(Long employeeId, BenefitType benefitType, Status status);
}
