package com.ediary.services;

import com.ediary.DTO.AddressDto;
import com.ediary.DTO.RoleDto;
import com.ediary.DTO.SchoolDto;
import com.ediary.DTO.UserDto;
import com.ediary.bootstrap.DefaultLoader;
import com.ediary.converters.*;
import com.ediary.domain.*;
import com.ediary.domain.security.Role;
import com.ediary.domain.security.User;
import com.ediary.exceptions.NotFoundException;
import com.ediary.repositories.*;
import com.ediary.repositories.security.RoleRepository;
import com.ediary.repositories.security.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final SchoolRepository schoolRepository;
    private final AddressRepository addressRepository;
    private final StudentRepository studentRepository;
    private final ParentRepository parentRepository;
    private final TeacherRepository teacherRepository;

    private final UserToUserDto userToUserDto;
    private final UserDtoToUser userDtoToUser;
    private final RoleToRoleDto roleToRoleDto;
    private final SchoolToSchoolDto schoolToSchoolDto;
    private final SchoolDtoToSchool schoolDtoToSchool;

    private final PasswordEncoder passwordEncoder;


    @Override
    public UserDto initNewUser() {
        return UserDto.builder().address(AddressDto.builder().build()).build();
    }

    @Override
    public User saveUser(UserDto userDto, List<Long> rolesId) {


        if (userRepository.findByUsername(userDto.getUsername()).isPresent()) {
            throw new BadCredentialsException("Username already taken");
        }

        userDto.setRolesId(rolesId);
        userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));

        User user = userDtoToUser.convert(userDto);
        Address address = user.getAddress();
        addressRepository.save(address);
        user.setAddress(address);

        if (user == null) {
            return null;
        }

        User savedUser = userRepository.save(user);

        Set<Role> roles = user.getRoles();
        roles.forEach(role -> {
            switch (role.getName()) {
                case DefaultLoader.STUDENT_ROLE:
                    studentRepository.save(Student.builder().user(savedUser).build());
                    break;
                case DefaultLoader.PARENT_ROLE:
                    parentRepository.save(Parent.builder().user(savedUser).build());
                    break;
                case DefaultLoader.TEACHER_ROLE:
                    teacherRepository.save(Teacher.builder().user(savedUser).build());
                    break;
            }
        });

        return savedUser;
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
