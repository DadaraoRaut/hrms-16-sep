package com.quantumsoft.hrms.Human_Resource_Website.serviceImpl;


import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.lowagie.text.Document;
import com.lowagie.text.PageSize;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;

import java.awt.Color;
import com.quantumsoft.hrms.Human_Resource_Website.controller.AttendanceSocketController;
import com.quantumsoft.hrms.Human_Resource_Website.entity.Attendance;
import com.quantumsoft.hrms.Human_Resource_Website.entity.ClockInRequest;
import com.quantumsoft.hrms.Human_Resource_Website.entity.Employee;
import com.quantumsoft.hrms.Human_Resource_Website.entity.User;
import com.quantumsoft.hrms.Human_Resource_Website.enums.AttendanceStatus;
import com.quantumsoft.hrms.Human_Resource_Website.enums.Mode;
import com.quantumsoft.hrms.Human_Resource_Website.enums.RegularizationStatus;
import com.quantumsoft.hrms.Human_Resource_Website.enums.WorkFrom;
import com.quantumsoft.hrms.Human_Resource_Website.exception.ResourceNotFoundException;
import com.quantumsoft.hrms.Human_Resource_Website.repository.*;
import com.quantumsoft.hrms.Human_Resource_Website.service.AttendenceService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

import static com.quantumsoft.hrms.Human_Resource_Website.enums.WorkFrom.OFFICE;

@Service
public class AttendanceServiceImpl implements AttendenceService {
    @Autowired
    private AttendanceRepository attendanceRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LeaveRepository leaveRepository;

    @Autowired
    private AttendanceSocketController attendanceSocketController;

    @Autowired
    private OptionalHolidayRepository optionalHolidayRepository;


    // Example office location coordinates
    private final double officeLatitude = 12.9716;
    private final double officeLongitude = 77.5946;
    private final double allowedRadiusMeters = 100;


//    @Transactional
//    @Override
//    public void clockIn(ClockInRequest request) {
//        LocalDate today = LocalDate.now();
//
//        String username = getCurrentUsername();
//        User user = userRepository.findByUsernameCaseSensitive(username)
//                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + username));
//
//        Employee employee = employeeRepository.findByEmail(user.getEmail())
//                .orElseThrow(() -> new ResourceNotFoundException("Employee not found for email: " + user.getEmail()));
//
//
//        // Check if already clocked in
//        if (attendanceRepository.existsByEmployee_EmpIdAndDate(employee.getEmpId(), today)) {
//            throw new IllegalArgumentException("Employee already clocked in today");
//        }
//
//        // Extract fields from request properly
//        Long employeeId = request.getEmployeeId();
//        WorkFrom workFrom = request.getWorkFrom();
//        Mode mode = request.getMode();
//        Double latitude = request.getLatitude();
//        Double longitude = request.getLongitude();
//
//        // Validate geolocation
////        double distance = calculateDistance(latitude, longitude, officeLatitude, officeLongitude);
////
////            if (distance > allowedRadiusMeters) {
////                throw new IllegalArgumentException("Clock-in location is outside allowed office radius");
////            }
//
//        // Validate geolocation only for OFFICE mode
//        if (workFrom == WorkFrom.OFFICE) {
//            if (latitude == null || longitude == null) {
//                throw new IllegalArgumentException("Location coordinates are required for office attendance");
//            }
//
//            double distance = calculateDistance(latitude, longitude, officeLatitude, officeLongitude);
//            if (distance > allowedRadiusMeters) {
//                throw new IllegalArgumentException("Clock-in location is outside allowed office radius");
//            }
//        } else {
//            // For REMOTE work, skip location check and set lat/lon as null if not provided
//            if (latitude == null) latitude = 0.0;
//            if (longitude == null) longitude = 0.0;
//        }
//
//
//
//        // Determine status based on time
//        LocalTime now = LocalTime.now();
//
//        Attendance attendance = new Attendance();
//        attendance.setEmployee(employee);
//        attendance.setDate(today);
//        attendance.setClockInTime(now);
//        attendance.setWorkFrom(workFrom);
//        attendance.setMode(mode);
//        attendance.setLatitude(latitude);
//        attendance.setLongitude(longitude);
//        attendance.setStatus(AttendanceStatus.PRESENT);
//
//        attendanceRepository.save(attendance);
//
//        System.out.println("Notifying managers via WebSocket...");
//        attendanceSocketController.notifyManagers(
//                employee.getFirstName() + " clocked in at " + attendance.getClockInTime()
//        );
//    }
@Transactional
@Override
public void clockIn(ClockInRequest request) {
    LocalDate today = LocalDate.now();

    // Get logged-in user
    String username = getCurrentUsername();
    User user = userRepository.findByUsernameCaseSensitive(username)
            .orElseThrow(() -> new ResourceNotFoundException("User not found: " + username));

    Employee employee = employeeRepository.findByEmail(user.getEmail())
            .orElseThrow(() -> new ResourceNotFoundException("Employee not found for email: " + user.getEmail()));

    // Check if already clocked in
    if (attendanceRepository.existsByEmployee_EmpIdAndDate(employee.getEmpId(), today)) {
        throw new IllegalArgumentException("Employee already clocked in today");
    }

    // Extract fields from request (no employeeId anymore)
    WorkFrom workFrom = request.getWorkFrom();
    Mode mode = request.getMode();
    Double latitude = request.getLatitude();
    Double longitude = request.getLongitude();

    // Validate geolocation only for OFFICE mode
    if (workFrom == WorkFrom.OFFICE) {
        if (latitude == null || longitude == null) {
            throw new IllegalArgumentException("Location coordinates are required for office attendance");
        }

        double distance = calculateDistance(latitude, longitude, officeLatitude, officeLongitude);
        if (distance > allowedRadiusMeters) {
            throw new IllegalArgumentException("Clock-in location is outside allowed office radius");
        }
    } else {
        // For REMOTE, set defaults if not provided
        if (latitude == null) latitude = 0.0;
        if (longitude == null) longitude = 0.0;
    }

    // Determine status based on time
    LocalTime now = LocalTime.now();

    Attendance attendance = new Attendance();
    attendance.setEmployee(employee);
    attendance.setDate(today);
    attendance.setClockInTime(now);
    attendance.setWorkFrom(workFrom);
    attendance.setMode(mode);
    attendance.setLatitude(latitude);
    attendance.setLongitude(longitude);
    attendance.setStatus(AttendanceStatus.PRESENT);

    attendanceRepository.save(attendance);

    // WebSocket notify managers
    attendanceSocketController.notifyManagers(
            employee.getFirstName() + " clocked in at " + attendance.getClockInTime()
    );
}

