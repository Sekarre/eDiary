package com.ediary.services;

import com.ediary.domain.Message;

import java.util.List;

public interface UserService {

    List<Message> listReadMessage(Long userId);
    List<Message> listSendMessage(Long userId);
}
