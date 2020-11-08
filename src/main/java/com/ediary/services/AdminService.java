package com.ediary.services;

import com.ediary.DTO.UserDto;
import com.ediary.domain.School;
import com.ediary.domain.security.User;

import java.util.List;

public interface AdminService {

    UserDto initNewUser();
    User saveUser(UserDto userDto);
    Boolean deleteUser(Long userId);
    User findUser(Long userId);

    List<UserDto> getAllUsers();


    School saveSchool(School school);

    School getSchool();
}
