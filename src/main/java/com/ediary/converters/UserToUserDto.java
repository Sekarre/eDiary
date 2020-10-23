package com.ediary.converters;

import com.ediary.DTO.UserDto;
import com.ediary.domain.security.User;
import lombok.Synchronized;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

@Component
public class UserToUserDto implements Converter<User, UserDto> {

    @Nullable
    @Synchronized
    @Override
    public UserDto convert(User source) {

        if (source == null) {
            return null;
        }

        final UserDto userDto = new UserDto();
        userDto.setId(source.getId());

        return userDto;
    }
}
