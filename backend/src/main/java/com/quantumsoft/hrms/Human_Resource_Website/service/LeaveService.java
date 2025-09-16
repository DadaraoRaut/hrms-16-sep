package com.quantumsoft.hrms.Human_Resource_Website.service;

import com.quantumsoft.hrms.Human_Resource_Website.entity.Leave;

import java.util.List;

public interface LeaveService {
    Leave applyLeave(Leave leave);
    List<Leave> getAllLeaves();
    Leave getLeaveById(Long leaveTypeId);
    Leave approveLeave(Long leaveTypeId, String comments);
    Leave rejectLeave(Long leaveTypeId, String comments);
}
