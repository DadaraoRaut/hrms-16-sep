package com.quantumsoft.hrms.Human_Resource_Website.serviceImpl;

import com.quantumsoft.hrms.Human_Resource_Website.entity.LeaveType;
import com.quantumsoft.hrms.Human_Resource_Website.exception.ResourceNotFoundException;
import com.quantumsoft.hrms.Human_Resource_Website.repository.LeaveTypeRepository;
import com.quantumsoft.hrms.Human_Resource_Website.service.LeaveTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LeaveTypeServiceImpl implements LeaveTypeService {

    @Autowired
    private LeaveTypeRepository leaveTypeRepository;


    @Override
    public LeaveType createLeaveType(LeaveType leaveType) {
        return leaveTypeRepository.save(leaveType);
    }

    @Override
    public LeaveType getLeaveTypeById(Long leaveTypeId) {
        return leaveTypeRepository.findById(leaveTypeId)
                .orElseThrow(() -> new ResourceNotFoundException("LeaveType not found with id: " + leaveTypeId));
    }

    @Override
    public LeaveType updateLeaveType(Long leaveTypeId, LeaveType leaveType) {
        LeaveType existingLeaveType = leaveTypeRepository.findById(leaveTypeId)
                .orElseThrow(() -> new ResourceNotFoundException("LeaveType not found with id: " + leaveTypeId));
        existingLeaveType.setName(leaveType.getName());
        existingLeaveType.setCarryForward(leaveType.isCarryForward());
        existingLeaveType.setEncashable(leaveType.isEncashable());
        existingLeaveType.setMaxDaysPerYear(leaveType.getMaxDaysPerYear());
        existingLeaveType.setApprovalFlow(leaveType.getApprovalFlow());
        existingLeaveType.setDescription(leaveType.getDescription());
        return leaveTypeRepository.save(existingLeaveType);
    }

    @Override
    public void deleteLeaveType(Long leaveTypeId) {
        leaveTypeRepository.deleteById(leaveTypeId);
    }

    @Override
    public List<LeaveType> getAllLeaveTypes() {
        return leaveTypeRepository.findAll();
    }
}
