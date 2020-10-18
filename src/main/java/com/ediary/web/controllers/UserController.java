package com.ediary.web.controllers;

import com.ediary.DTO.MessageDto;
import com.ediary.DTO.NoticeDto;
import com.ediary.domain.Message;
import com.ediary.domain.Notice;
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
        return "user/readMessages";
    }

    @GetMapping("/{userId}/sendMessages")
    public String getSendMessages(@PathVariable Long userId, Model model) {

        model.addAttribute("sendMessages", userService.listSendMessage(userId));
        return "user/sendMessages";
    }

    @GetMapping("/{userId}/newMessages")
    public String newMessage(@PathVariable Long userId, Model model) {

        model.addAttribute("message", userService.initNewMessage(userId));
        return "user/newMessages";
    }

    @PostMapping("/{userId}/newMessages")
    public String processNewMessage(@Valid @ModelAttribute MessageDto message, BindingResult result) {
        if (result.hasErrors()){
            //TODO
            return "/";
        } else {
            userService.sendMessage(message);
            return "redirect:user/sendMessages";
        }
    }

    @GetMapping("/notice")
    public String getAllNotices(Model model) {

        model.addAttribute("notices", userService.listNotices());
        return "user/allNotices";
    }

    @GetMapping("/{userId}/newNotice")
    public String newNotice(@PathVariable Long userId, Model model) {

        model.addAttribute("notice", userService.initNewNotice(userId));
        return "user/newNotice";
    }

    @PostMapping("/{userId}/newNotice")
    public String processNewNotice(@Valid @ModelAttribute NoticeDto notice, BindingResult result) {
        if (result.hasErrors()){
            //TODO
            return "/";
        } else {
            userService.addNotice(notice);
            return "redirect:user/allNotices";
        }
    }



}
