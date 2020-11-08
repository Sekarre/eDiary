package com.ediary.web.controllers;

import com.ediary.DTO.UserDto;
import com.ediary.converters.UserToUserDto;
import com.ediary.domain.security.User;
import com.ediary.services.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RequiredArgsConstructor
@Controller
@RequestMapping("/admin")
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

    @GetMapping("/users")
    public String getAllUsers(Model model) {
        model.addAttribute("users", adminService.getAllUsers());

        return "admin/allUsers";
    }

    @GetMapping("/users/{userId}")
    public String getUser(@PathVariable Long userId, Model model) {

        model.addAttribute("user", adminService.getUser(userId));

        return "admin/user";
    }

    @GetMapping("/newUser")
    public String initNewUser(Model model) {

        model.addAttribute("newUser", adminService.initNewUser());

        return "admin/newUser";
    }


    @PostMapping("/newUser")
    public String processNewUser(@Valid @ModelAttribute UserDto userDto,
                                 BindingResult result) {

        if (result.hasErrors()) {
            //todo: path
            return "";
        }

        User user = adminService.saveUser(userDto);

        //todo: change to redirect to single user
        return "admin/users";
    }

    @DeleteMapping("/deleteUser/{userId}")
    public String deleteUser(@PathVariable Long userId) {

        adminService.deleteUser(userId);

        return "admin/users";
    }



}
