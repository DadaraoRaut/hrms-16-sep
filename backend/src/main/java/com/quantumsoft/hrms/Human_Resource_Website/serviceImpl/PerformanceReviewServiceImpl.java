package com.quantumsoft.hrms.Human_Resource_Website.serviceImpl;

import com.quantumsoft.hrms.Human_Resource_Website.entity.Employee;
import com.quantumsoft.hrms.Human_Resource_Website.entity.PerformanceReview;
import com.quantumsoft.hrms.Human_Resource_Website.exception.ResourceNotFoundException;
import com.quantumsoft.hrms.Human_Resource_Website.repository.PerformanceReviewRepository;
import com.quantumsoft.hrms.Human_Resource_Website.service.PerformanceReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PerformanceReviewServiceImpl implements PerformanceReviewService {

    @Autowired
            private PerformanceReviewRepository performanceReviewRepository;

    @Autowired
        private EmployeeServiceImpl employeeService;

    public PerformanceReview createReview(Long employeeId, PerformanceReview review) {
        Employee employee = employeeService.getEmployeeById(employeeId);
        review.setEmployee(employee);
        return performanceReviewRepository.save(review);
    }

    public PerformanceReview updateReview(long id, PerformanceReview review) {
        PerformanceReview existingReview = performanceReviewRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Performance review not found"));

        existingReview.setSelfAssessment(review.getSelfAssessment());
        existingReview.setManagerReview(review.getManagerReview());
        existingReview.setPeerReview(review.getPeerReview());
        existingReview.setOverallScore(review.getOverallScore());
        existingReview.setGoals(review.getGoals());

        return performanceReviewRepository.save(existingReview);
    }


    public PerformanceReview finalizeReview(long id) {
        PerformanceReview review = performanceReviewRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Performance review not found"));

        // Add any finalization logic here
        return performanceReviewRepository.save(review);
    }


    public PerformanceReview getReview(long id) {
        return performanceReviewRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Performance review not found"));
    }


    public List<PerformanceReview> getEmployeeReviews(Long employeeId) {
        Employee employee = employeeService.getEmployeeById(employeeId);
        return performanceReviewRepository.findByEmployee(employee);
    }


    public List<PerformanceReview> getCycleReviews(String cycle) {
        return performanceReviewRepository.findByReviewCycle(cycle);
    }


}

