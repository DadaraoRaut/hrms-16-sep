package com.quantumsoft.hrms.Human_Resource_Website.service;

import com.quantumsoft.hrms.Human_Resource_Website.entity.SalaryStructure;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Optional;

public interface SalaryStructureService{

    SalaryStructure createSalaryStructure(Long employeeId, SalaryStructure newStructure);

    List<SalaryStructure> getAll();

    SalaryStructure getCurrentStructureForEmployee(Long employeeId);

//    Optional<SalaryStructure> getStructuresForEmployee(Long employeeId);

   void deleteStructure(Long id);


    Optional<SalaryStructure> getActiveStructuresForEmployee(Long employeeId);
}
