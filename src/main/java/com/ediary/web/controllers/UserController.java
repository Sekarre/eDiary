package com.ediary.web.controllers;

import com.ediary.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@RequiredArgsConstructor
@Controller
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @GetMapping("/{userId}/readMessages")
    public String getReadMessages(@PathVariable Long userId, Model model) {

        model.addAttribute("readMessages", userService.listReadMessage(userId));
        return "student/readMessages";
    }

    @GetMapping("/{userId}/sendMessages")
    public String getSendMessages(@PathVariable Long userId, Model model) {

        model.addAttribute("sendMessages", userService.listSendMessage(userId));
        return "student/sendMessages";
    }

}
