package com.quantumsoft.hrms.Human_Resource_Website.controller;

import com.quantumsoft.hrms.Human_Resource_Website.entity.EmployeeProjectAssignment;
import com.quantumsoft.hrms.Human_Resource_Website.service.EmployeeProjectAssignmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/employee-projects")
public class EmployeeProjectAssignmentController {

    @Autowired
    private EmployeeProjectAssignmentService service;

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @PostMapping
    public ResponseEntity<EmployeeProjectAssignment> assign(@RequestBody EmployeeProjectAssignment assignment) {
        return new ResponseEntity<>(service.createAssignment(assignment), HttpStatus.CREATED);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @PutMapping("/{id}")
    public ResponseEntity<EmployeeProjectAssignment> updateAssignment(
            @PathVariable Long id,
            @RequestBody EmployeeProjectAssignment updated) {
        return ResponseEntity.ok(service.updateAssignment(id, updated));
    }
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @GetMapping
    public ResponseEntity<List<EmployeeProjectAssignment>> getAssignments(
            @RequestParam(required = false) Long empId,
            @RequestParam(required = false) Long projectId) {

        if (empId != null) {
            return ResponseEntity.ok(service.getAssignmentsByEmployee(empId));
        } else if (projectId != null) {
            return ResponseEntity.ok(service.getAssignmentsByProject(projectId));
        } else {
            throw new IllegalArgumentException("Either empId or projectId must be provided");
        }
    }

}
