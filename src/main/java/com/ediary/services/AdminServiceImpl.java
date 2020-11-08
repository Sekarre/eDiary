package com.ediary.services;

import com.ediary.DTO.UserDto;
import com.ediary.converters.UserDtoToUser;
import com.ediary.converters.UserToUserDto;
import com.ediary.domain.School;
import com.ediary.domain.security.User;
import com.ediary.exceptions.NotFoundException;
import com.ediary.repositories.SchoolRepository;
import com.ediary.repositories.security.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final UserRepository userRepository;
    private final SchoolRepository schoolRepository;

    private final UserToUserDto userToUserDto;
    private final UserDtoToUser userDtoToUser;


    @Override
    public UserDto initNewUser() {
        return UserDto.builder().build();
    }

    @Override
    public User saveUser(UserDto userDto) {

        User user = userDtoToUser.convert(userDto);

        if (user != null) {
            return userRepository.save(user);
        }

        return null;
    }

    @Override
    public Boolean deleteUser(Long userId) {
        User user = getUserById(userId);

        user.setEnabled(false);
//        userRepository.delete(user);

        return true;
    }

    @Override
    public UserDto getUser(Long userId) {
        return userToUserDto.convertForAdmin(getUserById(userId));
    }

    @Override
    public List<UserDto> getAllUsers() {

        return userRepository.findAll().stream()
                .map(userToUserDto::convertForAdmin)
                .collect(Collectors.toList());

    }

    @Override
    public School saveSchool(School school) {
        return null;
    }

    @Override
    public School getSchool() {
        return null;
    }

    private User getUserById(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User not found"));
    }

}
