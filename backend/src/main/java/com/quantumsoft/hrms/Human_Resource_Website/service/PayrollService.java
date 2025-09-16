package com.quantumsoft.hrms.Human_Resource_Website.service;

import com.quantumsoft.hrms.Human_Resource_Website.entity.Payroll;

import java.io.ByteArrayInputStream;

public interface PayrollService {

Payroll generatePayroll(Long empId, String month, int year);

     public ByteArrayInputStream generatePayslipPdf(Long employeeId, String month, int year);

    Payroll getPayrollByEmpIdMonthYear(Long empId, String month, int year);
}
