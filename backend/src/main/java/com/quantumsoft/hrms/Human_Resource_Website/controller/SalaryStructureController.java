package com.quantumsoft.hrms.Human_Resource_Website.controller;

import com.quantumsoft.hrms.Human_Resource_Website.entity.SalaryStructure;
import com.quantumsoft.hrms.Human_Resource_Website.service.SalaryStructureService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/salary-structures")
@RequiredArgsConstructor
public class SalaryStructureController {

    private final SalaryStructureService salaryStructureService;

    // Create Salary Structure (Admin/HR)
    @PostMapping("/create/{employeeId}")
    @PreAuthorize("hasAnyRole('ADMIN','HR')")
    public ResponseEntity<SalaryStructure> createStructure(@PathVariable Long employeeId,
                                                           @Valid @RequestBody SalaryStructure structure) {
        return ResponseEntity.ok(salaryStructureService.createSalaryStructure(employeeId, structure));
    }

    // Get all Salary Structures (Admin/HR)
    @GetMapping("/all")
    @PreAuthorize("hasAnyRole('ADMIN','HR')")
    public ResponseEntity<List<SalaryStructure>> getAll() {
        return ResponseEntity.ok(salaryStructureService.getAll());
    }

    // Get all structures for employee (Employee/HR/Admin)
    @GetMapping("/employee/{employeeId}")
    @PreAuthorize("hasAnyRole('EMPLOYEE','HR','ADMIN')")
    public ResponseEntity<Optional<SalaryStructure>> getForEmployee(@PathVariable Long employeeId) {
        return ResponseEntity.ok(salaryStructureService.getActiveStructuresForEmployee(employeeId));
    }

    // Get current active structure for employee (Employee/HR/Admin)
    @GetMapping("/employee/current/{employeeId}")
    @PreAuthorize("hasAnyRole('EMPLOYEE','HR','ADMIN')")
    public ResponseEntity<SalaryStructure> getCurrentForEmployee(@PathVariable Long employeeId) {
        return ResponseEntity.ok(salaryStructureService.getCurrentStructureForEmployee(employeeId));
    }


    // Delete Salary Structure (Admin/HR)
    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','HR')")
    public ResponseEntity<String> deleteStructure(@PathVariable Long id) {
        salaryStructureService.deleteStructure(id);
        return ResponseEntity.ok("Deleted Successfully");
    }



}