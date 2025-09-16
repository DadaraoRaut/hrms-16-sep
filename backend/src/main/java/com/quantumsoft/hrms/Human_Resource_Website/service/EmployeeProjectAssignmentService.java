package com.quantumsoft.hrms.Human_Resource_Website.service;

import com.quantumsoft.hrms.Human_Resource_Website.entity.EmployeeProjectAssignment;

import java.util.List;
import java.util.UUID;

public interface EmployeeProjectAssignmentService {
    EmployeeProjectAssignment createAssignment(EmployeeProjectAssignment assignment);

    EmployeeProjectAssignment updateAssignment(Long id, EmployeeProjectAssignment updated);

    List<EmployeeProjectAssignment> getAssignmentsByEmployee(Long empId);

    List<EmployeeProjectAssignment> getAssignmentsByProject(Long projectId);

    List<EmployeeProjectAssignment> getAllActiveAssignments();

    List<EmployeeProjectAssignment> getAssignmentsEndingSoon(int days);

    List<EmployeeProjectAssignment> getAssignmentHistoryForEmployee(Long empId);

    void autoReleaseExpiredAssignments();
}
