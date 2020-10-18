package com.ediary.services;

import com.ediary.DTO.MessageDto;
import com.ediary.DTO.NoticeDto;
import com.ediary.domain.Message;
import com.ediary.domain.Notice;

import java.util.List;

public interface UserService {

    List<MessageDto> listReadMessage(Long userId);
    List<MessageDto> listSendMessage(Long userId);
    MessageDto initNewMessage(Long userId);
    Message sendMessage(MessageDto messageDto);
    List<NoticeDto> listNotices();
    NoticeDto initNewNotice(Long userId);
    Notice addNotice(NoticeDto notice);

}
