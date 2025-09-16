package com.quantumsoft.hrms.Human_Resource_Website.serviceImpl;

import com.quantumsoft.hrms.Human_Resource_Website.entity.BankDetail;
import com.quantumsoft.hrms.Human_Resource_Website.entity.Employee;
import com.quantumsoft.hrms.Human_Resource_Website.repository.BankDetailRepository;
import com.quantumsoft.hrms.Human_Resource_Website.repository.EmployeeRepository;
import com.quantumsoft.hrms.Human_Resource_Website.service.BankDetailService;
import com.quantumsoft.hrms.Human_Resource_Website.service.EmployeeService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;


@Service
@RequiredArgsConstructor
public class BankDetailServiceImpl implements BankDetailService {

     @Autowired
    private BankDetailRepository bankDetailRepository;

     @Autowired
     private EmployeeService employeeService;

     @Autowired
     private EmployeeRepository employeeRepository;

    @Override
    public BankDetail createBankDetailToEmployee(Long employeeId, BankDetail bankDetail) {
        System.out.println("Looking for employee with ID: " + employeeId);

        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new EntityNotFoundException("Employee not found"));

        if (bankDetailRepository.findByEmployee(employee).size() > 0) {
            throw new IllegalStateException("Bank detail already exists for this employee");
        }
        bankDetail.setEmployee(employee);
        return bankDetailRepository.save(bankDetail);
    }

    @Override
    public BankDetail updateBankDetail(Long bankDetailId, BankDetail updatedDetails) {

        BankDetail existing = bankDetailRepository.findById(bankDetailId)
                .orElseThrow(() -> new EntityNotFoundException("BankDetail not found with ID: " + bankDetailId));

        existing.setAccountHolderName(updatedDetails.getAccountHolderName());
        existing.setBankName(updatedDetails.getBankName());
        existing.setAccountNumber(updatedDetails.getAccountNumber());
        existing.setIfscCode(updatedDetails.getIfscCode());
        existing.setBranch(updatedDetails.getBranch());
        existing.setAccountType(updatedDetails.getAccountType());
        return bankDetailRepository.save(existing);
    }



    @Override
    public List<BankDetail> getBankDetailsByEmployee(Long employeeId) {
        Employee employee = employeeService.getEmployeeById(employeeId);
        return bankDetailRepository.findByEmployee(employee);
    }


    @Override
    public List<BankDetail> getAll() {
        return bankDetailRepository.findAll();
    }

    @Override
    public void delete(Long id) {
        bankDetailRepository.deleteById(id);

    }


}
