package com.ediary.services;

import com.ediary.domain.SchoolPeriod;
import com.ediary.domain.timetable.Day;
import com.ediary.domain.timetable.Duration;
import com.ediary.domain.timetable.Timetable;
import com.ediary.repositories.SchoolPeriodRepository;
import com.ediary.repositories.timetable.DayRepository;
import com.ediary.repositories.timetable.DurationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;


@RequiredArgsConstructor
@Service
public class TimetableServiceImpl implements TimetableService {

    private final SchoolPeriodRepository schoolPeriodRepository;
    private final DayRepository dayRepository;
    private final DurationRepository durationRepository;


    @Override
    public Timetable getTimetableByClassId(Long classId) {

        List<Duration> durations = durationRepository.findAllByOrderByNumberAsc();
        int durationsSize = durations.size();

        List<Day> days = dayRepository.findAllByOrderByNumberAsc();

        List<SchoolPeriod> schoolPeriods = schoolPeriodRepository.findAllBySchoolClassId(classId);

        HashMap<Day, ArrayList<SchoolPeriod>> schedule = new HashMap<>();

        days.forEach(day -> {
            ArrayList<SchoolPeriod> arrayList = new ArrayList<>(durationsSize);
            schedule.put(day, arrayList);
        });

        schoolPeriods.forEach(schoolPeriod -> {
            schedule.get(schoolPeriod.getDay()).add(schoolPeriod.getDuration().getNumber(), schoolPeriod);
        });

        return Timetable.builder().durations(durations).schedule(schedule).build();
    }

}
