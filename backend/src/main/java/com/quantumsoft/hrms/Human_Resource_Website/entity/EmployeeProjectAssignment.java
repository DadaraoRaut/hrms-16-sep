package com.quantumsoft.hrms.Human_Resource_Website.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.quantumsoft.hrms.Human_Resource_Website.enums.AssignmentStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter
@Setter
public class EmployeeProjectAssignment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long assignmentId;

    @NotNull(message = "Employee reference is required")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "emp_id", nullable = false)
    @JsonIgnore
    private Employee employee;

    @NotNull(message = "Project reference is required")
    @ManyToOne(fetch = FetchType.EAGER)
    @JsonIgnore
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    @NotBlank(message = "Role is required")
    @Size(max = 100, message = "Role must not exceed 100 characters")
    private String role;


    @NotNull(message = "Assigned date is required")
    @PastOrPresent(message = "Assigned date cannot be in the future")
    private LocalDate assignedDate;

    private LocalDate releaseDate;

    @NotNull(message = "Allocation percentage is required")
    @Min(value = 0, message = "Allocation percentage cannot be less than 0")
    @Max(value = 100, message = "Allocation percentage cannot be more than 100")
    private Integer allocationPercentage;

    @NotNull(message = "Assignment status is required")
    @Enumerated(EnumType.STRING)
    private AssignmentStatus status;
}
