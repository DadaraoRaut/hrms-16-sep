package com.quantumsoft.hrms.Human_Resource_Website.serviceImpl;

import com.quantumsoft.hrms.Human_Resource_Website.entity.Employee;
import com.quantumsoft.hrms.Human_Resource_Website.entity.EmployeeProjectAssignment;
import com.quantumsoft.hrms.Human_Resource_Website.entity.Project;
import com.quantumsoft.hrms.Human_Resource_Website.enums.AssignmentStatus;
import com.quantumsoft.hrms.Human_Resource_Website.exception.ResourceNotFoundException;
import com.quantumsoft.hrms.Human_Resource_Website.repository.EmployeeProjectAssignmentRepository;
import com.quantumsoft.hrms.Human_Resource_Website.repository.EmployeeRepository;
import com.quantumsoft.hrms.Human_Resource_Website.repository.ProjectRepository;
import com.quantumsoft.hrms.Human_Resource_Website.service.EmployeeProjectAssignmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
@Service
public class EmployeeProjectAssignmentServiceImpl implements EmployeeProjectAssignmentService {
    @Autowired
    private EmployeeProjectAssignmentRepository repository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Override
    public EmployeeProjectAssignment createAssignment(EmployeeProjectAssignment assignment) {
        // Fetch full Employee
        Employee employee = employeeRepository.findById(assignment.getEmployee().getEmpId())
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found"));

        // Fetch full Project
        Project project = projectRepository.findById(assignment.getProject().getProjectId())
                .orElseThrow(() -> new ResourceNotFoundException("Project not found"));

        // Set full objects
        assignment.setEmployee(employee);
        assignment.setProject(project);
        assignment.setStatus(AssignmentStatus.ACTIVE);

        return repository.save(assignment);
    }

    @Override
    public EmployeeProjectAssignment updateAssignment(Long id, EmployeeProjectAssignment updated) {
        EmployeeProjectAssignment existing = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Assignment not found with ID: " + id));

        existing.setRole(updated.getRole());
        existing.setReleaseDate(updated.getReleaseDate());
        existing.setAllocationPercentage(updated.getAllocationPercentage());

        if (updated.getReleaseDate() != null && updated.getReleaseDate().isBefore(LocalDate.now())) {
            existing.setStatus(AssignmentStatus.RELEASED);
        }

        return repository.save(existing);
    }

    @Override
    public List<EmployeeProjectAssignment> getAssignmentsByEmployee(Long empId) {
        return repository.findByEmployee_EmpId(empId);
    }

    @Override
    public List<EmployeeProjectAssignment> getAssignmentsByProject(Long projectId) {
        return repository.findByProject_ProjectId(projectId);
    }

    @Override
    public List<EmployeeProjectAssignment> getAllActiveAssignments() {
        return repository.findByStatus(AssignmentStatus.ACTIVE);
    }

    @Override
    public List<EmployeeProjectAssignment> getAssignmentsEndingSoon(int days) {
        LocalDate date = LocalDate.now().plusDays(days);
        return repository.findAssignmentsEndingSoon(date);
    }

    @Override
    public List<EmployeeProjectAssignment> getAssignmentHistoryForEmployee(Long empId) {
        return repository.findByEmployee_EmpIdOrderByAssignedDateDesc(empId);
    }

    @Override
    @Scheduled(cron = "0 0 0 * * ?") // daily at 12:00 AM
    public void autoReleaseExpiredAssignments() {
        LocalDate today = LocalDate.now();
        List<EmployeeProjectAssignment> expired = repository
                .findByReleaseDateBeforeAndStatus(today, AssignmentStatus.ACTIVE);

        if (!expired.isEmpty()) {
            expired.forEach(assignment -> {
                assignment.setStatus(AssignmentStatus.RELEASED);
                System.out.println("Auto-expired assignment ID: " + assignment.getAssignmentId());
            });

            repository.saveAll(expired);
            System.out.println("Total auto-expired assignments: " + expired.size());
        }


    }
}
