package com.quantumsoft.hrms.Human_Resource_Website.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewAttachment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "File name is required")
    @Size(max = 255, message = "File name must not exceed 255 characters")
    private String fileName;

    @NotBlank(message = "File type is required")
    @Size(max = 100, message = "File type must not exceed 100 characters")
    private String fileType;

    @NotNull(message = "File data is required")
    @Lob
    @Column(length = 10485760) // e.g. 10MB max
    private byte[] data;

    @NotNull(message = "Performance review reference is required")
    @ManyToOne
    @JoinColumn(name = "review_id")
    private PerformanceReview performanceReview;
}

