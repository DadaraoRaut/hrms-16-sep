package com.quantumsoft.hrms.Human_Resource_Website.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LeaveType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long leaveTypeId;

    @NotBlank(message = "Leave type name is required")
    @Size(max = 100, message = "Leave type name must not exceed 100 characters")
    @Column(nullable = false, unique = true)
    private String name; // Sick, Casual, Earned

    @Column(name = "carry_forward", nullable = false)
    private boolean carryForward;

    @Column(name = "encashable", nullable = false)
    private boolean encashable;

    @Min(value = 1, message = "Max days per year must be at least 1")
    @Max(value = 365, message = "Max days per year cannot exceed 365")
    @Column(name = "max_per_year", nullable = false)
    private int maxDaysPerYear;

    @OneToMany(mappedBy = "leaveType")
    private List<Leave> leaves;

    @Size(max = 100, message = "Approval flow description must not exceed 100 characters")
    @Column(name = "approval_flow")
    private String approvalFlow;

    @Size(max = 200, message = "Description must not exceed 200 characters")
    @Column(name="description")
    private  String description;


}
