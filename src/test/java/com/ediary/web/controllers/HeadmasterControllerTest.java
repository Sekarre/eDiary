package com.ediary.web.controllers;

import com.ediary.DTO.StudentDto;
import com.ediary.DTO.TeacherDto;
import com.ediary.converters.UserToUserDto;
import com.ediary.domain.helpers.TimeInterval;
import com.ediary.services.HeadmasterService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;


import java.time.LocalDate;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class HeadmasterControllerTest {

    @Mock
    UserToUserDto userToUserDto;

    @Mock
    HeadmasterService headmasterService;

    HeadmasterController headmasterController;

    MockMvc mockMvc;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        headmasterController = new HeadmasterController(headmasterService, userToUserDto);
        mockMvc = MockMvcBuilders.standaloneSetup(headmasterController).build();
    }

    @Test
    void getTeacherReport() throws Exception {
        Integer page = 1;

        when(headmasterService.listAllTeachers(any(),any())).thenReturn(Arrays.asList(
                TeacherDto.builder().id(1L).build(),
                TeacherDto.builder().id(2L).build()
        ));

        when(headmasterService.initNewTimeInterval()).thenReturn(TimeInterval.builder().build());

        mockMvc.perform(get("/headmaster/teacherReport/" + page))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("teachers"))
                .andExpect(model().attributeExists("timeInterval"))
                .andExpect(model().attributeExists("currentPage"))
                .andExpect(view().name("headmaster/teacherReport"));

        assertEquals(2, headmasterService.listAllTeachers(page, 20).size());

    }

    @Test
    void processNewTimeInterval() throws Exception {
        Integer page = 1;

        when(headmasterService.listAllTeachers(any(),any())).thenReturn(Arrays.asList(
                TeacherDto.builder().id(1L).build(),
                TeacherDto.builder().id(2L).build()
        ));

        when(headmasterService.setTimeInterval(any(), any())).thenReturn(TimeInterval.builder().build());

        mockMvc.perform(post("/headmaster/teacherReport/" + page)
                .param("startTime", String.valueOf(LocalDate.now()))
                .param("endTime", String.valueOf(LocalDate.now())))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("teachers"))
                .andExpect(model().attributeExists("timeInterval"))
                .andExpect(model().attributeExists("currentPage"))
                .andExpect(view().name("headmaster/teacherReport"));

        assertEquals(2, headmasterService.listAllTeachers(page, 20).size());
    }

    //todo
    @Test
    void downloadStudentCardPdf(){

    }

}
