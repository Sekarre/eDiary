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
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;


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

        TreeMap<Day, List<SchoolPeriod>> schedule = new TreeMap<>(new Comparator<Day>() {
            @Override
            public int compare(Day o1, Day o2) {

                if (o1.getNumber() == o2.getNumber())
                    return 0;
                else if (o1.getNumber() > o2.getNumber())
                    return 1;

                return -1;
            }
        });

        days.forEach(day -> {
            List<SchoolPeriod> arrayList = Stream
                    .generate(SchoolPeriod::new)
                    .limit(durationsSize)
                    .collect(Collectors.toList());

            schedule.put(day, arrayList);
        });

        schoolPeriods.forEach(schoolPeriod -> {
            schedule.get(schoolPeriod.getDay()).add(schoolPeriod.getDuration().getNumber() - 1, schoolPeriod);
        });

        return Timetable.builder().durations(durations).schedule(schedule).build();
    }

}
