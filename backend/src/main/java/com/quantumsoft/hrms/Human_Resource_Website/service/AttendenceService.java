package com.quantumsoft.hrms.Human_Resource_Website.service;

import com.quantumsoft.hrms.Human_Resource_Website.entity.Attendance;
import com.quantumsoft.hrms.Human_Resource_Website.entity.ClockInRequest;
import com.quantumsoft.hrms.Human_Resource_Website.enums.Mode;
import com.quantumsoft.hrms.Human_Resource_Website.enums.WorkFrom;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface AttendenceService {

    void clockIn(ClockInRequest request);

    void clockOut();

    void generateCsvReport(HttpServletResponse response, Long empId, String from, String to);

    Map<String, Object> getMonthlyStatus(Long empId, int month, int year);

    void generatePdfReport(HttpServletResponse response,
                           Long empId,
                           String department,
                           String fromDate,
                           String toDate) throws IOException;


    void requestRegularization(String date, String reason);

    void approveRegularization(Long attendanceId);

    void rejectRegularization(Long attendanceId, String reason);


    List<Attendance> getAllRegularizationRequests();

    List<Attendance> getAttendanceByEmployee(Long empId);
}
