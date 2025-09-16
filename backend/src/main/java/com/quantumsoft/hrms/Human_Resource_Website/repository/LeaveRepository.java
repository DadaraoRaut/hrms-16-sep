package com.quantumsoft.hrms.Human_Resource_Website.repository;

import com.quantumsoft.hrms.Human_Resource_Website.entity.Employee;
import com.quantumsoft.hrms.Human_Resource_Website.entity.Leave;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface LeaveRepository extends JpaRepository<Leave, Long> {
    @Query("SELECT COUNT(l) > 0 FROM Leave l " +
            "WHERE l.employee = :employee " +
            "AND :date BETWEEN l.startDate AND l.endDate " +
            "AND l.status = com.quantumsoft.hrms.Human_Resource_Website.enums.LeaveStatus.APPROVED")
    boolean existsApprovedLeaveByEmployeeAndDate(@Param("employee") Employee employee,
                                                 @Param("date") LocalDate date);


//    boolean existsByEmployeeAndDateAndApprovedTrue(Employee employee, LocalDate date);

}