    private String getCurrentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof UserDetails) {
                return ((UserDetails) principal).getUsername();
            }
        }
        throw new RuntimeException("Unauthorized or invalid user.");
    }
    @Transactional
    @Override
    public void clockOut() {
        String username = getCurrentUsername();
        User user = userRepository.findByUsernameCaseSensitive(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + username));

        Employee employee = employeeRepository.findByEmail(user.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found for email: " + user.getEmail()));

        LocalDate today = LocalDate.now();

        Attendance attendance = attendanceRepository.findByEmployee_EmpIdAndDate(employee.getEmpId(), today)
                .orElseThrow(() -> new IllegalArgumentException("No clock-in record found for today"));

        if (attendance.getClockOutTime() != null) {
            throw new IllegalArgumentException("Already clocked out today");
        }

        LocalTime clockOutTime = LocalTime.now();
        attendance.setClockOutTime(clockOutTime);

        // Calculate total hours worked
        if (attendance.getClockInTime() != null) {
            Duration duration = Duration.between(attendance.getClockInTime(), clockOutTime);
            double hours = duration.toMinutes() / 60.0;
            attendance.setTotalHoursWorked(roundToTwoDecimalPlaces(hours));
        }

        attendanceRepository.save(attendance);

        System.out.println("Notifying managers via WebSocket...");
        attendanceSocketController.notifyManagers(
                employee.getFirstName() + " clocked out at " + attendance.getClockOutTime()
        );
    }
    private double roundToTwoDecimalPlaces(double value) {
        return Math.round(value * 100.0) / 100.0;
    }

    // Haversine distance calculation (meters)
    private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371000; // Radius of earth in meters
        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c;
    }

    @Override
    public void generateCsvReport(HttpServletResponse response, Long empId, String from, String to) {
        List<Attendance> records = filterAttendance(empId, from, to);
        response.setContentType("text/csv");
        response.setHeader("Content-Disposition", "attachment; filename=attendance_report.csv");

        try (PrintWriter writer = response.getWriter()) {
            writer.println("Employee ID,Date,Clock-In,Clock-Out,Status,Mode,Latitude,Longitude");
            for (Attendance att : records) {
                writer.printf("%s,%s,%s,%s,%s,%s,%s,%s\n",
                        att.getEmployee().getEmpId(),
                        att.getDate(),
                        att.getClockInTime(),
                        att.getClockOutTime(),
                        att.getStatus(),
                        att.getMode(),
                        att.getLatitude(),
                        att.getLongitude());
            }
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void generatePdfReport(HttpServletResponse response, Long empId, String department, String fromDate, String toDate) throws IOException {
        LocalDate start = (fromDate != null) ? LocalDate.parse(fromDate) : LocalDate.now().withDayOfMonth(1);
        LocalDate end = (toDate != null) ? LocalDate.parse(toDate) : LocalDate.now();

        List<Attendance> records = attendanceRepository.findWithEmployeeAndDepartmentFiltered(empId, department, start, end);

        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=attendance_report.pdf");

        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, response.getOutputStream());
        document.open();

        Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, Color.BLUE);
        Paragraph title = new Paragraph("Attendance Report", titleFont);
        title.setAlignment(Paragraph.ALIGN_CENTER);
        title.setSpacingAfter(20);
        document.add(title);

        PdfPTable table = new PdfPTable(5);
        table.setWidthPercentage(100f);
        table.setSpacingBefore(10);

        writeTableHeader(table);
        writeTableData(table, records);

        document.add(table);
        document.close();
    }

    @Override
    public void requestRegularization(String date, String reason) {
        LocalDate targetDate = LocalDate.parse(date);

        String username = getCurrentUsername();
        User user = userRepository.findByUsernameCaseSensitive(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + username));

        Employee employee = employeeRepository.findByEmail(user.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found for email: " + user.getEmail()));

        Attendance attendance = attendanceRepository
                .findByEmployee_EmpIdAndDate(employee.getEmpId(), targetDate)
                .orElseThrow(() -> new ResourceNotFoundException("Attendance record not found"));

        attendance.setRegularizationStatus(RegularizationStatus.PENDING);
        attendance.setRegularizationReason(reason);

        attendanceRepository.save(attendance);
    }


    @Override
    public void approveRegularization(Long attendanceId) {
        Attendance attendance = attendanceRepository.findById(attendanceId)
                .orElseThrow(() -> new ResourceNotFoundException("Attendance not found"));

        attendance.setApprovedClockInTime(LocalTime.of(9, 30));
        attendance.setApprovedClockOutTime(LocalTime.of(18, 30));
        attendance.setClockInTime(LocalTime.of(9, 30));
        attendance.setClockOutTime(LocalTime.of(18, 30));
        attendance.setTotalHoursWorked(9.0);
        attendance.setStatus(AttendanceStatus.PRESENT);
        attendance.setRegularizationStatus(RegularizationStatus.APPROVED);

        attendanceRepository.save(attendance);

    }

    @Override
    public void rejectRegularization(Long attendanceId, String reason) {
        Attendance attendance = attendanceRepository.findById(attendanceId)
                .orElseThrow(() -> new ResourceNotFoundException("Attendance not found"));

        attendance.setRegularizationStatus(RegularizationStatus.REJECTED);
        attendance.setRegularizationReason(reason);

        attendanceRepository.save(attendance);

    }

    @Override
    public List<Attendance> getAllRegularizationRequests() {
        return attendanceRepository.findByRegularizationStatus(RegularizationStatus.PENDING);
    }

    @Override
    public List<Attendance> getAttendanceByEmployee(Long empId) {
        return attendanceRepository.findByEmployee_EmpId(empId);
    }

