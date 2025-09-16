package com.quantumsoft.hrms.Human_Resource_Website.controller;

import com.quantumsoft.hrms.Human_Resource_Website.entity.Leave;
import com.quantumsoft.hrms.Human_Resource_Website.service.LeaveService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@CrossOrigin("*")
@RestController
@Tag(name="Leave")
@RequestMapping("/api/leaves")
public class LeaveController {
    @Autowired
    private LeaveService leaveService;

    @PostMapping("/apply")
    public ResponseEntity<Leave> applyLeave(@RequestBody Leave leave) {
        return ResponseEntity.ok(leaveService.applyLeave(leave));
    }

    @GetMapping("/all_leave")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR', 'MANAGER')")
    public ResponseEntity<List<Leave>> getAllLeaves() {
        return ResponseEntity.ok(leaveService.getAllLeaves());
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<Leave> getLeaveById(@PathVariable Long id) {
        return ResponseEntity.ok(leaveService.getLeaveById(id));
    }

    @PutMapping("/approve/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR', 'MANAGER')")
    public ResponseEntity<Leave> approveLeave(@PathVariable Long id, @RequestBody String comments) {
        return ResponseEntity.ok(leaveService.approveLeave(id, comments));
    }

    @PutMapping("/reject/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR', 'MANAGER')")
    public ResponseEntity<Leave> rejectLeave(@PathVariable Long id, @RequestBody String comments) {
        return ResponseEntity.ok(leaveService.rejectLeave(id, comments));
    }
}