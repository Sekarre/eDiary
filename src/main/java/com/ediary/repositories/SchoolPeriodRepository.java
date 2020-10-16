package com.ediary.repositories;

import com.ediary.domain.SchoolPeriod;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SchoolPeriodRepository extends JpaRepository<SchoolPeriod, Long> {

    List<SchoolPeriod> findAllBySchoolClassId(Long classId);
}
