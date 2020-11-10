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

        Set<Duration> durs = new HashSet<>(durationRepository.findAllByOrderByNumberAsc());

        List<SchoolPeriod> schoolPeriods = schoolPeriodRepository.findAllBySchoolClassId(classId);

        TreeMap<Duration, List<SchoolPeriod>> schedule = new TreeMap<>(new Comparator<Duration>() {
            @Override
            public int compare(Duration o1, Duration o2) {

                if (o1.getNumber() == o2.getNumber())
                    return 0;
                else if (o1.getNumber() > o2.getNumber())
                    return 1;

                return -1;
            }
        });

        durs.forEach(duration -> {
            List<SchoolPeriod> arrayList = Stream
                    .generate(SchoolPeriod::new)
                    .limit(durationsSize)
                    .collect(Collectors.toList());

            schedule.put(duration, arrayList);
        });

        schoolPeriods.forEach(schoolPeriod -> {
            schedule.get(schoolPeriod.getDuration()).add(schoolPeriod.getDay().getNumber(), schoolPeriod);
        });

        return Timetable.builder().durations(durations).schedule(schedule).build();
    }

}
