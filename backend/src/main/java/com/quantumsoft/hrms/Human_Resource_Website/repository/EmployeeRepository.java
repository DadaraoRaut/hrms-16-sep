package com.quantumsoft.hrms.Human_Resource_Website.repository;

import com.quantumsoft.hrms.Human_Resource_Website.entity.Department;
import com.quantumsoft.hrms.Human_Resource_Website.entity.Employee;
import com.quantumsoft.hrms.Human_Resource_Website.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface EmployeeRepository extends JpaRepository <Employee, Long> {
    Optional<Employee> findByEmail(String email);

    Optional<Employee> findById(Long employeeId);

//    List<Employee> findByEmailIn(List<String> userEmails);

    List<Employee> findByUserRole(Role role);

    List<Employee> findByDepartment(Department department);

    @Query("SELECT e FROM Employee e WHERE e.empId = :id")
    Optional<Employee> findHeadById(@Param("id") Long id);
}
