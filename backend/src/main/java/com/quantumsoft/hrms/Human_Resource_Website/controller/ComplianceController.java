package com.quantumsoft.hrms.Human_Resource_Website.controller;

import com.quantumsoft.hrms.Human_Resource_Website.entity.Compliance;
import com.quantumsoft.hrms.Human_Resource_Website.service.ComplianceService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/compliances")
public class ComplianceController {
    @Autowired
    private ComplianceService complianceService;

    @PostMapping("/create")
    @PreAuthorize("hasAnyRole('HR','ADMIN')")
    public Compliance addCompliance(@Valid @RequestBody Compliance compliance) {
        return complianceService.createCompliance(compliance);
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasAnyRole('HR','ADMIN')")
    public Compliance updateCompliance(@PathVariable Long id, @Valid @RequestBody Compliance compliance) {
        return complianceService.updateCompliance(id, compliance);
    }
    @GetMapping("/all")
    @PreAuthorize("hasAnyRole('HR','ADMIN')")
    public List<Compliance> getAllCompliances() {
        return complianceService.getAllCompliances();
    }

    @GetMapping("/get/{id}")
    @PreAuthorize("hasAnyRole('HR','ADMIN')")
    public ResponseEntity<Compliance> getComplianceById(@PathVariable Long id) {
        Compliance compliance = complianceService.getComplianceById(id);
        return ResponseEntity.ok(compliance);
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAnyRole('HR','ADMIN')")
    public void deactivateCompliance(@PathVariable Long id) {
        complianceService.deactivateCompliance(id);
    }

}
