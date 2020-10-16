package com.ediary.web.controllers;

import com.ediary.domain.Grade;
import com.ediary.services.StudentService;
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

    private static Long id = 1L;

    @Mock
    StudentService studentService;

    StudentController studentController;

    MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        studentController = new StudentController(studentService);
        mockMvc = MockMvcBuilders.standaloneSetup(studentController).build();
    }

    @Test
    void getAllGrades() throws Exception {
        when(studentService.listGrades(id)).thenReturn(Arrays.asList(
                Grade.builder().id(1L).value("1").build(),
                Grade.builder().id(2L).value("2").build()
        ));

        mockMvc.perform(get("/students/" + id))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("grades"))
                .andExpect(view().name("student/allGrades"));

        verify(studentService).listGrades(id);
        assertEquals(2, studentService.listGrades(id).size());
    }
}