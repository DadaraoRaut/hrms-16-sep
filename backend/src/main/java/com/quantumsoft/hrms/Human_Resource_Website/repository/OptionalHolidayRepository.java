package com.quantumsoft.hrms.Human_Resource_Website.repository;

import com.quantumsoft.hrms.Human_Resource_Website.entity.OptionalHoliday;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface OptionalHolidayRepository extends JpaRepository<OptionalHoliday, Long> {
    boolean existsByDate(LocalDate date);
}
