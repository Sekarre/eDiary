package com.ediary.web.controllers;

import com.ediary.domain.Message;
import com.ediary.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.beans.PropertyEditorSupport;
import java.time.LocalDate;

@RequiredArgsConstructor
@Controller
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @InitBinder
    public void dataBinder(WebDataBinder dataBinder){
        dataBinder.setDisallowedFields("id");

        dataBinder.registerCustomEditor(LocalDate.class, new PropertyEditorSupport(){
            @Override
            public void setAsText(String text) throws IllegalArgumentException {
                setValue(LocalDate.parse(text));
            }
        });
    }

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

    @GetMapping("/{userId}/newMessages")
    public String newMessage(@PathVariable Long userId, Model model) {

        model.addAttribute("message", userService.initNewMessage(userId));
        return "student/newMessages";
    }

    @PostMapping("/{userId}/newMessages")
    public String processNewMessage(@Valid @ModelAttribute Message message, BindingResult result) {
        if (result.hasErrors()){
            //TODO
            return "/";
        } else {
            userService.sendMessage(message);
            return "redirect:student/sendMessages";
        }
    }

    @GetMapping("/notice")
    public String getAllNotices(Model model) {

        model.addAttribute("notices", userService.listNotices());
        return "student/allNotices";
    }


}
