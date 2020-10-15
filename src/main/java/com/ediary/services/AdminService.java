package com.ediary.services;

import com.ediary.domain.security.User;

import java.util.List;

public interface AdminService {

    User saveUser(User user);
    Boolean deleteUser(Long userId);
    List<User> getAllUsers();
    User findUser(Long userId);
}
