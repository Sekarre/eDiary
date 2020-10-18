package com.ediary.converters;

import com.ediary.DTO.MessageDto;
import com.ediary.domain.Message;
import com.ediary.domain.security.User;
import lombok.Synchronized;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;


@Component
public class MessageToMessageDto implements Converter<Message, MessageDto> {

    @Synchronized
    @Nullable
    @Override
    public MessageDto convert(Message source) {

        if(source == null){
            return null;
        }

        final MessageDto messageDTO = new MessageDto();
        messageDTO.setId(source.getId());
        messageDTO.setTitle(source.getTitle());
        messageDTO.setContent(source.getContent());
        messageDTO.setStatus(source.getStatus());

        //Senders
        messageDTO.setSendersId(source.getSenders().getId());
        messageDTO.setSendersName(
                source.getSenders().getFirstName() + " " + source.getSenders().getLastName()
        );

        return messageDTO;
    }

    @Synchronized
    @Nullable
    public MessageDto convertWithReaders(Message source) {
        if(source == null){
            return null;
        }

        final MessageDto messageDTO = new MessageDto();
        messageDTO.setId(source.getId());
        messageDTO.setTitle(source.getTitle());
        messageDTO.setTitle(source.getTitle());
        messageDTO.setStatus(source.getStatus());

        //Senders
        messageDTO.setSendersId(source.getSenders().getId());
        messageDTO.setSendersName(
                source.getSenders().getFirstName() + " " + source.getSenders().getLastName()
        );

        messageDTO.setReadersId(source.getReaders().stream().map(User::getId).collect(Collectors.toList()));
        messageDTO.setReadersName(
                source.getReaders().stream().map(user -> {
                            return user.getFirstName() + " " + user.getLastName();
                        }
                ).collect(Collectors.toList())
        );

        return messageDTO;
    }
}
