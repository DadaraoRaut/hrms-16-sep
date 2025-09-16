package com.quantumsoft.hrms.Human_Resource_Website.service;

import com.quantumsoft.hrms.Human_Resource_Website.entity.Employee;
import com.quantumsoft.hrms.Human_Resource_Website.enums.Role;
import jakarta.validation.Valid;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

public interface EmployeeService {
    Employee createEmployee(Employee employee, MultipartFile photo,  String email);

    Employee updateEmployee(Long empId, @Valid Employee employee, MultipartFile photo);

    Employee getEmployeeById(Long id);

    void deleteEmployee(Long empId);

    List<Employee> getEmployeesByRoleManager(Role role);

    List<Employee> findAllEmployee();
}
