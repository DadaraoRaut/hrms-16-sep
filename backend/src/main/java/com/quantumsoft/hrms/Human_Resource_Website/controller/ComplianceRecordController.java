package com.quantumsoft.hrms.Human_Resource_Website.controller;

import com.quantumsoft.hrms.Human_Resource_Website.entity.ComplianceRecord;
import com.quantumsoft.hrms.Human_Resource_Website.service.ComplianceRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/compliance-records")
public class ComplianceRecordController {
    @Autowired
    private ComplianceRecordService recordService;


    @PostMapping(value = "/submit", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAnyRole('HR','ADMIN')")
    public ResponseEntity<ComplianceRecord> submitComplianceRecord(
            @RequestPart("record") ComplianceRecord record,
            @RequestPart("file") MultipartFile file) throws IOException {

        ComplianceRecord savedRecord = recordService.submitComplianceRecord(record, file);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedRecord);
    }

    @GetMapping("/{month}")
    @PreAuthorize("hasAnyRole('HR','ADMIN')")
    public List<ComplianceRecord> getComplianceRecordsByMonth(@PathVariable String month) {
        return recordService.getComplianceRecordsByMonth(month);
    }
    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAnyRole('HR','ADMIN')")
    public ResponseEntity<ComplianceRecord> updateComplianceRecordWithFile(
            @PathVariable Long id,
            @RequestPart(value = "record", required = false) ComplianceRecord record,
            @RequestPart(value = "file", required = false) MultipartFile file) throws IOException {
        ComplianceRecord updated = recordService.updateComplianceRecordWithFile(id, record, file);
        return ResponseEntity.ok(updated);
    }


}
