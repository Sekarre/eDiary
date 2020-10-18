package com.ediary.converters;

import com.ediary.DTO.MessageDto;
import com.ediary.domain.Message;
import com.ediary.domain.security.User;
import com.ediary.repositories.security.UserRepository;
import lombok.Synchronized;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class MessageDtoToMessage implements Converter<MessageDto, Message> {

    private final UserRepository userRepository;

    public MessageDtoToMessage(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Synchronized
    @Nullable
    @Override
    public Message convert(MessageDto source) {

        if(source == null){
            return null;
        }

        final Message message = new Message();

        message.setId(source.getId());
        message.setTitle(source.getTitle());
        message.setStatus(source.getStatus());

        Optional<User> userOptional = userRepository.findById(source.getSendersId());
        if(userOptional.isPresent())
            message.setSenders(userOptional.get());

        return message;
    }
}
