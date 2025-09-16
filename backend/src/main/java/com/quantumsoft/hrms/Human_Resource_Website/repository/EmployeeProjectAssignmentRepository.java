package com.quantumsoft.hrms.Human_Resource_Website.repository;

import com.quantumsoft.hrms.Human_Resource_Website.entity.EmployeeProjectAssignment;
import com.quantumsoft.hrms.Human_Resource_Website.enums.AssignmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
@Repository
public interface EmployeeProjectAssignmentRepository extends JpaRepository<EmployeeProjectAssignment, Long> {

    List<EmployeeProjectAssignment> findByEmployee_EmpId(Long empId);

    List<EmployeeProjectAssignment> findByEmployee_EmpIdOrderByAssignedDateDesc(Long empId);

    List<EmployeeProjectAssignment> findByProject_ProjectId(Long projectId);

    List<EmployeeProjectAssignment> findByStatus(AssignmentStatus status);

    List<EmployeeProjectAssignment> findByReleaseDateBeforeAndStatus(LocalDate date, AssignmentStatus status);

    @Query("SELECT e FROM EmployeeProjectAssignment e WHERE e.releaseDate <= CURRENT_DATE AND e.status = 'ACTIVE'")
    List<EmployeeProjectAssignment> findExpiredAssignments();


    @Query("SELECT e FROM EmployeeProjectAssignment e WHERE e.releaseDate BETWEEN CURRENT_DATE AND :date AND e.status = 'ACTIVE'")
    List<EmployeeProjectAssignment> findAssignmentsEndingSoon(@Param("date") LocalDate date);
}
