package com.quantumsoft.hrms.Human_Resource_Website.controller;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.quantumsoft.hrms.Human_Resource_Website.entity.Employee;
import com.quantumsoft.hrms.Human_Resource_Website.enums.Role;
import com.quantumsoft.hrms.Human_Resource_Website.service.EmployeeService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@CrossOrigin("*")
@RestController
@Tag( name="Employee API's", description = "All operation related to employee")
@RequestMapping("/api/employees")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;


    @Autowired
    private ObjectMapper objectMapper;


    private static final Logger log = LoggerFactory.getLogger(EmployeeController.class);


    @PostMapping(
            path = "/create",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @PreAuthorize("hasAnyRole('HR', 'ADMIN')")
    public ResponseEntity<Employee> createEmployee(
            @RequestPart("employee") String employeeJson,
            @RequestPart("photo") MultipartFile photo
    ) throws JsonProcessingException {
        Employee employee = objectMapper.readValue(employeeJson, Employee.class);
        String email = employee.getEmail();  // get email from parsed employee object
        return ResponseEntity.ok(employeeService.createEmployee(employee, photo, email));
    }


    @PutMapping(path = "/update/{empId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAnyRole('HR','ADMIN')")
    public ResponseEntity<Employee> updateEmployee(
            @PathVariable Long empId,
            @RequestPart("employee") String employeeJson,              // Accept raw JSON
            @RequestPart(value = "photo", required = false) MultipartFile photo
    ) throws JsonProcessingException {

        log.debug("Raw JSON: {}", employeeJson); // MUST include full JSON

        // Use the Spring-injected, JavaTimeModule-configured ObjectMapper
        Employee employee = objectMapper.readValue(employeeJson, Employee.class);

        return ResponseEntity.ok(employeeService.updateEmployee(empId, employee, photo));
    }


    @GetMapping("/get/{empId}")
    public ResponseEntity<Employee> getEmployee(@PathVariable Long empId) {
        return ResponseEntity.ok(employeeService.getEmployeeById(empId));
    }

    @DeleteMapping("/delete/{empId}")
    @PreAuthorize("hasAnyRole('HR', 'ADMIN')")
    public ResponseEntity<String> deleteEmployee(@PathVariable Long empId) {
        employeeService.deleteEmployee(empId);
        return ResponseEntity.ok("Employee deleted successfully");
    }

    @GetMapping("/all")
    public ResponseEntity<List<Employee>> getAllEmployees() {
        return ResponseEntity.ok(employeeService.findAllEmployee());
    }

    @GetMapping("/by-role/manager")
    @PreAuthorize("hasAnyRole('HR','ADMIN')")
    public ResponseEntity<List<Employee>> getManagers() {
        List<Employee> managers = employeeService.getEmployeesByRoleManager(Role.MANAGER);
        return ResponseEntity.ok(managers);
    }
}
