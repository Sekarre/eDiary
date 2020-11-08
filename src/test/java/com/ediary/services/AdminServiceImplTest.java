package com.ediary.services;

import com.ediary.DTO.UserDto;
import com.ediary.converters.UserToUserDto;
import com.ediary.domain.security.User;
import com.ediary.repositories.SchoolRepository;
import com.ediary.repositories.security.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

public class AdminServiceImplTest {

    @Mock
    UserRepository userRepository;
    @Mock
    SchoolRepository schoolRepository;
    @Mock
    UserToUserDto userToUserDto;

    AdminService adminService;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        adminService = new AdminServiceImpl(userRepository, schoolRepository, userToUserDto);
    }

    @Test
    void getAllAccounts() {
        UserDto userDto = UserDto.builder().id(1L).build();

        when(userRepository.findAll()).thenReturn(Collections.singletonList(User.builder().build()));
        when(userToUserDto.convertForAdmin(any())).thenReturn(userDto);

        List<UserDto> userDtoList = adminService.getAllAccounts();

        assertNotNull(userDtoList);
        assertEquals(1, userDtoList.size());
    }
}
