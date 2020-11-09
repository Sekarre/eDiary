package com.ediary.web.controllers;

import com.ediary.DTO.*;
import com.ediary.converters.UserToUserDto;
import com.ediary.domain.Attendance;
import com.ediary.domain.Behavior;
import com.ediary.domain.Event;
import com.ediary.domain.Grade;
import com.ediary.domain.timetable.Timetable;
import com.ediary.services.StudentService;
import com.ediary.services.SubjectService;
import com.ediary.services.WeeklyAttendancesService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class StudentControllerTest {

    private final Long studentId = 1L;

    private final Long subjectId = 2L;
    private final String subjectName = "Maths";

    @Mock
    StudentService studentService;
    @Mock
    SubjectService subjectService;
    @Mock
    WeeklyAttendancesService weeklyAttendancesService;

    @Mock
    UserToUserDto userToUserDto;

    StudentController studentController;

    MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        studentController = new StudentController(studentService, subjectService, weeklyAttendancesService, userToUserDto);
        mockMvc = MockMvcBuilders.standaloneSetup(studentController).build();
    }

    @Test
    void getAllGrades() throws Exception {
        when(studentService.listGrades(studentId)).thenReturn(Arrays.asList(
                GradeDto.builder().id(1L).value("1").build(),
                GradeDto.builder().id(2L).value("2").build()
        ));

        when(studentService.listSubjects(studentId)).thenReturn(Arrays.asList(
                SubjectDto.builder().id(1L).build(),
                SubjectDto.builder().id(2L).build(),
                SubjectDto.builder().id(3L).build()
        ));

        mockMvc.perform(get("/student/" + studentId + "/grade"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("subjects"))
                .andExpect(model().attributeExists("grades"))
                .andExpect(view().name("student/allGrades"));

        verify(studentService).listGrades(studentId);
        verify(studentService).listSubjects(studentId);
        assertEquals(2, studentService.listGrades(studentId).size());
        assertEquals(3, studentService.listSubjects(studentId).size());
    }

    @Test
    void getAllGradesBySubject() throws Exception {

        when(studentService.listGrades(studentId, subjectId)).thenReturn(Arrays.asList(
                GradeDto.builder().id(1L).value("1").build(),
                GradeDto.builder().id(2L).value("2").build()
        ));

        when(subjectService.getNameById(subjectId)).thenReturn(subjectName);

        mockMvc.perform(get("/student/" + studentId + "/grade/subject/" + subjectId))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("subjectName"))
                .andExpect(model().attributeExists("grades"))
                .andExpect(view().name("student/allGradesBySubject"));

        verify(studentService, times(1)).listGrades(studentId, subjectId);
        assertEquals(2, studentService.listGrades(studentId, subjectId).size());

        verify(subjectService, times(1)).getNameById(subjectId);
        assertEquals(subjectName, subjectService.getNameById(subjectId));

    }

    @Test
    void getAllAttendances() throws Exception {

        when(studentService.listAttendances(studentId)).thenReturn(Arrays.asList(AttendanceDto.builder().id(1L).build()));

        mockMvc.perform(get("/student/" + studentId + "/attendance"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("attendances"))
                .andExpect(view().name("student/allAttendances"));

        verify(studentService).listAttendances(studentId);
        assertEquals(1, studentService.listAttendances(studentId).size());
    }

    @Test
    void getAllBehaviors() throws Exception {

        when(studentService.listBehaviors(studentId)).thenReturn(Arrays.asList(
                BehaviorDto.builder().id(1L).build(),
                BehaviorDto.builder().id(2L).build()
        ));

        mockMvc.perform(get("/student/" + studentId + "/behavior"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("behaviors"))
                .andExpect(view().name("student/allBehaviors"));

        verify(studentService, times(1)).listBehaviors(studentId);
        assertEquals(2, studentService.listBehaviors(studentId).size());
    }

    @Test
    void getAllEvents() throws Exception {

        when(studentService.listEvents(studentId, 1, 2)).thenReturn(Arrays.asList(
                EventDto.builder().id(1L).build(),
                EventDto.builder().id(2L).build()
        ));

        mockMvc.perform(get("/student/" + studentId + "/event"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("events"))
                .andExpect(view().name("student/allEvents"));

        assertEquals(2, studentService.listEvents(studentId, 1, 2).size());
    }

    @Test
    void getTimetable() throws Exception {

        when(studentService.getTimetableByStudentId(studentId)).thenReturn(
                Timetable.builder().build()
        );

        mockMvc.perform(get("/student/" + studentId + "/timetable"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("timetable"))
                .andExpect(view().name("student/timetable"));

        verify(studentService, times(1)).getTimetableByStudentId(studentId);
    }

    @Test
    void authenticatedUserAndStudent() throws Exception {

        when(userToUserDto.convert(any())).thenReturn(UserDto.builder().build());
        when(studentService.findByUser(any())).thenReturn(StudentDto.builder().build());

        mockMvc.perform(get("/student/" + studentId + "/grade"))
                .andExpect(model().attributeExists("user"))
                .andExpect(model().attributeExists("student"));

        verify(userToUserDto, times(1)).convert(any());
        verify(studentService, times(1)).findByUser(any());
    }


}