package com.quantumsoft.hrms.Human_Resource_Website.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PerformanceReview {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Employee is required")
    @ManyToOne
    @JoinColumn(name = "emp_id")
    @JsonIgnore
    private Employee employee;

    @NotBlank(message = "Review cycle is required")
    @Pattern(regexp = "^\\d{4}-Q[1-4]$", message = "Review cycle must be in format YYYY-Qn (e.g. 2025-Q2)")
    @Column(nullable = false)
    private String reviewCycle; // e.g. "2025-Q2"

    @Size(max = 2000, message = "Self-assessment must not exceed 2000 characters")
    @Column(length = 2000, columnDefinition = "TEXT")
    private String selfAssessment;

    @Size(max = 2000, message = "Manager review must not exceed 2000 characters")
    @Column(length = 2000, columnDefinition = "TEXT")
    private String managerReview;

    @Size(max = 2000, message = "Peer review must not exceed 2000 characters")
    @Column(length = 2000, columnDefinition = "TEXT")
    private String peerReview;

    @DecimalMin(value = "0.0", inclusive = true, message = "Overall score must be at least 0.0")
    @DecimalMax(value = "100.0", inclusive = true, message = "Overall score must be at most 100.0")
    private Double overallScore;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String goals; // store JSON string (KRA/KPI with score weightages etc.)

    private boolean finalized;

    @OneToMany(mappedBy = "performanceReview", cascade = CascadeType.ALL)
    private List<ReviewAttachment> attachments;
}
