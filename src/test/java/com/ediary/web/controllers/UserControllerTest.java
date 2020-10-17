package com.ediary.web.controllers;

import com.ediary.domain.Message;
import com.ediary.domain.Notice;
import com.ediary.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.Mockito.*;

class UserControllerTest {

    private final Long userId = 1L;

    @Mock
    UserService userService;

    UserController userController;

    MockMvc mockMvc;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        userController = new UserController(userService);
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    @Test
    void getReadMessages() throws Exception {

        when(userService.listReadMessage(userId)).thenReturn(Arrays.asList(
                Message.builder().id(1L).build(),
                Message.builder().id(2L).build()
        ));

        mockMvc.perform(get("/user/"+ userId +"/readMessages"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("readMessages"))
                .andExpect(view().name("student/readMessages"));

        verify(userService, times(1)).listReadMessage(userId);
        assertEquals(2, userService.listReadMessage(userId).size());

    }

    @Test
    void getSendMessages() throws Exception {
        when(userService.listSendMessage(userId)).thenReturn(Arrays.asList(
                Message.builder().id(1L).build(),
                Message.builder().id(2L).build()
        ));

        mockMvc.perform(get("/user/"+ userId +"/sendMessages"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("sendMessages"))
                .andExpect(view().name("student/sendMessages"));

        verify(userService, times(1)).listSendMessage(userId);
        assertEquals(2, userService.listSendMessage(userId).size());
    }

    @Test
    void newMessage() throws Exception {
        when(userService.initNewMessage(userId)).thenReturn(Message.builder().build());

        mockMvc.perform(get("/user/"+ userId +"/newMessages"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("message"))
                .andExpect(view().name("student/newMessages"));

        verify(userService, times(1)).initNewMessage(userId);
    }

    @Test
    void processNewMessage() throws Exception {
        when(userService.sendMessage(any())).thenReturn(Message.builder().build());

        mockMvc.perform(post("/user/"+ userId +"/newMessages"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:student/sendMessages"));

        verify(userService).sendMessage(any());
    }

    @Test
    void getAllNotices() throws Exception {
        when(userService.listNotices()).thenReturn(Arrays.asList(
                Notice.builder().id(1L).build(),
                Notice.builder().id(2L).build()
        ));

        mockMvc.perform(get("/user/notice"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("notices"))
                .andExpect(view().name("student/allNotices"));

        verify(userService, times(1)).listNotices();
        assertEquals(2, userService.listNotices().size());
        assertEquals(2L, userService.listNotices().get(1).getId());
    }
}