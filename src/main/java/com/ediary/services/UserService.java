package com.ediary.services;

import com.ediary.DTO.MessageDto;
import com.ediary.DTO.NoticeDto;
import com.ediary.DTO.UserDto;
import com.ediary.domain.Message;
import com.ediary.domain.Notice;
import com.ediary.domain.security.User;

import java.util.List;

public interface UserService {

    List<UserDto> listUsers();
    Boolean updatePassword(User user, String password, String oldPassword);
    List<MessageDto> listReadMessage(Long userId);
    List<MessageDto> listSendMessage(Long userId);
    MessageDto initNewMessage(Long userId);
    MessageDto addReaderToMessage(MessageDto messageDto, Long readerId);
    MessageDto getReadMessageById(Long messageId, Long userId);
    MessageDto getSendMessageById(Long messageId, Long userId);
    Message sendMessage(MessageDto messageDto);
    List<NoticeDto> listNotices();
    NoticeDto initNewNotice(Long userId);
    Notice addNotice(NoticeDto notice);

}
