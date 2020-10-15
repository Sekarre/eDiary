package com.ediary.repositories.timetable;

import com.ediary.domain.timetable.Duration;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DurationRepository extends JpaRepository<Duration, Long> {
}
