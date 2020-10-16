package com.ediary.services;

import com.ediary.domain.timetable.Timetable;
import com.ediary.repositories.SchoolPeriodRepository;
import com.ediary.repositories.timetable.DayRepository;
import com.ediary.repositories.timetable.DurationRepository;
import org.springframework.stereotype.Service;



@Service
public class TimetableServiceImpl implements TimetableService {

    private final SchoolPeriodRepository schoolPeriodRepository;
    private final DayRepository dayRepository;
    private final DurationRepository durationRepository;

    public TimetableServiceImpl(SchoolPeriodRepository schoolPeriodRepository, DayRepository dayRepository, DurationRepository durationRepository) {
        this.schoolPeriodRepository = schoolPeriodRepository;
        this.dayRepository = dayRepository;
        this.durationRepository = durationRepository;
    }

    @Override
    public Timetable getTimetableByClassId(Long classId) {

        //TODO
        return new Timetable();
    }

}
