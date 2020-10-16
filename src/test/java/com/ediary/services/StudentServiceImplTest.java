package com.ediary.services;

import com.ediary.domain.Attendance;
import com.ediary.domain.Grade;
import com.ediary.repositories.AttendanceRepository;
import com.ediary.repositories.GradeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;

import static org.junit.jupiter.api.Assertions.*;

class StudentServiceImplTest {

    private final Long studentId = 1L;
    private final Long gradeId = 2L;
    private final String value = "3";

    @Mock
    GradeRepository gradeRepository;

    @Mock
    AttendanceRepository attendanceRepository;

    StudentService studentService;

    Grade gradeReturned;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        studentService = new StudentServiceImpl(gradeRepository, attendanceRepository);

        gradeReturned = Grade.builder().id(gradeId).value(value).build();
    }

    @Test
    void listGrades() {
        when(gradeRepository.findAllByStudentId(studentId)).thenReturn(Arrays.asList(gradeReturned));

        List<Grade> grades = studentService.listGrades(studentId);

        assertEquals(1, grades.size());
        assertEquals(gradeId, grades.get(0).getId());
        verify(gradeRepository, times(1)).findAllByStudentId(anyLong());
    }

    @Test
    void listAttendances() {
        when(attendanceRepository.findAllByStudentId(studentId)).thenReturn(Arrays.asList(
                Attendance.builder().id(1L).build(),
                Attendance.builder().id(2L).build()
        ));

        List<Attendance> attendances = studentService.listAttendances(studentId);

        assertEquals(2, attendances.size());
        assertEquals(1L, attendances.get(0).getId());
        assertEquals(2L, attendances.get(1).getId());
        verify(attendanceRepository, times(1)).findAllByStudentId(anyLong());
    }
}