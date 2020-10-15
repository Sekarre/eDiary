package com.ediary.repositories.timetable;

import com.ediary.domain.timetable.Day;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DayRepository extends JpaRepository<Day, Long> {
}