//    @Override
//    public void deleteAttendance(Long attendanceId) {
//        Attendance attendance = attendanceRepository.findById(attendanceId)
//                .orElseThrow(() -> new ResourceNotFoundException("Attendance not found with ID: " + attendanceId));
//        attendanceRepository.delete(attendance);
//    }

    private void writeTableHeader(PdfPTable table) {
        PdfPCell cell = new PdfPCell();
        cell.setBackgroundColor(Color.LIGHT_GRAY);
        cell.setPadding(5);

        Font font = FontFactory.getFont(FontFactory.HELVETICA);

        cell.setPhrase(new Phrase("Employee Name", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Department", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Date", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Status", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Total Hours", font));
        table.addCell(cell);
    }

    private void writeTableData(PdfPTable table, List<Attendance> records) {
        for (Attendance att : records) {
            Employee emp = att.getEmployee();
            String fullName = emp.getFirstName() + " " + emp.getLastName();
            String dept = (emp.getDepartment() != null) ? emp.getDepartment().getName() : "N/A";

            table.addCell(fullName);
            table.addCell(dept);
            table.addCell(att.getDate().toString());
            table.addCell(att.getStatus().name());
            table.addCell(att.getTotalHoursWorked() != null ? att.getTotalHoursWorked().toString() : "");
        }
    }

    @Override
    public Map<String, Object> getMonthlyStatus(Long empId, int month, int year) {
        List<Attendance> records = attendanceRepository.findByEmployeeAndMonth(empId, month, year);

        Map<String, Long> summary = records.stream()
                .collect(Collectors.groupingBy(
                        a -> a.getStatus().name(),
                        Collectors.counting()
                ));

        List<Map<String, String>> dailyStatus = records.stream()
                .map(a -> Map.of(
                        "date", a.getDate().toString(),
                        "status", a.getStatus().name()
                ))
                .collect(Collectors.toList());

        Map<String, Object> result = new HashMap<>();
        result.put("employeeId", empId);
        result.put("month", String.format("%04d-%02d", year, month));
        result.put("summary", summary);
        result.put("dailyStatus", dailyStatus);

        return result;
    }


    private List<Attendance> filterAttendance(Long empId, String from, String to) {
        LocalDate start = from != null ? LocalDate.parse(from) : LocalDate.now().minusMonths(1);
        LocalDate end = to != null ? LocalDate.parse(to) : LocalDate.now();

        return (empId != null) ?
                attendanceRepository.findByEmployee_EmpIdAndDateBetween(empId, start, end) :
                attendanceRepository.findAll().stream()
                        .filter(a -> !a.getDate().isBefore(start) && !a.getDate().isAfter(end))
                        .toList();
    }

