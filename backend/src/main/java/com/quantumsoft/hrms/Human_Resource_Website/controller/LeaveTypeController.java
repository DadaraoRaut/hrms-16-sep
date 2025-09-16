package com.quantumsoft.hrms.Human_Resource_Website.controller;


import com.quantumsoft.hrms.Human_Resource_Website.entity.LeaveType;
import com.quantumsoft.hrms.Human_Resource_Website.exception.ResourceNotFoundException;
import com.quantumsoft.hrms.Human_Resource_Website.service.LeaveTypeService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/leave-types")
@Tag(name="LeaveType")
public class LeaveTypeController {
    @Autowired
    private LeaveTypeService leaveTypeService;

    @PostMapping("/create")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR')")
    public ResponseEntity<LeaveType> createLeaveType(@Valid @RequestBody LeaveType leaveType) {
        return ResponseEntity.ok(leaveTypeService.createLeaveType(leaveType));
    }
    @GetMapping("/all")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR', 'MANAGER')")
    public ResponseEntity<List<LeaveType>> getAllLeaveTypes() {
        return ResponseEntity.ok(leaveTypeService.getAllLeaveTypes());
    }
    @GetMapping("/get/{leaveTypeId}")
    public ResponseEntity<LeaveType> getLeaveTypeById(@PathVariable Long leaveTypeId) {
        return ResponseEntity.ok(leaveTypeService.getLeaveTypeById(leaveTypeId));
    }
    @PutMapping("/update/{leaveTypeId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR')")
    public ResponseEntity<LeaveType> updateLeaveType(@PathVariable Long leaveTypeId, @Valid @RequestBody LeaveType leaveType) {

        LeaveType updated = leaveTypeService.updateLeaveType(leaveTypeId, leaveType);
        if (updated != null) {
            return ResponseEntity.ok(updated);
        } else {
            throw new ResourceNotFoundException("LeaveType not found with id: " + leaveTypeId);
        }
    }
    @DeleteMapping("/delete/{leaveTypeId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR')")
    public ResponseEntity<String> deleteLeaveType(@PathVariable Long leaveTypeId) {
        leaveTypeService.deleteLeaveType(leaveTypeId);
        return ResponseEntity.noContent().build();
    }
}

