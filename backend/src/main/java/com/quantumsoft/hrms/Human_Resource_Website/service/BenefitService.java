package com.quantumsoft.hrms.Human_Resource_Website.service;

import com.quantumsoft.hrms.Human_Resource_Website.entity.Benefit;

import java.util.List;

public interface BenefitService {

    Benefit createBenefit(Benefit benefit);

    Benefit updateBenefit(Long id, Benefit benefit);

    List<Benefit> getAllBenefits();

    void deleteBenefit(Long id);
}
