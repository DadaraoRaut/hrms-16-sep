package com.quantumsoft.hrms.Human_Resource_Website.serviceImpl;


import com.quantumsoft.hrms.Human_Resource_Website.entity.Attendance;
import com.quantumsoft.hrms.Human_Resource_Website.entity.Employee;
import com.quantumsoft.hrms.Human_Resource_Website.entity.Payroll;
import com.quantumsoft.hrms.Human_Resource_Website.entity.SalaryStructure;
import com.quantumsoft.hrms.Human_Resource_Website.enums.AttendanceStatus;
import com.quantumsoft.hrms.Human_Resource_Website.repository.*;
import com.quantumsoft.hrms.Human_Resource_Website.security.PayslipPdfGenerator;
import com.quantumsoft.hrms.Human_Resource_Website.service.PayrollService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Locale;

@Service
public class PayrollServiceImpl implements PayrollService {

    @Autowired
    private PayrollRepository payrollRepository;

    @Autowired
    private SalaryStructureRepository salaryStructureRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private AttendanceRepository attendanceRepository;

    @Autowired
    private LeaveRepository leaveRepository;

    @Override
    public Payroll generatePayroll(Long empId, String month, int year) {
        YearMonth yearMonth;
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM-yyyy", Locale.ENGLISH);
            yearMonth = YearMonth.parse(month + "-" + year, formatter);  // e.g., "Jul-2025"
        } catch (DateTimeParseException e) {
            throw new RuntimeException("Invalid month format. Use 3-letter month abbreviation like 'Jan', 'Feb', etc.");
        }

        LocalDate startDate = yearMonth.atDay(1);

        LocalDate endDate = startDate.withDayOfMonth(startDate.lengthOfMonth());

        Employee employee = employeeRepository.findById(empId)
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        // Check if payroll already exists for this employee and month
        if (payrollRepository.findByEmployee_EmpIdAndMonthAndYear(employee.getEmpId(), month, year).isPresent()) {
            throw new RuntimeException("Payroll already generated for this employee for the given month");
        }

        List<Attendance> attendanceRecords = attendanceRepository
                .findByEmployee_EmpIdAndDateBetween(employee.getEmpId(), startDate, endDate);

        long presentDays = attendanceRecords.stream()
                .filter(a -> a.getStatus() == AttendanceStatus.PRESENT).count();

        long paidLeaveDays = attendanceRecords.stream()
                .filter(a -> a.getStatus() == AttendanceStatus.ON_LEAVE).count();

        long workingDays = startDate.datesUntil(endDate.plusDays(1))
                .filter(date -> !(date.getDayOfWeek() == DayOfWeek.SUNDAY || date.getDayOfWeek() == DayOfWeek.SATURDAY))
                .count();

        double payableDays = presentDays + paidLeaveDays;


        // Check applicable salary structure for that month
//        SalaryStructure structure = salaryStructureRepository.findApplicableStructureForDate(employee, startDate)
//                .orElseThrow(() -> new RuntimeException("No salary structure found for this employee for the given month"));

        SalaryStructure structure = salaryStructureRepository.findActiveStructureForDate(employee, startDate)
                .orElseThrow(() -> new RuntimeException("No active salary structure found for this employee for the given month"));

        // Calculate earnings, deductions, net salary
        double totalEarnings = structure.getBasicSalary() +structure.getHra()+ structure.getSpecialAllowance()+structure.getBonus();
        double totalDeductions = structure.getPfDeduction()+structure.getTaxDeduction();
        double netSalary = totalEarnings - totalDeductions;

        Payroll payroll = Payroll.builder()
                .employee(employee)
                .month(month)
                .year(year)
                .generatedDate(LocalDate.now())
                .totalEarnings(totalEarnings)
                .totalDeductions(totalDeductions)
                .netSalary(netSalary)
                .paymentStatus("Pending")
                .presentDays(presentDays)
                .paidLeaveDays(paidLeaveDays)
                .workingDays(workingDays)
                .payableDays(payableDays)
                .build();

        return payrollRepository.save(payroll);

    }

    @Override
    public ByteArrayInputStream generatePayslipPdf(Long employeeId, String month, int year){

        Payroll payroll = payrollRepository.findByEmployee_EmpIdAndMonthAndYear(employeeId,month,year)
                .orElseThrow(() -> new RuntimeException("Payroll not found for employee and month"));

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM-yyyy", Locale.ENGLISH);
        YearMonth yearMonth;
        try {
            yearMonth = YearMonth.parse(month + "-" + year, formatter);
        } catch (DateTimeParseException e) {
            throw new RuntimeException("Invalid month format. Use 3-letter month abbreviation like 'Jan', 'Feb', etc.");
        }
        LocalDate payrollDate = yearMonth.atDay(1);

//        SalaryStructure structure = salaryStructureRepository
//                .findApplicableStructureForDate(payroll.getEmployee(), payrollDate)
//                .orElseThrow(() -> new RuntimeException("No salary structure for this employee in payroll month"));
        SalaryStructure structure = salaryStructureRepository.findActiveStructureForDate(payroll.getEmployee(), payrollDate)
                .orElseThrow(() -> new RuntimeException("No active salary structure found for this employee for the given month"));


        return PayslipPdfGenerator.generatePayslip(payroll, structure);
    }

    @Override
    public Payroll getPayrollByEmpIdMonthYear(Long empId, String month, int year) {
       Payroll payroll=payrollRepository.findByEmployee_EmpIdAndMonthAndYear(empId,month,year)
               .orElseThrow(() -> new RuntimeException("Payroll not found for employee and month"));

       return payroll;
    }

}
