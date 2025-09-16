package com.quantumsoft.hrms.Human_Resource_Website.controller;

import com.quantumsoft.hrms.Human_Resource_Website.entity.BankDetail;
import com.quantumsoft.hrms.Human_Resource_Website.service.BankDetailService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/banking")
@RequiredArgsConstructor
@Validated
@Tag(name = "Bank Details")
public class BankDetailController {

    @Autowired
    private BankDetailService bankDetailService;

    @PostMapping(value = "/create/{employeeId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAnyRole('ADMIN','HR')")
    public ResponseEntity<BankDetail> createBankDetails(
            @PathVariable Long employeeId,
            @Valid @RequestBody BankDetail bankDetails) {
        BankDetail createdDetails = bankDetailService.createBankDetailToEmployee(employeeId, bankDetails);
        return ResponseEntity.ok(createdDetails);
    }

    @PutMapping(value = "/update/{bankDetailId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAnyRole('ADMIN','HR')")
    public ResponseEntity<BankDetail> updateBankDetails(
            @PathVariable Long bankDetailId,
            @Valid @RequestBody BankDetail bankDetails) {

        BankDetail updatedDetails = bankDetailService.updateBankDetail(bankDetailId, bankDetails);
        return ResponseEntity.ok(updatedDetails);
    }

    @GetMapping("/{employeeId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR', 'EMPLOYEE')")
    public ResponseEntity<List<BankDetail>> getBankDetailsByEmployee(@PathVariable Long employeeId) {
        List<BankDetail> details = bankDetailService.getBankDetailsByEmployee(employeeId);
        return ResponseEntity.ok(details);
    }

    @GetMapping("/all")
    @PreAuthorize("hasAnyRole('ADMIN','HR')")
    public ResponseEntity<List<BankDetail>> getAll() {
        return ResponseEntity.ok(bankDetailService.getAll());
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','HR')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        bankDetailService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