//@Scheduled(cron = "0 01 03 * * *", zone = "Asia/Kolkata")// Runs daily at 11:01 AM
    public void autoMarkAbsentees() {
        LocalDate today = LocalDate.now();

        DayOfWeek dayOfWeek = today.getDayOfWeek();

        // optional Holiday check
        if (optionalHolidayRepository.existsByDate(today)) {
            List<Employee> allEmployees = employeeRepository.findAll();
            for (Employee employee : allEmployees) {
                Long empId = employee.getEmpId();

                boolean alreadyExists = attendanceRepository.existsByEmployee_EmpIdAndDate(empId, today);
                if (alreadyExists) continue;

                Attendance attendance = new Attendance();
                attendance.setEmployee(employee);
                attendance.setDate(today);
                attendance.setStatus(AttendanceStatus.HOLIDAY);

                attendanceRepository.save(attendance);
            }

            System.out.println("Attendance marked as HOLIDAY for all employees on optional holiday: " + today);
            return;

        }
            // For saturday/Sunday and Leave
            List<Employee> allEmployees = employeeRepository.findAll(); // Filter by active if needed

        for (Employee employee : allEmployees) {
            Long empId = employee.getEmpId();

            boolean hasClockIn = attendanceRepository.existsByEmployee_EmpIdAndDate(empId, today);
            if (hasClockIn) continue;

            Attendance attendance = new Attendance();
            attendance.setEmployee(employee);
            attendance.setDate(today);

            if (dayOfWeek == DayOfWeek.SATURDAY|| dayOfWeek == DayOfWeek.SUNDAY) {
                attendance.setStatus(AttendanceStatus.OPTIONAL);
            } else {

                boolean isOnLeave = leaveRepository.existsApprovedLeaveByEmployeeAndDate(employee, today);

                attendance.setStatus(isOnLeave ? AttendanceStatus.ON_LEAVE : AttendanceStatus.ABSENT);
            }
            attendanceRepository.save(attendance);
        }
        System.out.println("Auto attendance marking completed for " + today);
    }

}

