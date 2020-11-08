package com.ediary.web.controllers;

import com.ediary.converters.UserToUserDto;
import com.ediary.domain.security.User;
import com.ediary.services.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

@RequiredArgsConstructor
@Controller
public class AdminController {

    private final AdminService adminService;

    private final UserToUserDto userToUserDto;

    @ModelAttribute
    public void addAuthenticatedUserAndParent(@AuthenticationPrincipal User user, Model model) {
        model.addAttribute("user", userToUserDto.convert(user));
    }


    @GetMapping("/home")
    public String home() {

        return "admin/index";
    }

    @GetMapping("/admin/accounts")
    public String getAllAccounts(Model model) {
        model.addAttribute("accounts", adminService.getAllAccounts());

        return "admin/accounts";
    }



}
