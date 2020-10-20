package com.ediary.web.controllers;

import com.ediary.DTO.ClassDto;
import com.ediary.DTO.EventDto;
import com.ediary.domain.Event;
import com.ediary.domain.Notice;
import com.ediary.services.TeacherService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class TeacherControllerTest {

    private final Long teacherId = 1L;

    @Mock
    TeacherService teacherService;

    TeacherController teacherController;

    MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        teacherController = new TeacherController(teacherService);
        mockMvc = MockMvcBuilders.standaloneSetup(teacherController).build();
    }

    @Test
    void getAllEvents() throws Exception {

        when(teacherService.listEvents(teacherId)).thenReturn(Arrays.asList(
                EventDto.builder().id(1L).build(),
                EventDto.builder().id(2L).build()
        ));

        mockMvc.perform(get("/teacher/" + teacherId + "/event"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("events"))
                .andExpect(view().name("teacher/allEvents"));

        verify(teacherService, times(1)).listEvents(teacherId);
        assertEquals(2, teacherService.listEvents(teacherId).size());
    }


    @Test
    void getAllClasses() throws Exception {

        when(teacherService.listAllClasses()).thenReturn(Arrays.asList(
                ClassDto.builder().id(1L).build(),
                ClassDto.builder().id(2L).build()
        ));

        mockMvc.perform(get("/teacher/" + teacherId + "/classes"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("classes"))
                .andExpect(view().name("teacher/allClasses"));


        verify(teacherService, times(1)).listAllClasses();
        assertEquals(2, teacherService.listAllClasses().size());
    }

}