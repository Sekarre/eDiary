package com.ediary.web.controllers;

import com.ediary.DTO.MessageDto;
import com.ediary.DTO.NoticeDto;
import com.ediary.converters.UserToUserDto;
import com.ediary.domain.security.User;
import com.ediary.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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

    private final UserToUserDto userToUserDto;

    @ModelAttribute
    public void addAuthenticatedUser(@AuthenticationPrincipal User user, Model model) {
        model.addAttribute("user", userToUserDto.convert(user));
    }

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

    @GetMapping("/{userId}/updatePassword")
    public String updatePassword(@PathVariable Long userId) {
        return "user/updatePassword";
    }

    @PostMapping("/{userId}/updatePassword")
    public String processUpdatePassword(@PathVariable Long userId,
                                        @RequestParam("password") String password,
                                        @RequestParam("oldPassword") String oldPassword,
                                        @AuthenticationPrincipal User user,
                                        Model model) {

        userService.updatePassword(user, password, oldPassword);
        return "/";
    }

    @GetMapping("/{userId}/messages")
    public String mainMessages(@PathVariable Long userId, Model model) {
        return "user/messages";
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

        model.addAttribute("readers", userService.listUsers());
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
            return "redirect:/user/" + message.getSendersId() + "/sendMessages";
        }
    }

    @PostMapping("/{userId}/newMessages/addReader/{readerId}")
    public String addReaderToMessage(@PathVariable Long userId,
                                     @PathVariable Long readerId,
                                     @ModelAttribute MessageDto message,
                                     Model model) {
        model.addAttribute("readers", userService.listUsers());
        model.addAttribute("message", userService.addReaderToMessage(message, readerId));
        return "user/newMessages";

    }

    @GetMapping("/{userId}/readMessages/{messageId}")
    public String viewReadMessage(@PathVariable Long userId, @PathVariable Long messageId, Model model) {

        model.addAttribute("message", userService.getReadMessageById(messageId, userId));
        return "/user/viewReadMessage";
    }

    @PostMapping("/{userId}/readMessages/{messageId}")
    public String replyReadMessage(@PathVariable Long userId,
                                   @PathVariable Long messageId,
                                   @ModelAttribute MessageDto messageDto,
                                   Model model) {

        model.addAttribute("readers", userService.listUsers());
        model.addAttribute("message", userService.replyMessage(userId, messageDto));
        return "/user/newMessages";
    }

    @GetMapping("/{userId}/sendMessages/{messageId}")
    public String viewSendMessage(@PathVariable Long userId, @PathVariable Long messageId, Model model) {

        model.addAttribute("message", userService.getSendMessageById(messageId, userId));
        return "/user/viewSendMessage";
    }

    @GetMapping("/notice")
    public String getAllNotices(Model model) {

        return "user/notices";
    }

    @GetMapping("/{userId}/readNotices")
    public String getReadNotices(@PathVariable Long userId, Model model) {
        model.addAttribute("notices", userService.listNotices());
        return "user/readNotices";
    }

    @GetMapping("/{userId}/newNotice")
    public String newNotice(@PathVariable Long userId, Model model) {

        model.addAttribute("notice", userService.initNewNotice(userId));
        return "user/newNotice";
    }

    @PostMapping("/{userId}/newNotice")
    public String processNewNotice(@PathVariable Long userId,
                                   @Valid @ModelAttribute NoticeDto notice, BindingResult result) {
        if (result.hasErrors()){
            //TODO
            return "/";
        } else {
            userService.addNotice(notice);
            return "redirect:/user/" + userId + "/readNotices";
        }
    }

    @GetMapping("/{userId}/editNotice/{noticeId}")
    public String editNotice(@PathVariable Long userId,
                             @PathVariable Long noticeId,
                             Model model) {

        model.addAttribute("notice", userService.getNoticeById(userId,noticeId));
        return "user/editNotice";
    }

    @PostMapping("/{userId}/updateNotice/{noticeId}")
    public String updatePatchNotice(@PathVariable Long userId,
                                    @PathVariable Long noticeId,
                                    @Valid @ModelAttribute NoticeDto notice, BindingResult result) {
        if (result.hasErrors()){
            //TODO
            return "/";
        } else {
            userService.updatePatchNotice(notice, noticeId);
            return "redirect:/user/" + userId + "/readNotices";
        }
    }

    @PostMapping("/{userId}/deleteNotice/{noticeId}")
    public String deleteNotice(@PathVariable Long userId,
                               @PathVariable Long noticeId) {

            userService.deleteNotice(userId, noticeId);
            return "redirect:/user/" + userId + "/readNotices";
    }






}
