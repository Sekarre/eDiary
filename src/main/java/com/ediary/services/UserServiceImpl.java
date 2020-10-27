package com.ediary.services;

import com.ediary.DTO.MessageDto;
import com.ediary.DTO.NoticeDto;
import com.ediary.DTO.UserDto;
import com.ediary.converters.*;
import com.ediary.domain.Message;
import com.ediary.domain.Notice;
import com.ediary.domain.security.User;
import com.ediary.exceptions.NoAccessException;
import com.ediary.exceptions.NotFoundException;
import com.ediary.repositories.MessageRepository;
import com.ediary.repositories.security.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final MessageService messageService;
    private final UserRepository userRepository;
    private final NoticeService noticeService;

    private final MessageToMessageDto messageToMessageDto;
    private final MessageDtoToMessage messageDtoToMessage;
    private final MessageRepository messageRepository;

    private final NoticeToNoticeDto noticeToNoticeDto;
    private final NoticeDtoToNotice noticeDtoToNotice;
    private final UserToUserDto userToUserDto;

    private final PasswordEncoder passwordEncoder;

    @Override
    public List<UserDto> listUsers() {
        return userRepository.findAll().stream().map(userToUserDto::convert).collect(Collectors.toList());
    }

    @Override
    public Boolean updatePassword(User user, String password, String oldPassword) {

        if (!user.getPassword().equals(passwordEncoder.encode(oldPassword))) {
            return false;
        }

        //TODO validation
        if (password.isEmpty()) {
            return false;
        }

        user.setPassword(passwordEncoder.encode(password));
        userRepository.save(user);
        return true;
    }

    @Override
    public List<MessageDto> listReadMessage(Long userId) {

        User user = getUserById(userId);

        return messageService.listReadMessageByUser(user).stream().map(messageToMessageDto::convert).collect(Collectors.toList());
    }

    @Override
    public List<MessageDto> listSendMessage(Long userId) {

        User user = getUserById(userId);

        return messageService.listSendMessageByUser(user).stream().map(messageToMessageDto::convertWithReaders).collect(Collectors.toList());
    }

    @Override
    public MessageDto initNewMessage(Long userId) {

        User user = getUserById(userId);

        Message message = messageService.initNewMessageBySender(user);
        return messageToMessageDto.convertWithReaders(message);
    }

    @Override
    public MessageDto addReaderToMessage(MessageDto messageDto, Long readerId) {
        Message message = messageDtoToMessage.convert(messageDto);

        Optional<User> userOptional = userRepository.findById(readerId);
        if (userOptional.isPresent()) {
            Set<User> s = new HashSet<User>();
            s.addAll(message.getReaders());
            s.add(userOptional.get());
            message.setReaders(s);
        }
        return messageToMessageDto.convertWithReaders(message);
    }

    @Override
    public MessageDto getReadMessageById(Long messageId, Long userId) {
        User user = getUserById(userId);

        Optional<Message> messageOptional = messageRepository.findById(messageId);
        if (!messageOptional.isPresent()){
            throw new NotFoundException("Message Not Found.");
        }

        Message message = messageOptional.get();
        if (message.getReaders().contains(user)) {
            if (message.getStatus().equals(Message.Status.SENT)) {
                message.setStatus(Message.Status.READ);
                messageRepository.save(message);
            }
            return messageToMessageDto.convert(message);

        } else {
            throw new NoAccessException();
        }
    }

    @Override
    public Message sendMessage(MessageDto messageDto) {
        messageDto.setDate(new Date());
        messageDto.setStatus(Message.Status.SENT);

        return messageService.saveMessage(messageDtoToMessage.convert(messageDto));
    }

    @Override
    public List<NoticeDto> listNotices() {
        return noticeService.listNotices().stream().map(noticeToNoticeDto::convert).collect(Collectors.toList());
    }

    @Override
    public NoticeDto initNewNotice(Long userId) {

        User user = getUserById(userId);

        Notice notice = noticeService.initNewNotice(user);
        return noticeToNoticeDto.convert(notice);
    }

    @Override
    public Notice addNotice(NoticeDto noticeDto) {

        return noticeService.addNotice(noticeDtoToNotice.convert(noticeDto));
    }

    private User getUserById(Long userId) {
        Optional<User> userOptional = userRepository.findById(userId);

        if (!userOptional.isPresent()) {
            throw new NotFoundException("User Not Found.");
        }

        return userOptional.get();
    }
}
