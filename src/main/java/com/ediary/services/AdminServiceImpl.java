package com.ediary.services;

import com.ediary.DTO.UserDto;
import com.ediary.converters.UserDtoToUser;
import com.ediary.converters.UserToUserDto;
import com.ediary.domain.School;
import com.ediary.domain.security.User;
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
            user.setAccountNonExpired(true);
            user.setAccountNonLocked(true);
            user.setCredentialsNonExpired(true);
            user.setEnabled(true);

            return userRepository.save(user);
        }

        return null;
    }

    @Override
    public Boolean deleteUser(Long userId) {
        return null;
    }

    @Override
    public User findUser(Long userId) {
        return null;
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
}
