package com.quantumsoft.hrms.Human_Resource_Website.controller;

import com.quantumsoft.hrms.Human_Resource_Website.entity.Employee;
import com.quantumsoft.hrms.Human_Resource_Website.entity.Payroll;
import com.quantumsoft.hrms.Human_Resource_Website.repository.EmployeeRepository;
import com.quantumsoft.hrms.Human_Resource_Website.repository.PayrollRepository;
import com.quantumsoft.hrms.Human_Resource_Website.service.PayrollService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;
import java.util.UUID;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/payroll")
@RequiredArgsConstructor
public class PayrollController {

    @Autowired
    private PayrollService payrollService;

    @Autowired
    private EmployeeRepository employeeRepository;

    @PostMapping("/generate")
    @PreAuthorize("hasAnyRole('HR', 'ADMIN')")
    public ResponseEntity<Payroll> generatePayroll(@RequestParam Long empId,
                                                   @RequestParam String month,
                                                   @RequestParam int year)
    {

        return ResponseEntity.ok(payrollService.generatePayroll(empId, month, year));
    }


    @GetMapping("/download")
    @PreAuthorize("hasAnyRole('HR', 'ADMIN', 'EMPLOYEE', 'MANAGER', 'FINANCE')")
    public ResponseEntity<byte[]> downloadPayslip(@RequestParam Long empId, @RequestParam String month, @RequestParam int year) {
        ByteArrayInputStream bis = payrollService.generatePayslipPdf(empId, month, year);

        Employee employee = employeeRepository.findById(empId)
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        String firstName = employee.getFirstName().replaceAll("\\s+", "_");
        String lastName = employee.getLastName().replaceAll("\\s+", "_");

        String fileName = String.format("%s_%s(%d)_%s_%d.pdf", firstName, lastName, empId, month, year);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=" + fileName);
        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(bis.readAllBytes());
    }

    @GetMapping("/view")
    @PreAuthorize("hasAnyRole('HR', 'ADMIN', 'EMPLOYEE', 'MANAGER', 'FINANCE')")
    public ResponseEntity<Payroll> viewPayroll(@RequestParam Long empId,
                                               @RequestParam String month,
                                               @RequestParam int year) {
        Payroll payroll = payrollService.getPayrollByEmpIdMonthYear(empId, month, year);
        if (payroll != null) {
            return ResponseEntity.ok(payroll);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
