package com.quantumsoft.hrms.Human_Resource_Website.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.quantumsoft.hrms.Human_Resource_Website.enums.AttendanceStatus;
import com.quantumsoft.hrms.Human_Resource_Website.enums.Mode;
import com.quantumsoft.hrms.Human_Resource_Website.enums.RegularizationStatus;
import com.quantumsoft.hrms.Human_Resource_Website.enums.WorkFrom;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name="attendance")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Attendance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long attendanceId;

    @NotNull(message = "Employee is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "emp_id", nullable = false)
    private Employee employee;

    @NotNull(message = "Date is required")
    private LocalDate date;

    @Column(nullable = true)
    private LocalTime clockInTime;

    @Column(nullable = true)
    private LocalTime clockOutTime;

    @NotNull(message = "Mode is required")
    @Enumerated(EnumType.STRING)
    private Mode mode;

    @NotNull(message = "Work-from is required")
    @Enumerated(EnumType.STRING)
    private WorkFrom workFrom;

    private Double latitude;

    private Double longitude;

    @PositiveOrZero(message = "Total hours worked must be zero or positive")
    @Column(name = "total_hours_worked")
    private Double totalHoursWorked;

    private String timesheetProjectCode;  // nullable


    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private AttendanceStatus status;

    @Column(nullable = true)
    @Enumerated(EnumType.STRING)
    private RegularizationStatus regularizationStatus; // PENDING, APPROVED, REJECTED

    @Size(max = 255, message = "Regularization reason cannot exceed 255 characters")
    private String regularizationReason;

    @JsonFormat(pattern = "HH:mm")
    private LocalTime approvedClockInTime;

    @JsonFormat(pattern = "HH:mm")
    private LocalTime approvedClockOutTime;
}
