package com.quantumsoft.hrms.Human_Resource_Website.serviceImpl;

import com.quantumsoft.hrms.Human_Resource_Website.entity.Department;
import com.quantumsoft.hrms.Human_Resource_Website.entity.Employee;
import com.quantumsoft.hrms.Human_Resource_Website.exception.ResourceNotFoundException;
import com.quantumsoft.hrms.Human_Resource_Website.repository.DepartmentRepository;
import com.quantumsoft.hrms.Human_Resource_Website.repository.EmployeeRepository;
import com.quantumsoft.hrms.Human_Resource_Website.service.DepartmentService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DepartmentServiceImpl implements DepartmentService {

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private EmployeeRepository employeeRepository;



    @Override
    public List<Department> findAllDepartment() {
        return departmentRepository.findAll();
    }

    @Override
    public Department findDepartmentById(Long id) {
        return departmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Department not found with id: " + id));
    }

    @Override
    public Department createDepartment(Department department) {
        if (departmentRepository.existsByDepartmentCode(department.getDepartmentCode())) {
            throw new IllegalArgumentException("Department code already exists");
        }
        return departmentRepository.save(department);
    }

    @Override
    public Department updateDepartment(Long id, Department dept) {
        Department existing = departmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("department not found with id: " + id));

        existing.setDepartmentCode(dept.getDepartmentCode());
        existing.setName(dept.getName());
        existing.setDescription(dept.getDescription());
        existing.setLocation(dept.getLocation());
        if (dept.getDepartmentHead() != null && dept.getDepartmentHead().getEmpId() != null) {
            Employee head = employeeRepository.findById(dept.getDepartmentHead().getEmpId())
                    .orElseThrow(() -> new ResourceNotFoundException("Employee not found for department head"));
            existing.setDepartmentHead(head);
        } else {
            existing.setDepartmentHead(null);
        }
        return departmentRepository.save(existing);
    }

    @Override
    @Transactional
    public void deleteDepartment(Long id) {
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Department not found with id: " + id));

        List<Employee> employees = employeeRepository.findByDepartment(department);
        for (Employee emp : employees) {
            emp.setDepartment(null);
        }
        employeeRepository.saveAll(employees);
        departmentRepository.delete(department);
    }
}
