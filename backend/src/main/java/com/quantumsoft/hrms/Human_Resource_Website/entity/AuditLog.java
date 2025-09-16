package com.quantumsoft.hrms.Human_Resource_Website.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table
@Getter
@Setter
public class AuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Username is required")
    private String username;

    @NotBlank(message = "Action is required")
    private String action;    // LOGIN, LOGOUT, PASSWORD_CHANGE etc.

//    private String ipAddress; // Optional
    @NotNull(message = "Timestamp is required")
    private LocalDateTime timestamp;
}
