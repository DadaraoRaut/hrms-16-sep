package com.quantumsoft.hrms.Human_Resource_Website.controller;

import com.quantumsoft.hrms.Human_Resource_Website.service.AdminService;
import com.quantumsoft.hrms.Human_Resource_Website.service.AuditLogService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Map;

@CrossOrigin("*")
@RestController
@Tag( name="Admin API's", description = "Admin login and logout api")
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private AdminService authService;

    @Autowired
    private AuditLogService auditLogService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody Map<String, String> request) {
        String username = request.get("username");
        String password = request.get("password");
        String token = authService.login(username, password);
        return ResponseEntity.ok(Collections.singletonMap("token", token));
    }


    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestBody Map<String, String> request) {
        String username = request.get("username");

        auditLogService.logEvent(username, "LOGOUT");

        return ResponseEntity.ok("Logout successful.");
    }
}
