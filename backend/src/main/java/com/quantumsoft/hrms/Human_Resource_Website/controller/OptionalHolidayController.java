package com.quantumsoft.hrms.Human_Resource_Website.controller;

import com.quantumsoft.hrms.Human_Resource_Website.entity.OptionalHoliday;
import com.quantumsoft.hrms.Human_Resource_Website.repository.OptionalHolidayRepository;
import com.quantumsoft.hrms.Human_Resource_Website.service.OptionalHolidayService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@CrossOrigin("*")
@Tag(name = "Optional Holiday API's", description = "HR/Admin Handler for Optional Holidays")
@RestController
@RequestMapping("/api/optional-holidays")
public class OptionalHolidayController {

    @Autowired
    private OptionalHolidayService optionalHolidayService;

    @PostMapping("/create")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR', 'MANAGER')")
    public ResponseEntity<OptionalHoliday> createOptionalHoliday(@RequestBody OptionalHoliday optionalHoliday) {
        return ResponseEntity.ok(optionalHolidayService.createOptionalHoliday(optionalHoliday));
    }
    @PutMapping("/update/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR', 'MANAGER')")
    public ResponseEntity<OptionalHoliday> updateOptionalHoliday(@PathVariable Long id, @RequestBody OptionalHoliday optionalHoliday) {
        return ResponseEntity.ok(optionalHolidayService.updateOptionalHoliday(id, optionalHoliday));
    }
    @GetMapping("/search/{id}")
    public ResponseEntity<OptionalHoliday> getOptionalHolidayById(@PathVariable Long id) {
        return ResponseEntity.ok(optionalHolidayService.getOptionalHolidayById(id));
    }
    @GetMapping("/all")
    public ResponseEntity<List<OptionalHoliday>> getAllOptionalHolidays() {
        return ResponseEntity.ok(optionalHolidayService.getAllOptionalHolidays());
    }
    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR', 'MANAGER')")
    public ResponseEntity<String> deleteOptionalHoliday(@PathVariable Long id) {
        optionalHolidayService.deleteOptionalHoliday(id);
        return ResponseEntity.ok("Deleted Successfully");
    }
}
