package com.ediary.services;

import com.ediary.DTO.MessageDto;
import com.ediary.DTO.NoticeDto;
import com.ediary.converters.MessageDtoToMessage;
import com.ediary.converters.MessageToMessageDto;
import com.ediary.converters.NoticeDtoToNotice;
import com.ediary.converters.NoticeToNoticeDto;
import com.ediary.domain.Message;
import com.ediary.domain.Notice;
import com.ediary.domain.security.User;
import com.ediary.exceptions.NotFoundException;
import com.ediary.repositories.security.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final MessageService messageService;
    private final UserRepository userRepository;
    private final NoticeService noticeService;

    private final MessageToMessageDto messageToMessageDto;
    private final MessageDtoToMessage messageDtoToMessage;

    private final NoticeToNoticeDto noticeToNoticeDto;
    private final NoticeDtoToNotice noticeDtoToNotice;

    private final PasswordEncoder passwordEncoder;

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
        return messageToMessageDto.convert(message);
    }

    @Override
    public Message sendMessage(MessageDto messageDto) {

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
