package com.ediary.web.controllers;

import com.ediary.DTO.EventDto;
import com.ediary.DTO.UserDto;
import com.ediary.converters.UserToUserDto;
import com.ediary.domain.security.User;
import com.ediary.services.AdminService;
import com.ediary.web.controllers.AdminController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;


import java.util.Collections;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


public class AdminControllerTest {

    @Mock
    AdminService adminService;
    @Mock
    UserToUserDto userToUserDto;

    AdminController adminController;

    MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        adminController = new AdminController(adminService, userToUserDto);
        mockMvc = MockMvcBuilders.standaloneSetup(adminController).build();

    }

    @Test
    void getAllUsers() throws Exception {

        when(adminService.getAllUsers()).thenReturn(Collections.singletonList(UserDto.builder().build()));

        mockMvc.perform(get("/admin/users"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("users"))
                .andExpect(view().name("admin/users"));

        assertNotNull(adminService.getAllUsers());
    }


    @Test
    void initNewUser() throws Exception {
        when(adminService.initNewUser()).thenReturn(UserDto.builder().build());

        mockMvc.perform(get("/admin/newUser"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("newUser"))
                .andExpect(view().name("admin/newUser"));

        assertNotNull(adminService.initNewUser());
    }

    @Test
    void processNewUser() throws Exception {

        when(adminService.saveUser(any())).thenReturn(User.builder().build());

        mockMvc.perform(post("/admin/newUser")
                .contentType(MediaType.APPLICATION_JSON)
                .content(AbstractAsJsonControllerTest.asJsonString(UserDto.builder().build())))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/users"));

        assertNotNull(adminService.saveUser(any()));

    }

}
