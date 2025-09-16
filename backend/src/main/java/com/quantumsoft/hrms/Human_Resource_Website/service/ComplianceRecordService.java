package com.quantumsoft.hrms.Human_Resource_Website.service;

import com.quantumsoft.hrms.Human_Resource_Website.entity.ComplianceRecord;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ComplianceRecordService {
    ComplianceRecord submitComplianceRecord(ComplianceRecord record, MultipartFile file) throws IOException;
    List<ComplianceRecord> getComplianceRecordsByMonth(String month);
    ComplianceRecord updateComplianceRecordWithFile(Long id, ComplianceRecord record, MultipartFile file)throws IOException;
}
