package com.ediary.converters;

import com.ediary.DTO.UserDto;
import com.ediary.domain.security.User;
import com.ediary.repositories.security.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.HashSet;

@RequiredArgsConstructor
@Component
public class UserDtoToUser implements Converter<UserDto, User> {

    private final RoleRepository roleRepository;
    private final AddressDtoToAddress addressDtoToAddress;

    @Override
    public User convert(UserDto source) {

        if (source == null) {
            return null;
        }

        final User user = new User();

        String[] firstAndLastName = source.getName().split(" ");

        user.setId(source.getId());
        user.setUsername(source.getUsername());
        user.setPassword(source.getPassword());
        user.setFirstName(firstAndLastName[0]);
        user.setLastName(firstAndLastName[1]);

        //Address
        user.setAddress(addressDtoToAddress.convert(source.getAddress()));

        //Roles
        user.setRoles(new HashSet<>(roleRepository.findAllById(source.getRolesId())));

        return user;
    }
}
