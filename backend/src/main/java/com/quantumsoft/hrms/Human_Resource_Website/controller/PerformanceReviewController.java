package com.quantumsoft.hrms.Human_Resource_Website.controller;

import com.quantumsoft.hrms.Human_Resource_Website.entity.PerformanceReview;
import com.quantumsoft.hrms.Human_Resource_Website.service.PerformanceReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/performance-reviews")
@RequiredArgsConstructor
public class PerformanceReviewController {

    @Autowired
    private PerformanceReviewService performanceReviewService;

    @PostMapping("/create/{employeeId}")
    public ResponseEntity<PerformanceReview> createReview(
            @PathVariable Long employeeId,
            @RequestBody PerformanceReview review) {
        PerformanceReview createdReview = performanceReviewService.createReview(employeeId, review);
        return ResponseEntity.ok(createdReview);
    }

    @PutMapping("/update/{reviewId}")
    public ResponseEntity<PerformanceReview> updateReview(
            @PathVariable long reviewId,
            @RequestBody PerformanceReview review) {
        PerformanceReview updatedReview = performanceReviewService.updateReview(reviewId, review);
        return ResponseEntity.ok(updatedReview);
    }

    @PutMapping("/finalize/{reviewId}")
    public ResponseEntity<PerformanceReview> finalizeReview(@PathVariable long reviewId) {
        PerformanceReview finalizedReview = performanceReviewService.finalizeReview(reviewId);
        return ResponseEntity.ok(finalizedReview);
    }

    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<List<PerformanceReview>> getEmployeeReviews(@PathVariable Long employeeId) {
        List<PerformanceReview> reviews = performanceReviewService.getEmployeeReviews(employeeId);
        return ResponseEntity.ok(reviews);
    }


    @GetMapping("/cycle/{cycle}")
    public ResponseEntity<List<PerformanceReview>> getCycleReviews(@PathVariable String cycle) {
        List<PerformanceReview> reviews = performanceReviewService.getCycleReviews(cycle);
        return ResponseEntity.ok(reviews);
    }

}

