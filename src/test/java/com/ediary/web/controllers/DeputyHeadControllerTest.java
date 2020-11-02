package com.ediary.web.controllers;

import com.ediary.DTO.*;
import com.ediary.converters.UserToUserDto;
import com.ediary.domain.Class;
import com.ediary.services.DeputyHeadService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class DeputyHeadControllerTest {


    @Mock
    UserToUserDto userToUserDto;

    @Mock
    DeputyHeadService deputyHeadService;

    DeputyHeadController deputyHeadController;

    MockMvc mockMvc;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        deputyHeadController = new DeputyHeadController(deputyHeadService, userToUserDto);
        mockMvc = MockMvcBuilders.standaloneSetup(deputyHeadController).build();
    }

    @Test
    void newClass() throws Exception {
        when(deputyHeadService.initNewClass()).thenReturn(ClassDto.builder().build());
        when(deputyHeadService.listAllStudentsWithoutClass()).thenReturn(Collections.singletonList(StudentDto.builder().build()));
        when(deputyHeadService.listAllTeachersWithoutClass()).thenReturn(Collections.singletonList(TeacherDto.builder().build()));


        mockMvc.perform(get("/deputyHead/newClass"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("schoolClass"))
                .andExpect(model().attributeExists("students"))
                .andExpect(model().attributeExists("teachers"))
                .andExpect(view().name("deputyHead/newClass"));

        assertNotNull(deputyHeadService.listAllStudentsWithoutClass());
        assertNotNull(deputyHeadService.listAllTeachersWithoutClass());
        assertNotNull(deputyHeadService.initNewClass());

    }

    @Test
    void processNewClass() throws Exception {
        when(deputyHeadService.saveClass(any(), any())).thenReturn(Class.builder().build());

        mockMvc.perform(post("/deputyHead/newClass")
                .param("studentsId", String.valueOf(1L))
                .contentType(MediaType.APPLICATION_JSON)
                .content(AbstractAsJsonControllerTest.asJsonString(StudentCouncilDto.builder().build())))
                .andExpect(status().isOk())
                .andExpect(view().name("deputyHead/classes"));

        verify(deputyHeadService, times(1)).saveClass(any(), any());
    }

    @Test
    void getClasses() throws Exception {
        when(deputyHeadService.listAllClasses()).thenReturn(Collections.singletonList(ClassDto.builder().build()));


        mockMvc.perform(get("/deputyHead/classes"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("classes"))
                .andExpect(view().name("deputyHead/classes"));

        assertNotNull(deputyHeadService.listAllClasses());
    }

    @Test
    void getOneClass() throws Exception {
        when(deputyHeadService.getSchoolClass(any())).thenReturn(ClassDto.builder().build());


        mockMvc.perform(get("/deputyHead/classes/" + 1L))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("class"))
                .andExpect(view().name("deputyHead/oneClass"));

        assertNotNull(deputyHeadService.getSchoolClass(1L));
    }

    @Test
    void deleteClass() throws Exception {
        mockMvc.perform(delete("/deputyHead/classes/" + 1L))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/deputyHead/classes"));

        verify(deputyHeadService, times(1)).deleteClass(any());
    }

    @Test
    void removeFormTutorFromClass() throws Exception {
        when(deputyHeadService.removeFormTutorFromClass(any(), any())).thenReturn(ClassDto.builder().build());
        when(deputyHeadService.listAllStudentsWithoutClass()).thenReturn(Collections.singletonList(StudentDto.builder().build()));
        when(deputyHeadService.listAllTeachersWithoutClass()).thenReturn(Collections.singletonList(TeacherDto.builder().build()));


        mockMvc.perform(post("/deputyHead/classes/" + 1L + "/teacher/" + 1L))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("schoolClass"))
                .andExpect(model().attributeExists("students"))
                .andExpect(model().attributeExists("teachers"))
                .andExpect(view().name("deputyHead/oneClass"));

        assertNotNull(deputyHeadService.listAllStudentsWithoutClass());
        assertNotNull(deputyHeadService.listAllTeachersWithoutClass());
        assertNotNull(deputyHeadService.removeFormTutorFromClass(1L, 1L));
    }

    @Test
    void removeStudentFromClass() throws Exception {
        when(deputyHeadService.removeStudentFromClass(any(), any())).thenReturn(ClassDto.builder().build());
        when(deputyHeadService.listAllStudentsWithoutClass()).thenReturn(Collections.singletonList(StudentDto.builder().build()));
        when(deputyHeadService.listAllTeachersWithoutClass()).thenReturn(Collections.singletonList(TeacherDto.builder().build()));


        mockMvc.perform(post("/deputyHead/classes/" + 1L + "/student/" + 1L))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("schoolClass"))
                .andExpect(model().attributeExists("students"))
                .andExpect(model().attributeExists("teachers"))
                .andExpect(view().name("deputyHead/oneClass"));

        assertNotNull(deputyHeadService.listAllStudentsWithoutClass());
        assertNotNull(deputyHeadService.listAllTeachersWithoutClass());
        assertNotNull(deputyHeadService.removeStudentFromClass(1L, 1L));
    }

}
