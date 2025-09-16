    package com.quantumsoft.hrms.Human_Resource_Website.entity;

    import jakarta.persistence.*;
    import jakarta.validation.constraints.*;
    import lombok.Getter;
    import lombok.Setter;
    import org.springframework.validation.annotation.Validated;

    import java.time.Instant;
    import java.time.LocalDateTime;


    @Entity
    @Table
    @Getter
    @Setter
    @Validated
    public class Otp {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @NotBlank(message = "Email is required")
        @Email(message = "Invalid email format")
        @Column(nullable = false)
        private String email;

        @NotBlank(message = "OTP code is required")
        @Size(min = 4, max = 8, message = "OTP must be between 4 and 8 characters")
        @Column(nullable = false)
        private  String otp;

        @NotNull(message = "Expiry date/time is required")
        @Future(message = "Expiry must be a future date/time")
        @Column(nullable = false)
        private LocalDateTime expiry;

    }
