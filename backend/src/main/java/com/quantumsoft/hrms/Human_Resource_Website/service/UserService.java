package com.quantumsoft.hrms.Human_Resource_Website.service;

import com.quantumsoft.hrms.Human_Resource_Website.enums.Role;
import com.quantumsoft.hrms.Human_Resource_Website.enums.Status;
import com.quantumsoft.hrms.Human_Resource_Website.entity.User;

import java.util.List;

public interface UserService {

    public void createUser(User user);

    public List<User> getUsersByRole(Role role);

    public  void updateStatus(Long userID, Status status);

    String login(String username, String password);

    void sendOtp(String email);

    void resetPassword(String email, String otp, String newPassword);

    User roleChangeByEmail(String email, Role newRole);

    List<User> getAllUsers();

//    List<User> getUsersWithEmployee();
}
