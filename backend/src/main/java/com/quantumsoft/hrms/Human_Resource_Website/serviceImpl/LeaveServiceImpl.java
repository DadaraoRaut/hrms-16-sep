package com.quantumsoft.hrms.Human_Resource_Website.serviceImpl;


import com.quantumsoft.hrms.Human_Resource_Website.entity.Employee;
import com.quantumsoft.hrms.Human_Resource_Website.entity.Leave;
import com.quantumsoft.hrms.Human_Resource_Website.entity.LeaveType;
import com.quantumsoft.hrms.Human_Resource_Website.entity.User;
import com.quantumsoft.hrms.Human_Resource_Website.enums.LeaveStatus;
import com.quantumsoft.hrms.Human_Resource_Website.exception.ResourceNotFoundException;
import com.quantumsoft.hrms.Human_Resource_Website.repository.EmployeeRepository;
import com.quantumsoft.hrms.Human_Resource_Website.repository.LeaveRepository;
import com.quantumsoft.hrms.Human_Resource_Website.repository.LeaveTypeRepository;
import com.quantumsoft.hrms.Human_Resource_Website.repository.UserRepository;
import com.quantumsoft.hrms.Human_Resource_Website.service.LeaveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LeaveServiceImpl implements LeaveService {

    @Autowired
    LeaveRepository leaveRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LeaveTypeRepository leaveTypeRepository;


    @Override
    public Leave applyLeave(Leave leave) {
        String username=getCurrentUsername();
      User user = userRepository.findByUsernameCaseSensitive(username)
            .orElseThrow(() -> new ResourceNotFoundException("User not found: " + username));

        Employee employee = employeeRepository.findByEmail(user.getEmail())
            .orElseThrow(() -> new ResourceNotFoundException("Employee not found for email: " + user.getEmail()));

        LeaveType leaveType = leaveTypeRepository.findById(leave.getLeaveType().getLeaveTypeId())
                .orElseThrow(() -> new ResourceNotFoundException("LeaveType not found"));

        leave.setLeaveType(leaveType);
        leave.setEmployee(employee);
        leave.setStatus(LeaveStatus.PENDING);
        return leaveRepository.save(leave);
    }
    private String getCurrentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof UserDetails) {
                return ((UserDetails) principal).getUsername();
            }
        }
        throw new RuntimeException("Unauthorized or invalid user.");
    }
    @Override
    public List<Leave> getAllLeaves() {
        return leaveRepository.findAll();
    }

    @Override
    public Leave getLeaveById(Long id) {
        return leaveRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Leave not found with id: " + id));
    }

    @Override
    public Leave approveLeave(Long id, String comments) {
        String username=getCurrentUsername();
        User user = userRepository.findByUsernameCaseSensitive(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + username));
        Leave leave = getLeaveById(id);
        leave.setApproverId(user.getUserID());
        leave.setStatus(LeaveStatus.APPROVED);
        return leaveRepository.save(leave);
    }

    @Override
    public Leave rejectLeave(Long id, String comments) {
        Leave leave = getLeaveById(id);
        leave.setStatus(LeaveStatus.REJECTED);
        return leaveRepository.save(leave);
    }
}