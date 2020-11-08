package com.ediary.services;

import com.ediary.DTO.RoleDto;
import com.ediary.DTO.SchoolDto;
import com.ediary.DTO.UserDto;
import com.ediary.converters.*;
import com.ediary.domain.School;
import com.ediary.domain.security.User;
import com.ediary.exceptions.NotFoundException;
import com.ediary.repositories.SchoolRepository;
import com.ediary.repositories.security.RoleRepository;
import com.ediary.repositories.security.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final SchoolRepository schoolRepository;

    private final UserToUserDto userToUserDto;
    private final UserDtoToUser userDtoToUser;
    private final RoleToRoleDto roleToRoleDto;
    private final SchoolToSchoolDto schoolToSchoolDto;
    private final SchoolDtoToSchool schoolDtoToSchool;


    @Override
    public UserDto initNewUser() {
        return UserDto.builder().build();
    }

    @Override
    public User saveUser(UserDto userDto, List<Long> rolesId) {


        if (userRepository.findByUsername(userDto.getUsername()).isPresent()) {
            throw new BadCredentialsException("Username already taken");
        }

        userDto.setRolesId(rolesId);

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
    public UserDto updateUser(UserDto userDto, List<Long> rolesId) {

        userDto.setRolesId(rolesId);

        return userToUserDto
                .convertForAdmin(userRepository.save(userDtoToUser.convert(userDto)));
    }

    @Override
    public List<UserDto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(userToUserDto::convertForAdmin)
                .collect(Collectors.toList());

    }

    @Override
    public List<RoleDto> getAllRoles() {
        return roleRepository.findAll()
                .stream()
                .map(roleToRoleDto::convert)
                .collect(Collectors.toList());
    }

    @Override
    public School updateSchool(SchoolDto school) {
        return schoolRepository.save(schoolDtoToSchool.convert(school));
    }

    @Override
    public SchoolDto getSchool() {
        return schoolToSchoolDto.convert(schoolRepository.findAll()
                .stream()
                .findFirst().orElseThrow(() -> new NotFoundException("No school has been found")));
    }


    private User getUserById(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User not found"));
    }

}
