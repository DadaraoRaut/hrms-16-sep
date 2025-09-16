package com.quantumsoft.hrms.Human_Resource_Website.controller;

import com.quantumsoft.hrms.Human_Resource_Website.entity.EmployeeBenefit;
import com.quantumsoft.hrms.Human_Resource_Website.service.EmployeeBenefitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/employee-benefits")
public class EmployeeBenefitController {

    @Autowired
    private EmployeeBenefitService employeeBenefitService;


    @PostMapping("/assign")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR')")
    public EmployeeBenefit assignBenefit(@RequestBody EmployeeBenefit employeeBenefit) {
        return employeeBenefitService.assignBenefit(employeeBenefit);
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR')")
    public EmployeeBenefit updateEmployeeBenefit(@PathVariable Long id, @RequestBody EmployeeBenefit employeeBenefit) {
        return employeeBenefitService.updateEmployeeBenefit(id, employeeBenefit);
    }

    @GetMapping("/employee/{employeeId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR','EMPLOYEE')")
    public List<EmployeeBenefit> getBenefitsByEmployeeId(@PathVariable Long employeeId) {
        return employeeBenefitService.getBenefitsByEmployeeId(employeeId);
    }

    @PatchMapping("/soft-delete/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR')")
    public ResponseEntity<String> softDeleteEmployeeBenefit(@PathVariable Long id) {
        employeeBenefitService.softDeleteEmployeeBenefit(id);
        return ResponseEntity.ok("Employee benefit assignment soft-deleted (status set to INACTIVE)");
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR')")
    public ResponseEntity<String> deleteEmployeeBenefit(@PathVariable Long id) {
        employeeBenefitService.deleteEmployeeBenefit(id);
        return ResponseEntity.ok("Delete employee benefits delete suceessfully");
    }
}
