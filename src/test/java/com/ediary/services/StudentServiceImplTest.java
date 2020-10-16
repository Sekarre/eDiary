package com.ediary.services;

import com.ediary.domain.Attendance;
import com.ediary.domain.Behavior;
import com.ediary.domain.Grade;
import com.ediary.repositories.AttendanceRepository;
import com.ediary.repositories.BehaviorRepository;
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

    private final Long subjectId = 2L;

    private final Long gradeId = 3L;
    private final String value = "4";

    @Mock
    GradeRepository gradeRepository;

    @Mock
    AttendanceRepository attendanceRepository;

    @Mock
    BehaviorRepository behaviorRepository;

    StudentService studentService;

    Grade gradeReturned;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        studentService = new StudentServiceImpl(gradeRepository, attendanceRepository, behaviorRepository);

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
    void listGradesBySubject() {
        when(gradeRepository.findAllByStudentIdAndSubjectId(studentId, subjectId)).thenReturn(Arrays.asList(gradeReturned));

        List<Grade> grades = studentService.listGrades(studentId, subjectId);

        assertEquals(1, grades.size());
        assertEquals(gradeId, grades.get(0).getId());
        verify(gradeRepository, times(1)).findAllByStudentIdAndSubjectId(anyLong(), anyLong());
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

    @Test
    void listBehavior() {
        when(behaviorRepository.findAllByStudentId(studentId)).thenReturn(Arrays.asList(
                Behavior.builder().id(1L).build(),
                Behavior.builder().id(2L).build()
        ));

        List<Behavior> behaviors = studentService.listBehaviors(studentId);

        assertEquals(2, behaviors.size());
        assertEquals(1L, behaviors.get(0).getId());
        assertEquals(2L, behaviors.get(1).getId());
        verify(behaviorRepository, times(1)).findAllByStudentId(anyLong());
    }
}