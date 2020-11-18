package com.ediary.web.controllers;

import com.ediary.DTO.AddressDto;
import com.ediary.DTO.EventDto;
import com.ediary.DTO.SchoolDto;
import com.ediary.DTO.UserDto;
import com.ediary.converters.UserToUserDto;
import com.ediary.domain.Address;
import com.ediary.domain.School;
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
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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
                .andExpect(view().name("admin/allUsers"));

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

        when(adminService.saveUser(any(), any())).thenReturn(User.builder().build());

        mockMvc.perform(post("/admin/newUser")
                .contentType(MediaType.APPLICATION_JSON)
                .content(AbstractAsJsonControllerTest.asJsonString(UserDto.builder().build()))
                .param("selectedRoles", "1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/admin/users"));

        assertNotNull(adminService.saveUser(any(), any()));

    }

    @Test
    void getUser() throws Exception {
        Long userId = 1L;

        when(adminService.getUser(any())).thenReturn(UserDto.builder().build());

        mockMvc.perform(get("/admin/users/" + userId))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("user"))
                .andExpect(view().name("admin/user"));

        verify(adminService, times(1)).getUser(userId);
        assertNotNull(adminService.getUser(userId));
    }

    @Test
    void deleteUser() throws Exception {
        Long userId = 1L;

        when(adminService.deleteUser(any())).thenReturn(true);

        mockMvc.perform(delete("/admin/users/" + userId + "/delete"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/users"));

        verify(adminService, times(1)).deleteUser(userId);
        assertTrue(adminService.deleteUser(userId));
    }

    @Test
    void editUser() throws Exception {
        Long userId = 1L;

        when(adminService.getUser(any())).thenReturn(UserDto.builder().build());

        mockMvc.perform(get("/admin/users/" + userId +"/edit"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("user"))
                .andExpect(view().name("admin/editUser"));

        verify(adminService, times(1)).getUser(userId);
        assertNotNull(adminService.getUser(userId));
    }

    @Test
    void updateUser() throws Exception {
        Long userId = 1L;

        UserDto userDto = UserDto.builder().id(userId).build();
        when(adminService.updateUser(any(), any())).thenReturn(userDto);

        mockMvc.perform(post("/admin/users/" + userId + "/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(AbstractAsJsonControllerTest.asJsonString(userDto))
                .param("roleId", "1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/admin/users/" + userId));

        assertNotNull(adminService.updateUser(any(), any()));
    }



}
