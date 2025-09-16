package com.quantumsoft.hrms.Human_Resource_Website.service;

public interface AdminService {

    public String login(String username, String password);

    void createDefaultAdmin();
}
