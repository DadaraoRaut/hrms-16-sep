package com.quantumsoft.hrms.Human_Resource_Website.controller;

import com.quantumsoft.hrms.Human_Resource_Website.entity.Benefit;
import com.quantumsoft.hrms.Human_Resource_Website.service.BenefitService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@CrossOrigin("*")
@RequestMapping("/api/benefits")
public class BenefitController {

    @Autowired
    private BenefitService benefitService;


    @PostMapping("/create")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR')")
    public Benefit createBenefit(@Valid @RequestBody Benefit benefit) {
        return benefitService.createBenefit(benefit);
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR')")
    public Benefit updateBenefit(@PathVariable Long id, @Valid @RequestBody Benefit benefit) {
        return benefitService.updateBenefit(id, benefit);
    }

    @GetMapping("/all")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR', 'EMPLOYEE','MANAGER','FINANCE')")
    public List<Benefit> getAllBenefits() {
        return benefitService.getAllBenefits();
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR')")
    public ResponseEntity<Void> deleteBenefit(@PathVariable Long id) {
        benefitService.deleteBenefit(id);
        return ResponseEntity.noContent().build();
    }

}
