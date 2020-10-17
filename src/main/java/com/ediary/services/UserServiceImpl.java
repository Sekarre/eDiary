package com.ediary.services;

import com.ediary.domain.Message;
import com.ediary.domain.security.User;
import com.ediary.exceptions.NotFoundException;
import com.ediary.repositories.security.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final MessageService messageService;
    private final UserRepository userRepository;

    @Override
    public List<Message> listReadMessage(Long userId) {

        User user = getUserById(userId);

        return messageService.listReadMessageByUser(user);
    }

    @Override
    public List<Message> listSendMessage(Long userId) {

        User user = getUserById(userId);

        return messageService.listSendMessageByUser(user);
    }

    @Override
    public Message initNewMessage(Long userId) {

        User user = getUserById(userId);

        return messageService.initNewMessageBySender(user);
    }

    @Override
    public Message sendMessage(Message message) {
        return messageService.saveMessage(message);
    }

    private User getUserById(Long userId) {
        Optional<User> userOptional = userRepository.findById(userId);

        if (!userOptional.isPresent()) {
            throw new NotFoundException("User Not Found.");
        }

        return userOptional.get();
    }
}
