package com.ediary.services;

import com.ediary.domain.School;
import com.ediary.domain.security.User;

import java.util.List;

public interface AdminService {

    User saveUser(User user);
    Boolean deleteUser(Long userId);
    User findUser(Long userId);
    List<User> getAllUsers();

    School saveSchool(School school);
    School getSchool();
}
