package com.quantumsoft.hrms.Human_Resource_Website.repository;

import com.quantumsoft.hrms.Human_Resource_Website.entity.Payroll;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PayrollRepository extends JpaRepository<Payroll, Long> {

    Optional<Payroll> findByEmployee_EmpIdAndMonthAndYear(Long employee, String month, int year);
}
