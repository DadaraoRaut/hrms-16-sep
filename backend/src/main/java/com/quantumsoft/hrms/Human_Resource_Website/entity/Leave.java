package com.quantumsoft.hrms.Human_Resource_Website.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.quantumsoft.hrms.Human_Resource_Website.enums.LeaveStatus;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import jakarta.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name="leaves")
@Entity
public class Leave {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long leaveRequestId;

    @NotNull(message = "Employee reference is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "emp_id", nullable = false)
    @JsonBackReference("employee-leaves")
    private Employee employee; // Foreign key to Employee

    @NotNull(message = "Leave type is required")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "leave_type_id", nullable = false)
    @JsonIgnoreProperties({"leaves"})
    private LeaveType leaveType;

    @NotNull(message = "Start date is required")
    @FutureOrPresent(message = "Start date cannot be in the past")
    @Column(name = "start_date",  nullable = false)
    private LocalDate startDate;

    @NotNull(message = "End date is required")
    @FutureOrPresent(message = "End date cannot be in the past")
    @Column(name = "end_date",  nullable = false)
    private LocalDate endDate;

    @Positive(message = "Total days must be greater than 0")
    @Column(name = "total_days", nullable = false)
    private int totalDays;

    @NotBlank(message = "Reason is required")
    @Size(max = 500, message = "Reason must not exceed 500 characters")
    private String reason;

    @NotNull(message = "Leave status is required")
    @Enumerated(EnumType.STRING)
    private LeaveStatus status; // Pending, Approved, Rejected

    @Positive(message = "Approver ID must be a positive number")
    @Column(name = "approver_id")
    private Long approverId; // Foreign key to Manager

    @Size(max = 255, message = "Attachment URL must not exceed 255 characters")
    @Column(name = "attachment_url")
    private String attachmentUrl;

}
