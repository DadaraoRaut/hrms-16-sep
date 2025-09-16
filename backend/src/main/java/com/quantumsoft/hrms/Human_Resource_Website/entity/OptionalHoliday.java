package com.quantumsoft.hrms.Human_Resource_Website.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OptionalHoliday {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long holidayId;

    @NotBlank(message = "Holiday name is required")
    @Size(max = 25, message = "Holiday name must not exceed 25 characters")
    @Column(nullable = false)
    private String name;

    @NotNull(message = "Holiday date is required")
    @FutureOrPresent(message = "Holiday date must be today or in the future")
    @Column(nullable = false)
    private LocalDate date;

    @Size(max = 200, message = "Description must not exceed 200 characters")
    private String description;

    @Size(max = 100, message = "Region or religion must not exceed 100 characters")
    private String regionOrReligion;
}

