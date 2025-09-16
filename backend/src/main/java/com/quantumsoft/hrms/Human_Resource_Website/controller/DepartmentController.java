package com.quantumsoft.hrms.Human_Resource_Website.controller;

import com.quantumsoft.hrms.Human_Resource_Website.entity.Department;
import com.quantumsoft.hrms.Human_Resource_Website.service.DepartmentService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin("*")
@RestController
@Tag( name="Department API's", description = "All operation related to department")
@RequestMapping("/api/departments")
public class DepartmentController {

    @Autowired
    private DepartmentService departmentService;

    @PreAuthorize("hasAnyRole('HR','ADMIN')")
    @PostMapping(value = "/create", consumes = MediaType.APPLICATION_JSON_VALUE)
    public  ResponseEntity<Department> createDepartment(@Valid @RequestBody Department department){
        Department createDepartment=departmentService.createDepartment(department);
        return ResponseEntity.ok(createDepartment);
    }

    @PreAuthorize("hasAnyRole('HR','ADMIN')")
    @PutMapping(value= "/update/{id}",  consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Department> updateDepartment(@PathVariable Long id,
                                                       @Valid @RequestBody Department dept) {
        return ResponseEntity.ok(departmentService.updateDepartment(id, dept));
    }

    @PreAuthorize("hasAnyRole('HR','ADMIN','EMPLOYEE','MANAGER','FINANCE')")
    @GetMapping("/all")
    public ResponseEntity<List<Department>> getAllDepartment(){
        List<Department> department=departmentService.findAllDepartment();
        return ResponseEntity.ok(department);
    }
    @PreAuthorize("hasAnyRole('HR','ADMIN','EMPLOYEE','MANAGER','FINANCE')")
    @GetMapping("/get/{id}")
    public ResponseEntity<?> getDepartment(@PathVariable Long id){
        Department department=departmentService.findDepartmentById(id);
        return ResponseEntity.ok(department);
    }

    @PreAuthorize("hasAnyRole('HR','ADMIN')")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteDepartment(@PathVariable Long id) {
        departmentService.deleteDepartment(id);
        return ResponseEntity.ok("Department deleted successfully");
    }


}
