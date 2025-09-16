package com.quantumsoft.hrms.Human_Resource_Website.repository;

import com.quantumsoft.hrms.Human_Resource_Website.entity.Employee;
import com.quantumsoft.hrms.Human_Resource_Website.entity.Payroll;
import com.quantumsoft.hrms.Human_Resource_Website.entity.SalaryStructure;
import com.quantumsoft.hrms.Human_Resource_Website.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface SalaryStructureRepository extends JpaRepository<SalaryStructure, Long> {

    List<SalaryStructure> findByEmployee(Employee employee);

    Optional<SalaryStructure> findByEmployeeAndStatus(Employee employee, Status status);

//    @Query("SELECT s FROM SalaryStructure s " +
//            "WHERE s.employee = :employee " +
//            "AND :payrollDate BETWEEN s.effectiveFrom AND s.effectiveTo")
//    Optional<SalaryStructure> findApplicableStructureForDate(
//            @Param("employee") Employee employee,
//            @Param("payrollDate") LocalDate payrollDate);

    @Query("SELECT s FROM SalaryStructure s WHERE s.employee = :employee AND " +
            "(:date BETWEEN s.effectiveFrom AND s.effectiveTo OR (s.effectiveTo IS NULL AND :date >= s.effectiveFrom)) " +
            "AND s.status = com.quantumsoft.hrms.Human_Resource_Website.enums.Status.ACTIVE")
    Optional<SalaryStructure> findActiveStructureForDate(@Param("employee") Employee employee,
                                                         @Param("date") LocalDate date);

}
