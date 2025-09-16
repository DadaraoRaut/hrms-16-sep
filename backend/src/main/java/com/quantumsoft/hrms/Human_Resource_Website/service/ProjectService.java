package com.quantumsoft.hrms.Human_Resource_Website.service;

import com.quantumsoft.hrms.Human_Resource_Website.entity.Project;

import java.util.List;

public interface ProjectService {
    Project createProject(Project project);
    Project updateProject(Long id, Project projectDetails);
    List<Project> getAllProjects();
    Project getProjectById(Long id);
}
