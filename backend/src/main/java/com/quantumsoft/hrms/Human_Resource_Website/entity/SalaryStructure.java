package com.quantumsoft.hrms.Human_Resource_Website.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.quantumsoft.hrms.Human_Resource_Website.enums.Status;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
public class SalaryStructure {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn( name="emp_id")
    @JsonBackReference("employee-salary")
    private Employee employee;

    @NotNull(message = "Basic salary is required")
    @Positive(message = "Basic salary must be greater than 0")
    private Double basicSalary;

    @NotNull(message = "HRA is required")
    @PositiveOrZero(message = "HRA cannot be negative")
    private Double hra;

    @NotNull(message = "Special allowance is required")
    @PositiveOrZero(message = "Special allowance cannot be negative")
    private Double specialAllowance;

    @PositiveOrZero(message = "Bonus cannot be negative")
    private Double bonus;

    @PositiveOrZero(message = "PF deduction cannot be negative")
    private Double pfDeduction;

    @PositiveOrZero(message = "Tax deduction cannot be negative")
    private Double taxDeduction;

    @NotNull(message = "Effective from date is required")
    private LocalDate effectiveFrom;
    
    private LocalDate effectiveTo;

    @Enumerated(EnumType.STRING)
    private Status status;

    private LocalDateTime  createdDate;

//    @PrePersist
//    public void onCreate() {
//        this.createdDate = LocalDateTime.now();
//    }

    @PrePersist
    @PreUpdate
    public void validateAndSetDates() {
        if (employee != null && effectiveFrom != null) {
            LocalDate joiningDate = employee.getJoiningDate();
            if (joiningDate != null && effectiveFrom.isBefore(joiningDate)) {
                throw new IllegalArgumentException(
                        "Salary structure effective date must be on or after the employee's joining date"
                );
            }
        }
        if (createdDate == null) {
            createdDate = LocalDateTime.now();
        }
    }

}

