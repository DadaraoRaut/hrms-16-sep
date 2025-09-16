package com.quantumsoft.hrms.Human_Resource_Website.controller;

import com.quantumsoft.hrms.Human_Resource_Website.service.AdminService;
import com.quantumsoft.hrms.Human_Resource_Website.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.CrossOrigin;

@CrossOrigin("*")
@Component
public class DataLoader implements ApplicationRunner {

    @Autowired
    private AdminService adminService;

    @Override
    public void run(ApplicationArguments args){
        adminService.createDefaultAdmin();
    }
}
