package com.ediary.services;

import com.ediary.domain.*;
import com.ediary.domain.Class;
import com.ediary.repositories.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

import static org.junit.jupiter.api.Assertions.*;

class StudentServiceImplTest {

    private final Long studentId = 1L;

    private final Long subjectId = 2L;

    private final Long classId = 3L;

    private final Long gradeId = 3L;
    private final String value = "4";

    @Mock
    GradeRepository gradeRepository;

    @Mock
    AttendanceRepository attendanceRepository;

    @Mock
    BehaviorRepository behaviorRepository;

    @Mock
    EventRepository eventRepository;

    @Mock
    StudentRepository studentRepository;

    StudentService studentService;

    Grade gradeReturned;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        studentService = new StudentServiceImpl(gradeRepository, attendanceRepository, behaviorRepository,
                eventRepository, studentRepository);

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
    void listBehaviors() {
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

    @Test
    void listEvents() {

        when(studentRepository.findById(studentId)).thenReturn(Optional.of(
                Student.builder().id(studentId).schoolClass(
                            Class.builder().id(classId).build())
                        .build()
        ));

        when(eventRepository.findAllBySchoolClassId(classId)).thenReturn(Arrays.asList(
                Event.builder().id(1L).build(),
                Event.builder().id(2L).build()
        ));

        List<Event> events = studentService.listEvents(studentId);

        assertEquals(2, events.size());
        assertEquals(1L, events.get(0).getId());
        assertEquals(2L, events.get(1).getId());
        verify(studentRepository, times(1)).findById(studentId);
        verify(eventRepository, times(1)).findAllBySchoolClassId(classId);

    }
}