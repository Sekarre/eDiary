package com.ediary.web.controllers;

import com.ediary.DTO.*;
import com.ediary.converters.UserToUserDto;
import com.ediary.domain.*;
import com.ediary.domain.timetable.Timetable;
import com.ediary.services.ParentService;
import com.ediary.services.StudentService;
import com.ediary.services.SubjectService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Slf4j
class ParentControllerTest {

    private final Long parentId = 1L;
    private final Long studentId = 1L;
    private final Long subjectId = 2L;
    private final String subjectName = "Maths";

    @Mock
    ParentService parentService;

    @Mock
    StudentService studentService;

    @Mock
    SubjectService subjectService;

    @Mock
    UserToUserDto userToUserDto;

    MockMvc mockMvc;

    ParentController parentController;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        parentController = new ParentController(parentService, studentService, subjectService, userToUserDto);
        mockMvc = MockMvcBuilders.standaloneSetup(parentController).build();
    }

    @Test
    void authenticatedUserAndParent() throws Exception {

        when(userToUserDto.convert(any())).thenReturn(UserDto.builder().build());
        when(parentService.findByUser(any())).thenReturn(ParentDto.builder().build());

        mockMvc.perform(get("/parent/" + parentId+ "/students"))
                .andExpect(model().attributeExists("user"))
                .andExpect(model().attributeExists("parent"));

        verify(userToUserDto, times(1)).convert(any());
        verify(parentService, times(1)).findByUser(any());
    }

    @Test
    void getAllStudents() throws Exception {

        when(parentService.listStudents(parentId)).thenReturn(
          Collections.singletonList(StudentDto.builder().id(parentId).build())
        );

        mockMvc.perform(get("/parent/" + parentId + "/students"))
                .andExpect(status().isOk())
                .andExpect(view().name("parent/allStudents"))
                .andExpect(model().attributeExists("students"));
    }

    //todo:
    @Disabled
    void getAllGrades() throws Exception {
        when(studentService.listGrades(studentId)).thenReturn(Arrays.asList(
                GradeDto.builder().id(1L).value("1").build(),
                GradeDto.builder().id(2L).value("2").build()
        ));

        mockMvc.perform(get("/parent/" + parentId + "/" + studentId + "/grade"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("grades"))
                .andExpect(view().name("parent/allGrades"));

        assertEquals(2, studentService.listGrades(studentId).size());
    }

    @Test
    void getAllGradesBySubject() throws Exception {

        when(studentService.listGrades(studentId, subjectId)).thenReturn(Arrays.asList(
                GradeDto.builder().id(1L).value("1").build(),
                GradeDto.builder().id(2L).value("2").build()
        ));

        when(subjectService.getNameById(subjectId)).thenReturn(subjectName);

        mockMvc.perform(get("/parent/" + parentId + "/" + studentId + "/grade/subject/" + subjectId))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("subjectName"))
                .andExpect(model().attributeExists("grades"))
                .andExpect(view().name("parent/allGradesBySubject"));

        verify(studentService, times(1)).listGrades(studentId, subjectId);
        assertEquals(2, studentService.listGrades(studentId, subjectId).size());

        verify(subjectService, times(1)).getNameById(subjectId);
        assertEquals(subjectName, subjectService.getNameById(subjectId));

    }

    @Test
    void getAllAttendances() throws Exception {

        when(studentService.listAttendances(studentId)).thenReturn(Arrays.asList(AttendanceDto.builder().id(1L).build()));

        mockMvc.perform(get("/parent/" + parentId + "/" +  studentId + "/attendance"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("attendances"))
                .andExpect(view().name("parent/allAttendances"));

        verify(studentService).listAttendances(studentId);
        assertEquals(1, studentService.listAttendances(studentId).size());
    }

    @Test
    void getAllBehaviors() throws Exception {

        when(studentService.listBehaviors(studentId)).thenReturn(Arrays.asList(
                BehaviorDto.builder().id(1L).build(),
                BehaviorDto.builder().id(2L).build()
        ));

        mockMvc.perform(get("/parent/" + parentId + "/" + studentId + "/behavior"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("behaviors"))
                .andExpect(view().name("parent/allBehaviors"));

        verify(studentService, times(1)).listBehaviors(studentId);
        assertEquals(2, studentService.listBehaviors(studentId).size());
    }

    @Test
    void getAllEvents() throws Exception {

        when(studentService.listEvents(studentId)).thenReturn(Arrays.asList(
                EventDto.builder().id(1L).build(),
                EventDto.builder().id(2L).build()
        ));

        mockMvc.perform(get("/parent/" + parentId + "/" + studentId + "/event"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("events"))
                .andExpect(view().name("parent/allEvents"));

        verify(studentService, times(1)).listEvents(studentId);
        assertEquals(2, studentService.listEvents(studentId).size());
    }

    @Test
    void getTimetable() throws Exception {

        when(studentService.getTimetableByStudentId(studentId)).thenReturn(
                Timetable.builder().build()
        );

        mockMvc.perform(get("/parent/" + parentId + "/" + studentId + "/timetable"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("timetable"))
                .andExpect(view().name("parent/timetable"));

        verify(studentService, times(1)).getTimetableByStudentId(studentId);
    }


}