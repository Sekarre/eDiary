package com.ediary.services;

import com.ediary.domain.Message;
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

    UserService userService;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        userService = new UserServiceImpl(messageService, userRepository);
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
}