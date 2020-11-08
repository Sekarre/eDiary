package com.ediary.services;

import com.ediary.DTO.UserDto;
import com.ediary.converters.UserDtoToUser;
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
    @Mock
    UserDtoToUser userDtoToUser;

    AdminService adminService;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        adminService = new AdminServiceImpl(userRepository, schoolRepository, userToUserDto, userDtoToUser);
    }

    @Test
    void initNewUser() {
        UserDto userDto = adminService.initNewUser();

        assertNotNull(userDto);
    }

    @Test
    void saveUser() {
        Long userId = 1L;

        User user = User.builder().id(userId).build();
        UserDto userDto = UserDto.builder().id(userId).build();

        when(userDtoToUser.convert(any())).thenReturn(user);
        when(userRepository.save(any())).thenReturn(user);

        User savedUser = adminService.saveUser(userDto);

        assertNotNull(savedUser);
        assertEquals(savedUser, user);
        assertTrue(savedUser.getEnabled());
        assertTrue(savedUser.isAccountNonExpired());
        verify(userRepository, times(1)).save(user);
    }


    @Test
    void deleteUser() {
        Long userId = 1L;

        User user = User.builder().enabled(true).id(userId).build();

        when(userRepository.findById(any())).thenReturn(java.util.Optional.ofNullable(user));

        Boolean result = adminService.deleteUser(userId);

        assertTrue(result);
        verify(userRepository, times(1)).findById(userId);

    }

    @Test
    void getUser() {
        Long userId = 1L;

        User user = User.builder().id(userId).build();
        UserDto userDto = UserDto.builder().build();

        when(userToUserDto.convertForAdmin(any())).thenReturn(userDto);
        when(userRepository.findById(any())).thenReturn(java.util.Optional.ofNullable(user));

        UserDto foundUser = adminService.getUser(userId);

        assertNotNull(foundUser);
        assertEquals(foundUser, userDto);
        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    void updateUser() {
        Long userId = 1L;

        User user = User.builder().id(userId).build();
        UserDto userDto = UserDto.builder().id(userId).build();

        when(userDtoToUser.convert(any())).thenReturn(user);
        when(userToUserDto.convertForAdmin(any())).thenReturn(userDto);
        when(userRepository.save(any())).thenReturn(user);

        User savedUser = adminService.saveUser(userDto);

        assertNotNull(savedUser);
        assertEquals(savedUser, user);
        assertTrue(savedUser.getEnabled());
        assertTrue(savedUser.isAccountNonExpired());
        verify(userRepository, times(1)).save(user);
    }


    @Test
    void getAllUsers() {
        UserDto userDto = UserDto.builder().id(1L).build();

        when(userRepository.findAll()).thenReturn(Collections.singletonList(User.builder().build()));
        when(userToUserDto.convertForAdmin(any())).thenReturn(userDto);

        List<UserDto> userDtoList = adminService.getAllUsers();

        assertNotNull(userDtoList);
        assertEquals(1, userDtoList.size());
    }

}
