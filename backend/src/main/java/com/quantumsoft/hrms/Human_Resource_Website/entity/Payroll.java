package com.quantumsoft.hrms.Human_Resource_Website.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Builder
@Getter
@Setter
@Table(name="payroll")
public class Payroll {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Employee is required")
    @ManyToOne
    @JoinColumn(name = "emp_id")
    private Employee employee;


//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "salary_structure_id")
//    private SalaryStructure salaryStructure;

    @NotBlank(message = "Month is required")
    @Pattern(regexp = "^(Jan|Feb|Mar|Apr|May|Jun|Jul|Aug|Sep|Oct|Nov|Dec)$", message = "Month must be a valid month name")
    private String month;

    @Min(value = 1900, message = "Year must be valid")
    @Max(value = 3000, message = "Year must be valid")
    @Column(nullable = false)
    private int year;

    @NotNull(message = "Generated date is required")
    @PastOrPresent(message = "Generated date cannot be in the future")
    @Column(nullable = false)
    private LocalDate generatedDate;

    @PositiveOrZero(message = "Total earnings must be zero or positive")
    @Column(nullable = false)
    private double totalEarnings;

    @PositiveOrZero(message = "Total deductions must be zero or positive")
    @Column(nullable = false)
    private double totalDeductions;

    @PositiveOrZero(message = "Net salary must be zero or positive")
    @Column(nullable = false)
    private double netSalary;

    @NotBlank(message = "Payment status is required")
    @Size(max = 50, message = "Payment status must not exceed 50 characters")
    private String paymentStatus;

    @PastOrPresent(message = "Payment date cannot be in the future")
    private LocalDate paymentDate;

    @PositiveOrZero(message = "Present days must be zero or positive")
    @Column(nullable = false)
    private long presentDays;

    @PositiveOrZero(message = "Paid leave days must be zero or positive")
    @Column(nullable = false)
    private long paidLeaveDays;

    @PositiveOrZero(message = "Working days must be zero or positive")
    @Column(nullable = false)
    private long workingDays;

    @PositiveOrZero(message = "Payable days must be zero or positive")
    @Column(nullable = false)
    private double payableDays;

    @PositiveOrZero(message = "Arrear days must be zero or positive")
    @Column(nullable = false)
    private long arrearDays;

    @PrePersist
    @PreUpdate
    public void validateGeneratedDate() {
        if (employee != null && generatedDate != null) {
            LocalDate joiningDate = employee.getJoiningDate();
            if (joiningDate != null && generatedDate.isBefore(joiningDate)) {
                throw new IllegalArgumentException(
                        "Payroll generated date must be on or after the employee's joining date"
                );
            }
        }
    }

    public Payroll(Long id, Employee employee, String month, int year, LocalDate generatedDate, double totalEarnings, double totalDeductions, double netSalary, String paymentStatus, LocalDate paymentDate, long presentDays, long paidLeaveDays, long workingDays, double payableDays, long arrearDays) {
        this.id = id;
        this.employee = employee;
        this.month = month;
        this.year = year;
        this.generatedDate = generatedDate;
        this.totalEarnings = totalEarnings;
        this.totalDeductions = totalDeductions;
        this.netSalary = netSalary;
        this.paymentStatus = paymentStatus;
        this.paymentDate = paymentDate;
        this.presentDays = presentDays;
        this.paidLeaveDays = paidLeaveDays;
        this.workingDays = workingDays;
        this.payableDays = payableDays;
        this.arrearDays = arrearDays;
    }

    public Payroll() {
    }
}
