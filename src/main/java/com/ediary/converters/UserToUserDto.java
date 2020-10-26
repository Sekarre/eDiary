package com.ediary.converters;

import com.ediary.DTO.UserDto;
import com.ediary.domain.Message;
import com.ediary.domain.security.User;
import com.ediary.repositories.MessageRepository;
import lombok.RequiredArgsConstructor;
import lombok.Synchronized;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class UserToUserDto implements Converter<User, UserDto> {

    private final MessageRepository messageRepository;

    @Nullable
    @Synchronized
    @Override
    public UserDto convert(User source) {

        if (source == null) {
            return null;
        }

        final UserDto userDto = new UserDto();
        userDto.setId(source.getId());
        userDto.setName(source.getFirstName() + " " + source.getLastName());
        userDto.setMessageNumber((long) messageRepository.findAllByStatusAndReaders(Message.Status.SENT, source).size());

        return userDto;
    }
}
