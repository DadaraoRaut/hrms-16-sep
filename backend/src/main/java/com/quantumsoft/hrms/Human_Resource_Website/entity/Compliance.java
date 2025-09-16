package com.quantumsoft.hrms.Human_Resource_Website.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.quantumsoft.hrms.Human_Resource_Website.enums.ComplianceFrequency;
import com.quantumsoft.hrms.Human_Resource_Website.enums.ComplianceType;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
@Table(name = "compliances")
public class Compliance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long complianceId;

    @NotBlank(message = "Compliance name is required")
    @Size(max = 25, message = "Compliance name must not exceed 25 characters")
    @Pattern(regexp = "^[A-Za-z ]+$", message = "Compliance name must contain only letters and spaces")
    @Column(nullable = false)
    private String name;

    @Size(max = 100, message = "Description must not exceed 100 characters")
    @Pattern(regexp = "^[A-Za-z ]*$", message = "Description must contain only letters and spaces")
    private String description;

    @NotNull(message = "Compliance type is required")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ComplianceType type;


    @NotNull(message = "Compliance frequency is required")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ComplianceFrequency frequency;

    @FutureOrPresent(message = "Due date must be today or in the future")
    private LocalDate dueDate;

    @Size(max = 100, message = "Penalty must not exceed 100 characters")
    private String penalty;  // Text or numeric

    @NotNull(message = "Document requirement flag is required")
    private Boolean documentRequired;

    @NotNull(message = "Active status must be specified")
    private Boolean isActive = true;

    private LocalDateTime createdAt = LocalDateTime.now();

    private LocalDateTime updatedAt = LocalDateTime.now();

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "emp_id")
//    @OnDelete(action = OnDeleteAction.CASCADE)
//    private Employee employee;

    @PreUpdate
    public void setUpdatedAt() {
        this.updatedAt = LocalDateTime.now();
    }

//    public boolean isEmployeeSpecific() {
//        return this.employee != null;
//    }
}
