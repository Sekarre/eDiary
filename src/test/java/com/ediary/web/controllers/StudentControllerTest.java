package com.ediary.web.controllers;

import com.ediary.domain.Grade;
import com.ediary.services.StudentService;
import com.ediary.services.SubjectService;
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

    StudentController studentController;

    MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        studentController = new StudentController(studentService, subjectService);
        mockMvc = MockMvcBuilders.standaloneSetup(studentController).build();
    }

    @Test
    void getAllGrades() throws Exception {
        when(studentService.listGrades(studentId)).thenReturn(Arrays.asList(
                Grade.builder().id(1L).value("1").build(),
                Grade.builder().id(2L).value("2").build()
        ));

        mockMvc.perform(get("/students/" + studentId))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("grades"))
                .andExpect(view().name("student/allGrades"));

        verify(studentService).listGrades(studentId);
        assertEquals(2, studentService.listGrades(studentId).size());
    }

    @Test
    void getAllGradesBySubject() throws Exception {

        when(studentService.listGrades(studentId, subjectId)).thenReturn(Arrays.asList(
                Grade.builder().id(1L).value("1").build(),
                Grade.builder().id(2L).value("2").build()
        ));

        when(subjectService.getNameById(subjectId)).thenReturn(subjectName);

        mockMvc.perform(get("/students/" + studentId + "/" + subjectId))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("subjectName"))
                .andExpect(model().attributeExists("grades"))
                .andExpect(view().name("student/allGradesBySubject"));

        verify(studentService, times(1)).listGrades(studentId, subjectId);
        assertEquals(2, studentService.listGrades(studentId, subjectId).size());

        verify(subjectService, times(1)).getNameById(subjectId);
        assertEquals(subjectName, subjectService.getNameById(subjectId));

    }
}