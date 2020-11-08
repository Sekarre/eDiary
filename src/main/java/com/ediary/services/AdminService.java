package com.ediary.services;

import com.ediary.DTO.RoleDto;
import com.ediary.DTO.UserDto;
import com.ediary.domain.School;
import com.ediary.domain.security.User;

import java.util.List;

public interface AdminService {

    UserDto initNewUser();
    User saveUser(UserDto userDto, List<Long> rolesId);
    Boolean deleteUser(Long userId);
    UserDto getUser(Long userId);
    UserDto updateUser(UserDto userDto, List<Long> rolesId);
    List<UserDto> getAllUsers();

    List<RoleDto> getAllRoles();

    School saveSchool(School school);
    School getSchool();

}
