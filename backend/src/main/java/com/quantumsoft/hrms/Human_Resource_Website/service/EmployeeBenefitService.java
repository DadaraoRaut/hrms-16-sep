package com.quantumsoft.hrms.Human_Resource_Website.service;

import com.quantumsoft.hrms.Human_Resource_Website.entity.Employee;
import com.quantumsoft.hrms.Human_Resource_Website.entity.EmployeeBenefit;
import com.quantumsoft.hrms.Human_Resource_Website.enums.Role;

import java.util.List;
import java.util.UUID;

public interface EmployeeBenefitService {

    EmployeeBenefit assignBenefit(EmployeeBenefit employeeBenefit);

    EmployeeBenefit updateEmployeeBenefit(Long id, EmployeeBenefit employeeBenefit);

    List<EmployeeBenefit> getBenefitsByEmployeeId(Long employeeId);

    void deleteEmployeeBenefit(Long id);

    void softDeleteEmployeeBenefit(Long id);


}
