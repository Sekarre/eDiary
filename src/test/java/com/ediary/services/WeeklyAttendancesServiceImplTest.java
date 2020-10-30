package com.ediary.services;

import com.ediary.DTO.AttendanceDto;
import com.ediary.converters.AttendanceToAttendanceDto;
import com.ediary.domain.Attendance;
import com.ediary.domain.Event;
import com.ediary.domain.helpers.WeeklyAttendances;
import com.ediary.repositories.AttendanceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;


public class WeeklyAttendancesServiceImplTest {

    @Mock
    private AttendanceRepository attendanceRepository;
    @Mock
    private AttendanceToAttendanceDto attendanceToAttendanceDto;

    WeeklyAttendancesService weeklyAttendancesService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        weeklyAttendancesService = new WeeklyAttendancesServiceImpl(attendanceRepository, attendanceToAttendanceDto);
    }

    @Test
    void getAttendanceByWeek() {
        when(attendanceRepository.findAllByStudentIdAndLesson_Date(any(),any())).thenReturn(Arrays.asList(
                Attendance.builder().id(1L).build(),
                Attendance.builder().id(2L).build()
        ));

        when(attendanceToAttendanceDto.convert(any())).thenReturn(AttendanceDto.builder().id(1L).build());


        WeeklyAttendances weeklyAttendances = weeklyAttendancesService.getAttendancesByWeek(1L, 7, Date.valueOf(LocalDate.now()));

        assertNotNull(weeklyAttendances);
        assertEquals(7, weeklyAttendances.getAttendances().keySet().size());
    }
}
