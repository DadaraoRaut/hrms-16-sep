package com.quantumsoft.hrms.Human_Resource_Website.serviceImpl;

import com.quantumsoft.hrms.Human_Resource_Website.entity.Employee;
import com.quantumsoft.hrms.Human_Resource_Website.entity.Project;
import com.quantumsoft.hrms.Human_Resource_Website.entity.User;
import com.quantumsoft.hrms.Human_Resource_Website.enums.ProjectStatus;
import com.quantumsoft.hrms.Human_Resource_Website.exception.ResourceNotFoundException;
import com.quantumsoft.hrms.Human_Resource_Website.repository.EmployeeRepository;
import com.quantumsoft.hrms.Human_Resource_Website.repository.ProjectRepository;
import com.quantumsoft.hrms.Human_Resource_Website.repository.UserRepository;
import com.quantumsoft.hrms.Human_Resource_Website.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectServiceImpl implements ProjectService {
    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private UserRepository userRepository;

    @Override
    public Project createProject(Project project) {
        Long empId = project.getManager().getEmpId();

        Employee manager = employeeRepository.findById(empId)
                .orElseThrow(() -> new ResourceNotFoundException("Manager not found with ID: " + empId));

        User user = userRepository.findByEmail(manager.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("Manger not found in User with email: " + manager.getEmail()));

        if (user == null || !"MANAGER".equalsIgnoreCase(user.getRole().name())) {
            throw new AccessDeniedException("Selected employee is not linked to a MANAGER user.");
        }

        project.setManager(manager);

        if (project.getStatus() == null) {
            project.setStatus(ProjectStatus.NOT_STARTED);
        }

        return projectRepository.save(project);

    }

    @Override
    public Project updateProject(Long id, Project projectDetails) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found with ID: " + id));

        if (projectDetails.getProjectName() != null)
            project.setProjectName(projectDetails.getProjectName());

        if (projectDetails.getDescription() != null)
            project.setDescription(projectDetails.getDescription());

        if (projectDetails.getStartDate() != null)
            project.setStartDate(projectDetails.getStartDate());

        if (projectDetails.getEndDate() != null)
            project.setEndDate(projectDetails.getEndDate());

        if (projectDetails.getStatus() != null)
            project.setStatus(projectDetails.getStatus());

        if (projectDetails.getManager() != null) {
            Long managerId = projectDetails.getManager().getEmpId();

            Employee manager = employeeRepository.findById(managerId)
                    .orElseThrow(() -> new ResourceNotFoundException("Manager not found"));

            User user = userRepository.findByEmail(manager.getEmail())
                    .orElseThrow(() -> new ResourceNotFoundException("Manger not found in User with email: " + manager.getEmail()));

            if (user == null || !"MANAGER".equalsIgnoreCase(user.getRole().name())) {
                throw new AccessDeniedException("Selected employee is not linked to a MANAGER user.");
            }

            project.setManager(manager);
        }

        return projectRepository.save(project);
    }

    @Override
    public List<Project> getAllProjects() {
        return projectRepository.findAll();
    }

    @Override
    public Project getProjectById(Long id) {
        return projectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found"));
    }
}
