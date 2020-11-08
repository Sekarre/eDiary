package com.ediary.web;

import com.ediary.DTO.UserDto;
import com.ediary.converters.UserToUserDto;
import com.ediary.domain.security.User;
import com.ediary.services.AdminService;
import com.ediary.web.controllers.AdminController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;


import java.util.Collections;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
    void getAllAccounts() throws Exception {

        when(adminService.getAllAccounts()).thenReturn(Collections.singletonList(UserDto.builder().build()));

        mockMvc.perform(get("/admin/accounts"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("accounts"))
                .andExpect(view().name("admin/accounts"));

        assertNotNull(adminService.getAllAccounts());
    }


}
