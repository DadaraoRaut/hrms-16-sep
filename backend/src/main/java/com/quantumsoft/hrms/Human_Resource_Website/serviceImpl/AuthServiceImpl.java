//package com.quantumsoft.hrms.Human_Resource_Website.serviceImpl;
//
//import com.quantumsoft.hrms.Human_Resource_Website.entity.Admin;
//import com.quantumsoft.hrms.Human_Resource_Website.entity.User;
//import com.quantumsoft.hrms.Human_Resource_Website.enums.Role;
//import com.quantumsoft.hrms.Human_Resource_Website.enums.Status;
//import com.quantumsoft.hrms.Human_Resource_Website.repository.AdminRepository;
//import com.quantumsoft.hrms.Human_Resource_Website.service.AdminService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Service;
//
//import java.time.LocalDateTime;
//
//@Service
//public class AdminServiceImpl implements AdminService {
//
//    @Autowired
//    private AdminRepository adminRepository;
//
//    @Autowired
//    private PasswordEncoder passwordEncoder;
//
//    @Override
//    public void createDefaultAdmin() {
//        if (!adminRepository.existsByRole(Role.ADMIN)) {
//            Admin admin = new Admin();
//            admin.setUsername("admin");
//            admin.setEmail("admin@hrms.com");
//            admin.setPassword(passwordEncoder.encode("Admin@123"));
//            admin.setRole(Role.ADMIN);
//            admin.setStatus(Status.ACTIVE);
//            admin.setCreatedAt(LocalDateTime.now());
//            admin.setUpdatedAt(LocalDateTime.now());
//            adminRepository.save(admin);
//        }
//
//    }
//}
