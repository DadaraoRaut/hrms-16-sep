package com.quantumsoft.hrms.Human_Resource_Website.service;

import com.quantumsoft.hrms.Human_Resource_Website.entity.LeaveType;

import java.util.List;

public interface LeaveTypeService {

    LeaveType createLeaveType(LeaveType leaveType);
    void deleteLeaveType(Long id);
    List<LeaveType> getAllLeaveTypes();
    LeaveType getLeaveTypeById(Long leaveTypeId);
    LeaveType updateLeaveType(Long id, LeaveType leaveType);
}
