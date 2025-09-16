package com.quantumsoft.hrms.Human_Resource_Website.service;

import com.quantumsoft.hrms.Human_Resource_Website.entity.PerformanceReview;

import java.util.List;

public interface PerformanceReviewService {
    PerformanceReview createReview(Long employeeId, PerformanceReview review);

    PerformanceReview updateReview(long reviewId, PerformanceReview review);

    PerformanceReview finalizeReview(long reviewId);

    List<PerformanceReview> getEmployeeReviews(Long employeeId);

    List<PerformanceReview> getCycleReviews(String cycle);
}
