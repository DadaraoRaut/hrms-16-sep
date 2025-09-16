package com.quantumsoft.hrms.Human_Resource_Website.repository;

import com.quantumsoft.hrms.Human_Resource_Website.entity.BankDetail;
import com.quantumsoft.hrms.Human_Resource_Website.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BankDetailRepository extends JpaRepository<BankDetail, Long> {
    List<BankDetail> findByEmployee(Employee employee);
}
