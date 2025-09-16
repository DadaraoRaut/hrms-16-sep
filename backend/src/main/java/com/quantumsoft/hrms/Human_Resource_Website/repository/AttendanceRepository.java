package com.quantumsoft.hrms.Human_Resource_Website.repository;


import com.quantumsoft.hrms.Human_Resource_Website.entity.Attendance;
import com.quantumsoft.hrms.Human_Resource_Website.enums.AttendanceStatus;
import com.quantumsoft.hrms.Human_Resource_Website.enums.RegularizationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface AttendanceRepository extends JpaRepository <Attendance, Long> {

    boolean existsByEmployee_EmpIdAndDate(Long empId, LocalDate date);

    Optional<Attendance> findByEmployee_EmpIdAndDate(Long empId, LocalDate date);

    List<Attendance> findByEmployee_EmpIdAndDateBetween(Long empId, LocalDate startDate, LocalDate endDate);

    @Query("SELECT a FROM Attendance a WHERE a.employee.empId = :empId AND MONTH(a.date) = :month AND YEAR(a.date) = :year")
    List<Attendance> findByEmployeeAndMonth(@Param("empId") Long empId,
                                            @Param("month") int month,
                                            @Param("year") int year);

    @Query("SELECT a FROM Attendance a " +
            "JOIN FETCH a.employee e " +
            "JOIN FETCH e.department d " +
            "WHERE (:empId IS NULL OR e.empId = :empId) " +
            "AND (:deptName IS NULL OR LOWER(d.name) = LOWER(:deptName)) " +
            "AND a.date BETWEEN :from AND :to")
    List<Attendance> findWithEmployeeAndDepartmentFiltered(
            @Param("empId") Long empId,
            @Param("deptName") String departmentName,
            @Param("from") LocalDate from,
            @Param("to") LocalDate to
    );


    List<Attendance> findByRegularizationStatus(RegularizationStatus regularizationStatus);

    List<Attendance> findByEmployee_EmpId(Long empId);
//    @Query("SELECT a FROM Attendance a WHERE " +
//            "(:empId IS NULL OR a.employee.empId = :empId) AND " +
//            "(:department IS NULL OR a.employee.department.name = :department) AND " +
//            "(:fromDate IS NULL OR a.date >= :fromDate) AND " +
//            "(:toDate IS NULL OR a.date <= :toDate)")
//    List<Attendance> findByFilters(UUID empId, String department, LocalDate fromDate, LocalDate toDate);
}
