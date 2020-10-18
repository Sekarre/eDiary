package com.ediary.services;

import com.ediary.DTO.MessageDto;
import com.ediary.DTO.NoticeDto;
import com.ediary.converters.MessageDtoToMessage;
import com.ediary.converters.MessageToMessageDto;
import com.ediary.converters.NoticeToNoticeDto;
import com.ediary.domain.Message;
import com.ediary.domain.Notice;
import com.ediary.domain.security.User;
import com.ediary.repositories.security.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceImplTest {

    private final Long userId = 1L;

    @Mock
    MessageService messageService;

    @Mock
    UserRepository userRepository;

    @Mock
    NoticeService noticeService;

    @Mock
    MessageToMessageDto messageToMessageDto;

    @Mock
    MessageDtoToMessage messageDtoToMessage;

    @Mock
    NoticeToNoticeDto noticeToNoticeDto;

    UserService userService;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        userService = new UserServiceImpl(messageService, userRepository, noticeService,
                messageToMessageDto, messageDtoToMessage, noticeToNoticeDto);
    }

    @Test
    void listReadMessage() {
        when(userRepository.findById(userId)).thenReturn(Optional.of(User.builder().id(userId).build()));

        when(messageService.listReadMessageByUser(any())).thenReturn(Arrays.asList(
                Message.builder().id(1L).build(),
                Message.builder().id(2L).build()
        ));

        when(messageToMessageDto.convert(any())).thenReturn(MessageDto.builder().id(1L).build());


        List<MessageDto> messagesDto = userService.listReadMessage(userId);

        assertEquals(2, messagesDto.size());
        assertEquals(1L, messagesDto.get(0).getId());
        verify(messageService, times(1)).listReadMessageByUser(any());
        verify(userRepository, times(1)).findById(userId);
        verify(messageToMessageDto, times(2)).convert(any());
    }

    @Test
    void listSendMessage() {
        when(userRepository.findById(userId)).thenReturn(Optional.of(User.builder().id(userId).build()));

        when(messageService.listSendMessageByUser(any())).thenReturn(Arrays.asList(
                Message.builder().id(1L).build(),
                Message.builder().id(2L).build()
        ));

        when(messageToMessageDto.convertWithReaders(any())).thenReturn(MessageDto.builder().id(1L).build());

        List<MessageDto> messagesDto = userService.listSendMessage(userId);

        assertEquals(2, messagesDto.size());
        assertEquals(1L, messagesDto.get(0).getId());
        verify(messageService, times(1)).listSendMessageByUser(any());
        verify(userRepository, times(1)).findById(userId);
        verify(messageToMessageDto, times(2)).convertWithReaders(any());
    }

    @Test
    void initNewMessage() {

        User user = User.builder().id(userId).build();

        Long messageId = 1L;
        Message messageToConvert = Message.builder().id(messageId).senders(user).build();

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        when(messageService.initNewMessageBySender(any())).thenReturn(
                messageToConvert
        );

        when(messageToMessageDto.convert(messageToConvert)).thenReturn(
                MessageDto.builder().id(messageId).sendersId(messageToConvert.getSenders().getId()).build());

        MessageDto messagesDto = userService.initNewMessage(userId);

        assertEquals(user.getId(), messagesDto.getSendersId());
        verify(messageService, times(1)).initNewMessageBySender(user);
        verify(userRepository, times(1)).findById(userId);
        verify(messageToMessageDto, times(1)).convert(messageToConvert);
    }

    @Test
    void sendMessage() {
        Long messageId = 1L;
        MessageDto messageDtoToSend = MessageDto.builder().id(messageId).build();


        when(messageDtoToMessage.convert(messageDtoToSend)).thenReturn(
                Message.builder().id(messageDtoToSend.getId()).build()
        );

        when(messageService.saveMessage(any())).thenReturn(
                Message.builder().id(messageId).build()
        );

        Message messageSaved = userService.sendMessage(messageDtoToSend);

        assertEquals(messageId, messageSaved.getId());
        verify(messageDtoToMessage, times(1)).convert(any());
        verify(messageService, times(1)).saveMessage(any());
    }

    @Test
    void listNotices() {
        when(noticeService.listNotices()).thenReturn(Arrays.asList(
                Notice.builder().id(1L).build(),
                Notice.builder().id(2L).build()
        ));

        when(noticeToNoticeDto.convert(any())).thenReturn(NoticeDto.builder().id(4L).build());

        List<NoticeDto> noticesDto = userService.listNotices();

        assertEquals(2, noticesDto.size());
        assertEquals(4L, noticesDto.get(0).getId());
        verify(noticeService, times(1)).listNotices();
        verify(noticeToNoticeDto, times(2)).convert(any());

    }
    @Test
    void initNewNotice() {
        User user = User.builder().id(userId).build();

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        when(noticeService.initNewNotice(any())).thenReturn(
                Notice.builder().user(user).build()
        );

        Notice notice = userService.initNewNotice(userId);

        assertEquals(user, notice.getUser());
        verify(noticeService, times(1)).initNewNotice(user);
        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    public void addNotice() {
        Long noticeId = 3L;
        Notice noticeToAdd = Notice.builder().id(noticeId).build();

        when(noticeService.addNotice(noticeToAdd)).thenReturn(
                Notice.builder().id(noticeToAdd.getId()).build()
        );

        Notice notice = userService.addNotice(noticeToAdd);

        assertEquals(noticeId, notice.getId());

    }

}