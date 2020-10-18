package com.ediary.services;

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

    UserService userService;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        userService = new UserServiceImpl(messageService, userRepository, noticeService);
    }

    @Test
    void listReadMessage() {
        when(userRepository.findById(userId)).thenReturn(Optional.of(User.builder().id(userId).build()));

        when(messageService.listReadMessageByUser(any())).thenReturn(Arrays.asList(
                Message.builder().id(1L).build(),
                Message.builder().id(2L).build()
        ));

        List<Message> messages = userService.listReadMessage(userId);

        assertEquals(2, messages.size());
        assertEquals(1L, messages.get(0).getId());
        verify(messageService, times(1)).listReadMessageByUser(any());
        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    void listSendMessage() {
        when(userRepository.findById(userId)).thenReturn(Optional.of(User.builder().id(userId).build()));

        when(messageService.listSendMessageByUser(any())).thenReturn(Arrays.asList(
                Message.builder().id(1L).build(),
                Message.builder().id(2L).build()
        ));

        List<Message> messages = userService.listSendMessage(userId);

        assertEquals(2, messages.size());
        assertEquals(1L, messages.get(0).getId());
        verify(messageService, times(1)).listSendMessageByUser(any());
        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    void initNewMessage() {
        User user = User.builder().id(userId).build();

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        when(messageService.initNewMessageBySender(any())).thenReturn(
                Message.builder().senders(user).build()
        );

        Message messages = userService.initNewMessage(userId);

        assertEquals(user, messages.getSenders());
        verify(messageService, times(1)).initNewMessageBySender(user);
        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    void sendMessage() {
        Long messageId = 1L;
        Message messageToSend = Message.builder().id(messageId).build();

        when(messageService.saveMessage(messageToSend)).thenReturn(
                Message.builder().id(messageToSend.getId()).build()
        );

        Message message = userService.sendMessage(messageToSend);

        assertEquals(messageId, message.getId());
    }

    @Test
    void listNotices() {
        when(noticeService.listNotices()).thenReturn(Arrays.asList(
                Notice.builder().id(1L).build(),
                Notice.builder().id(2L).build()
        ));

        List<Notice> notices = userService.listNotices();

        assertEquals(2, notices.size());
        assertEquals(1L, notices.get(0).getId());
        verify(noticeService, times(1)).listNotices();

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