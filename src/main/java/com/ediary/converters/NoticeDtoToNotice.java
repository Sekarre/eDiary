package com.ediary.converters;

import com.ediary.DTO.NoticeDto;
import com.ediary.domain.Notice;
import com.ediary.domain.security.User;
import com.ediary.repositories.security.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.Synchronized;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import java.util.Optional;

@RequiredArgsConstructor
@Component
public class NoticeDtoToNotice implements Converter<NoticeDto, Notice> {

    private final UserRepository userRepository;

    @Synchronized
    @Nullable
    @Override
    public Notice convert(NoticeDto source) {

        if(source == null){
            return null;
        }

        final Notice notice = new Notice();
        notice.setId(source.getId());
        notice.setTitle(source.getTitle());
        notice.setContent(source.getContent());
        notice.setDate(source.getDate());

        //author
        Optional<User> userOptional = userRepository.findById(source.getAuthorId());
        if(userOptional.isPresent()) {
            notice.setUser(userOptional.get());
        }

        return notice;
    }
}
