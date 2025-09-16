package com.quantumsoft.hrms.Human_Resource_Website.serviceImpl;

import com.quantumsoft.hrms.Human_Resource_Website.entity.OptionalHoliday;
import com.quantumsoft.hrms.Human_Resource_Website.exception.ResourceNotFoundException;
import com.quantumsoft.hrms.Human_Resource_Website.repository.OptionalHolidayRepository;
import com.quantumsoft.hrms.Human_Resource_Website.service.OptionalHolidayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OptionalHolidayServiceImpl implements OptionalHolidayService {

    @Autowired
    private OptionalHolidayRepository optionalHolidayRepository;

    public OptionalHoliday createOptionalHoliday(OptionalHoliday optionalHoliday) {
        return optionalHolidayRepository.save(optionalHoliday);
    }

    public List<OptionalHoliday> getAllOptionalHolidays() {
        return optionalHolidayRepository.findAll();
    }

    public OptionalHoliday getOptionalHolidayById(Long id) {
        return optionalHolidayRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("OptionalHoliday not found"));
    }

    public OptionalHoliday updateOptionalHoliday(Long id, OptionalHoliday optionalHoliday) {
        OptionalHoliday existingHoliday = getOptionalHolidayById(id);
        existingHoliday.setName(optionalHoliday.getName());
        existingHoliday.setDate(optionalHoliday.getDate());
        existingHoliday.setDescription(optionalHoliday.getDescription());
        existingHoliday.setRegionOrReligion(optionalHoliday.getRegionOrReligion());
        return optionalHolidayRepository.save(existingHoliday);
    }

    public void deleteOptionalHoliday(Long id) {
        optionalHolidayRepository.deleteById(id);
    }
}
