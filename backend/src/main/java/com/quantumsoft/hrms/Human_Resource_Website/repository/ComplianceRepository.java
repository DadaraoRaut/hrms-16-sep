package com.quantumsoft.hrms.Human_Resource_Website.repository;

import com.quantumsoft.hrms.Human_Resource_Website.entity.Compliance;
import com.quantumsoft.hrms.Human_Resource_Website.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ComplianceRepository extends JpaRepository<Compliance, Long> {

//    List<Compliance> findByEmployee(Employee employee);
//
//    List<Compliance> findByEmployeeIsNull();
//
//    List<Compliance> findByEmployee_Id(Long employeeId);
}
