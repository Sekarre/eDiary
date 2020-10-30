package com.ediary.services;

import com.ediary.DTO.AttendanceDto;
import com.ediary.converters.AttendanceToAttendanceDto;
import com.ediary.domain.Attendance;
import com.ediary.domain.helpers.WeeklyAttendances;
import com.ediary.repositories.AttendanceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class WeeklyAttendancesServiceImpl implements WeeklyAttendancesService {

    private final AttendanceRepository attendanceRepository;
    private final AttendanceToAttendanceDto attendanceToAttendanceDto;

    //todo tests
    @Override
    public WeeklyAttendances getAttendancesByWeek(Long studentId, Integer daysNumber, Date date) {

        LocalDate localDate = date.toLocalDate();


        Map<Date, List<AttendanceDto>> attendancesByDays = new TreeMap<>((o1, o2) -> {
            if (o1.getDate() == o2.getDate())
                return 0;
            else if (o1.getDate() > o2.getDate())
                return 1;

            return -1;
        });

        for (int i = 0; i < 7; i++) {
            List<Attendance> attendanceList = attendanceRepository.findAllByStudentIdAndLesson_Date(studentId, Date.valueOf(localDate));
            attendancesByDays.put(Date.valueOf(localDate), attendanceList.stream()
                    .map(attendanceToAttendanceDto::convert)
                    .collect(Collectors.toList()));
            localDate = localDate.plusDays(1);
        }

        return WeeklyAttendances.builder().attendances(attendancesByDays).build();
    }
}
