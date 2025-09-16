package com.quantumsoft.hrms.Human_Resource_Website.service;

import com.quantumsoft.hrms.Human_Resource_Website.entity.Department;

import java.util.List;

public interface DepartmentService {

    List<Department> findAllDepartment();

    Department findDepartmentById(Long id);

    Department createDepartment(Department department);

    Department updateDepartment(Long DepartmentId, Department dept);

    void deleteDepartment(Long DepartmentId);

}
