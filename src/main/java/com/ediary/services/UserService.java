package com.ediary.services;

import com.ediary.DTO.MessageDto;
import com.ediary.domain.Message;
import com.ediary.domain.Notice;

import java.util.List;

public interface UserService {

    List<MessageDto> listReadMessage(Long userId);
    List<MessageDto> listSendMessage(Long userId);
    MessageDto initNewMessage(Long userId);
    Message sendMessage(MessageDto messageDto);
    List<Notice> listNotices();
    Notice initNewNotice(Long userId);
    Notice addNotice(Notice notice);

}
