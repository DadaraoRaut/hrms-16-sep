package com.quantumsoft.hrms.Human_Resource_Website.service;

import com.quantumsoft.hrms.Human_Resource_Website.entity.BankDetail;
import jakarta.validation.Valid;

import java.util.List;


public interface BankDetailService {
    BankDetail createBankDetailToEmployee(Long employeeId, @Valid BankDetail bankDetails);

    BankDetail updateBankDetail(Long bankDetailId, @Valid BankDetail updatedDetails);

    List<BankDetail> getBankDetailsByEmployee(Long employeeId);

    List<BankDetail> getAll();

    void delete(Long id);


}
