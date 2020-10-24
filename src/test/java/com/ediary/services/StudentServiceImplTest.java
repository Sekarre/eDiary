package com.ediary.services;

import com.ediary.DTO.*;
import com.ediary.converters.*;
import com.ediary.domain.*;
import com.ediary.domain.Class;
import com.ediary.domain.security.User;
import com.ediary.domain.timetable.Timetable;
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
    TimetableService timetableService;

    @Mock
    EventService eventService;

    @Mock
    GradeRepository gradeRepository;

    @Mock
    AttendanceRepository attendanceRepository;

    @Mock
    BehaviorRepository behaviorRepository;

    @Mock
    StudentRepository studentRepository;

    @Mock
    GradeToGradeDto gradeToGradeDto;

    @Mock
    AttendanceToAttendanceDto attendanceToAttendanceDto;

    @Mock
    BehaviorToBehaviorDto behaviorToBehaviorDto;

    @Mock
    EventToEventDto eventToEventDto;

    @Mock
    StudentToStudentDto studentToStudentDto;

    StudentService studentService;

    Grade gradeReturned;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        studentService = new StudentServiceImpl(timetableService,eventService, gradeRepository, attendanceRepository,
                behaviorRepository, studentRepository, gradeToGradeDto, attendanceToAttendanceDto,
                behaviorToBehaviorDto, eventToEventDto, studentToStudentDto);

        gradeReturned = Grade.builder().id(gradeId).value(value).build();
    }

    @Test
    void listGrades() {
        when(gradeRepository.findAllByStudentId(studentId)).thenReturn(Arrays.asList(gradeReturned));

        when(gradeToGradeDto.convert(gradeReturned)).thenReturn(GradeDto.builder().id(gradeReturned.getId()).build());

        List<GradeDto> grades = studentService.listGrades(studentId);

        assertEquals(1, grades.size());
        assertEquals(gradeId, grades.get(0).getId());
        verify(gradeRepository, times(1)).findAllByStudentId(anyLong());
        verify(gradeToGradeDto, times(1)).convert(gradeReturned);
    }

    @Test
    void listGradesBySubject() {
        when(gradeRepository.findAllByStudentIdAndSubjectId(studentId, subjectId)).thenReturn(Arrays.asList(gradeReturned));

        when(gradeToGradeDto.convert(gradeReturned)).thenReturn(GradeDto.builder().id(gradeReturned.getId()).build());

        List<GradeDto> grades = studentService.listGrades(studentId, subjectId);

        assertEquals(1, grades.size());
        assertEquals(gradeId, grades.get(0).getId());
        verify(gradeRepository, times(1)).findAllByStudentIdAndSubjectId(anyLong(), anyLong());
        verify(gradeToGradeDto, times(1)).convert(gradeReturned);
    }

    @Test
    void listAttendances() {
        when(attendanceRepository.findAllByStudentId(studentId)).thenReturn(Arrays.asList(
                Attendance.builder().id(1L).build(),
                Attendance.builder().id(2L).build()
        ));

        when(attendanceToAttendanceDto.convert(any())).thenReturn(AttendanceDto.builder().id(5L).build());

        List<AttendanceDto> attendances = studentService.listAttendances(studentId);

        assertEquals(2, attendances.size());
        assertEquals(5L, attendances.get(1).getId());
        verify(attendanceRepository, times(1)).findAllByStudentId(anyLong());
        verify(attendanceToAttendanceDto, times(2)).convert(any());
    }

    @Test
    void listBehaviors() {
        when(behaviorRepository.findAllByStudentId(studentId)).thenReturn(Arrays.asList(
                Behavior.builder().id(1L).build(),
                Behavior.builder().id(2L).build()
        ));

        when(behaviorToBehaviorDto.convert(any())).thenReturn(BehaviorDto.builder().id(3L).build());

        List<BehaviorDto> behaviors = studentService.listBehaviors(studentId);

        assertEquals(2, behaviors.size());
        assertEquals(3L, behaviors.get(0).getId());
        verify(behaviorRepository, times(1)).findAllByStudentId(anyLong());
        verify(behaviorToBehaviorDto, times(2)).convert(any());
    }

    @Test
    void listEvents() {

        when(studentRepository.findById(studentId)).thenReturn(Optional.of(
                Student.builder().id(studentId).build()
                ));

        when(eventService.listEventsBySchoolClass(any())).thenReturn(Arrays.asList(
                Event.builder().id(1L).build(),
                Event.builder().id(2L).build()
        ));

        when(eventToEventDto.convert(any())).thenReturn(EventDto.builder().id(3L).build());

        List<EventDto> events = studentService.listEvents(studentId);

        assertEquals(2, events.size());
        assertEquals(3L, events.get(1).getId());
        verify(studentRepository, times(1)).findById(studentId);
        verify(eventService, times(1)).listEventsBySchoolClass(any());
        verify(eventToEventDto, times(2)).convert(any());

    }

    @Test
    void getTimetableByStudentId() {

        when(studentRepository.findById(studentId)).thenReturn(Optional.of(
                Student.builder().id(studentId).schoolClass(
                        Class.builder().id(classId).build())
                        .build()
        ));

        when(timetableService.getTimetableByClassId(classId)).thenReturn(Timetable.builder().build());

        when(timetableService.getTimetableByClassId(studentId)).thenReturn(Timetable.builder().build());

        Timetable timetable = studentService.getTimetableByStudentId(studentId);

        assertNotNull(timetable);
        verify(studentRepository, times(1)).findById(studentId);
        verify(timetableService, times(1)).getTimetableByClassId(classId);

    }

    @Test
    void findByUser() {
        Long userId = 24L;
        User user = User.builder().id(userId).build();

        Student studentReturned = Student.builder().id(studentId).build();

        when(studentRepository.findByUser(user)).thenReturn(Optional.of(studentReturned));
        when(studentToStudentDto.convert(studentReturned)).thenReturn(StudentDto.builder().id(studentReturned.getId()).build());

        StudentDto student = studentService.findByUser(user);

        assertEquals(student.getId(), studentReturned.getId());
        verify(studentRepository, times(1)).findByUser(user);
        verify(studentToStudentDto, times(1)).convert(studentReturned);
    }
}