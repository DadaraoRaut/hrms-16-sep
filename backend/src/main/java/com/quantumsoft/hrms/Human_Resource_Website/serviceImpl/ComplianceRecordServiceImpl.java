package com.quantumsoft.hrms.Human_Resource_Website.serviceImpl;

import com.quantumsoft.hrms.Human_Resource_Website.entity.Compliance;
import com.quantumsoft.hrms.Human_Resource_Website.entity.ComplianceRecord;
import com.quantumsoft.hrms.Human_Resource_Website.repository.ComplianceRecordRepository;
import com.quantumsoft.hrms.Human_Resource_Website.repository.ComplianceRepository;
import com.quantumsoft.hrms.Human_Resource_Website.service.ComplianceRecordService;
import com.quantumsoft.hrms.Human_Resource_Website.service.ComplianceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class ComplianceRecordServiceImpl implements ComplianceRecordService {

    @Autowired
    private ComplianceRecordRepository recordRepo;

    @Autowired
    private ComplianceRepository complianceRepo;

    @Autowired
    private ComplianceService complianceService;

    private final String FILE_DIR = "F:\\project files Quantumsoft\\uploaded resume";

    @Override
    public ComplianceRecord submitComplianceRecord(ComplianceRecord record, MultipartFile file) throws IOException {

        String filePath = saveFile(file);

        record.setFilePath(filePath);

        Compliance compliance = complianceService.getComplianceById(record.getCompliance().getComplianceId());
        record.setCompliance(compliance);

        return recordRepo.save(record);
    }

    private String saveFile(MultipartFile file) throws IOException {
        String uploadDir = "uploads/";
        File dir = new File(uploadDir);
        if (!dir.exists()) dir.mkdirs();

        String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
        Path path = Paths.get(uploadDir + fileName);
        Files.write(path, file.getBytes());

        return path.toString();
    }


    @Override
    public List<ComplianceRecord> getComplianceRecordsByMonth(String month) {
        return recordRepo.findByMonth(month);
    }

    @Override
    public ComplianceRecord updateComplianceRecordWithFile(Long id, ComplianceRecord record, MultipartFile file) throws IOException {
        ComplianceRecord existing = recordRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Not found with id " + id));

        if (record != null) {
            if (record.getRemarks() != null) existing.setRemarks(record.getRemarks());
            if (record.getStatus() != null) existing.setStatus(record.getStatus());
        }

        if (file != null && !file.isEmpty()) {
            String path = saveFile(file);
            existing.setFilePath(path);
        }

        existing.setUpdatedAt(LocalDateTime.now());
        return recordRepo.save(existing);
    }

}
