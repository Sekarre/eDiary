package com.ediary.web.controllers;

import com.ediary.domain.Student;
import com.ediary.services.ParentService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Slf4j
class ParentControllerTest {

    private final Long parentId = 1L;

    @Mock
    ParentService parentService;

    MockMvc mockMvc;

    ParentController parentController;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        parentController = new ParentController(parentService);
        mockMvc = MockMvcBuilders.standaloneSetup(parentController).build();
    }


    @Test
    void getAllStudents() throws Exception {

        when(parentService.listStudents(parentId)).thenReturn(
          Collections.singletonList(Student.builder().id(parentId).build())
        );

        mockMvc.perform(get("/parent/" + parentId + "/students"))
                .andExpect(status().isOk())
                .andExpect(view().name("parent/allStudents"))
                .andExpect(model().attributeExists("students"));
    }

}