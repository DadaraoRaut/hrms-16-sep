package com.quantumsoft.hrms.Human_Resource_Website.service;

import com.quantumsoft.hrms.Human_Resource_Website.entity.OptionalHoliday;

import java.util.List;

public interface OptionalHolidayService {

    OptionalHoliday createOptionalHoliday(OptionalHoliday optionalHoliday);

    List<OptionalHoliday> getAllOptionalHolidays();

    OptionalHoliday getOptionalHolidayById(Long id);

    void deleteOptionalHoliday(Long id);

    OptionalHoliday updateOptionalHoliday(Long id, OptionalHoliday optionalHoliday);
}
