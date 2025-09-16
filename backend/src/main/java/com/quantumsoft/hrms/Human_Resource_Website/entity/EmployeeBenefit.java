package com.quantumsoft.hrms.Human_Resource_Website.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.quantumsoft.hrms.Human_Resource_Website.enums.Status;
import jakarta.persistence.*;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Table
@Data
@Entity
public class EmployeeBenefit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long employeeBenefitId;

    @ManyToOne
    @NotNull(message = "Employee reference is required")
    @JoinColumn(name = "emp_id")
    @JsonBackReference("employee-benefits")
    private Employee employee;

    @ManyToOne
    @NotNull(message = "Benefit reference is required")
    @JoinColumn(name = "benefit_id")
    @JsonBackReference
    private Benefit benefit;

    @NotNull(message = "Amount is required")
    @PositiveOrZero(message = "Amount must be zero or positive")
    private Double amount;

    @NotNull(message = "Effective from date is required")
    private LocalDateTime effectiveFrom;

    @FutureOrPresent(message = "Effective to date must be in the present or future")
    private LocalDateTime effectiveTo;

    @NotNull(message = "Status is required")
    @Enumerated(EnumType.STRING)
    private Status status;

    @Size(max = 500, message = "Notes must not exceed 500 characters")
    private String notes;

    private LocalDateTime enrollmentDate;

    @PrePersist
    public void prePersist() {
        enrollmentDate = LocalDateTime.now();
    }
}
