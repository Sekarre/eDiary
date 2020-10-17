package com.ediary.services;

import com.ediary.domain.Message;
import com.ediary.domain.Notice;

import java.util.List;

public interface UserService {

    List<Message> listReadMessage(Long userId);
    List<Message> listSendMessage(Long userId);
    Message initNewMessage(Long userId);
    Message sendMessage(Message message);
    List<Notice> listNotices();
}
