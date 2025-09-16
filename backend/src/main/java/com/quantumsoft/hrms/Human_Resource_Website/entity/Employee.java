package com.quantumsoft.hrms.Human_Resource_Website.entity;

import com.fasterxml.jackson.annotation.*;
import com.quantumsoft.hrms.Human_Resource_Website.enums.Gender;
import com.quantumsoft.hrms.Human_Resource_Website.enums.JobType;
import com.quantumsoft.hrms.Human_Resource_Website.enums.Status;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "employee")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "emp_id")
    private Long empId;

    @NotBlank(message = "Created by is required")
    private String createdBy;

    @NotBlank(message = "Employee code is required")
    @Size(max = 20, message = "Employee code must not exceed 20 characters")
    @Column(unique = true, nullable = false)
    private String employeeCode;

    @NotBlank(message = "First name is required")
    @Size(max = 25, message = "First name must not exceed 50 characters")
    private String firstName;

    @NotBlank(message = "Last name is required")
    @Size(max = 25, message = "Last name must not exceed 50 characters")
    private String lastName;

    @Size(max = 255, message = "Photo path must not exceed 255 characters")
    private String photo;

    @ManyToOne
    @JoinColumn(name = "manager_id")
    private Employee manager;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Gender is required")
    private Gender gender;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @NotNull(message = "Date of birth is required")
    @Past(message = "Date of birth must be in the past")
    private LocalDate dob;

    @Column(unique = true)
    @NotBlank(message = "Contact number is required")
    @Pattern(regexp = "^\\d{10}$", message = "Contact number must be exactly 10 digits")
    private String contactNumber;

    @Column(unique = true)
    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    @Size(max = 70, message = "Email must not exceed 100 characters")
    private String email;

//    @NotBlank(message = "Address is required")
//    @Size(max = 255, message = "Address must not exceed 255 characters")
//    private String address;

    @NotBlank(message = "Emergency contact is required")
    @Pattern(regexp = "^\\d{10}$", message = "Emergency contact must be exactly 10 digits")
    private String emergencyContact;

    @PastOrPresent(message = "Joining date cannot be in the future")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate joiningDate;

    //    @FutureOrPresent(message = "Probation end date cannot be in the past")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate probationEndDate;

    @PastOrPresent(message = "Resignation date cannot be in the future")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate resignationDate;

    @Size(max = 255, message = "Exit reason must not exceed 255 characters")
    private String exitReason;

    @Enumerated(EnumType.STRING)
    private Status status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id")
    @JsonBackReference("employee-department")
    private Department department;

    @NotBlank(message = "Designation is required")
    @Size(max = 100, message = "Designation must not exceed 100 characters")
    @Column(nullable = false)
    private String designation;

    @Enumerated(EnumType.STRING)
    private JobType jobType;

    @Size(max = 25, message = "Location must not exceed 25 characters")
    private String location;

    @Lob
    @Size(max = 50, message = "Education details must not exceed 50 characters")
    private String education;

    @Lob
    @Size(max = 50, message = "Experience details must not exceed 50 characters")
    private String experience;

    @Lob
    @Size(max = 100, message = "Certifications details must not exceed 500 characters")
    private String certifications;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
//    @JsonIgnore
    private User user;

    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL)
    @JsonManagedReference("employee-benefits")
    private List<EmployeeBenefit> benefits;

    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference("employee-salary")
    private List<SalaryStructure> salaryStructures;

    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference("employee-leaves")
    private List<Leave> leaves;

    @OneToOne(mappedBy = "employee", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonBackReference
    private BankDetail bankDetail;

    @OneToOne(mappedBy = "departmentHead")
    @JsonBackReference("department-head")
    private Department managedDepartments;
}

