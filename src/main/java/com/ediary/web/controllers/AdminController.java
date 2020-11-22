package com.ediary.web.controllers;

import com.ediary.DTO.SchoolDto;
import com.ediary.DTO.UserDto;
import com.ediary.converters.UserToUserDto;
import com.ediary.domain.security.User;
import com.ediary.security.perms.AdminPermission;
import com.ediary.services.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

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


    @AdminPermission
    @GetMapping("/home")
    public String home() {
        return "admin/index";
    }


    @AdminPermission
    @GetMapping("/users")
    public String getAllUsers(Model model) {
        model.addAttribute("users", adminService.getAllUsers());

        return "admin/allUsers";
    }

    @AdminPermission
    @GetMapping("/newUser")
    public String initNewUser(Model model) {

        model.addAttribute("newUser", adminService.initNewUser());
        model.addAttribute("roles", adminService.getAllRoles());

        return "admin/newUser";
    }

    @AdminPermission
    @PostMapping("/newUser")
    public String processNewUser(@Valid @ModelAttribute UserDto userDto,
                                 @RequestParam(name = "selectedRoles") List<Long> rolesId,
                                 BindingResult result) {

        if (result.hasErrors()) {
            //todo: path
            return "";
        }

        User user = adminService.saveUser(userDto, rolesId);

        return "redirect:/admin/users";
    }

    @AdminPermission
    @GetMapping("/users/{userId}")
    public String getUser(@PathVariable Long userId, Model model) {

        model.addAttribute("user", adminService.getUser(userId));

        return "admin/user";
    }

    @AdminPermission
    @DeleteMapping("/users/{userId}/delete")
    public String deleteUser(@PathVariable Long userId) {

        adminService.deleteUser(userId);

        return "admin/users";
    }

    @AdminPermission
    @GetMapping("/users/{userId}/edit")
    public String editUser(@PathVariable Long userId, Model model) {

        model.addAttribute("user", adminService.getUser(userId));

        return "admin/editUser";
    }

    @AdminPermission
    @PostMapping("/users/{userId}/update")
    public String updateUser(@PathVariable Long userId,
                             @RequestParam(name = "roleId") List<Long> rolesId,
                             @Valid @ModelAttribute UserDto userDto,
                             BindingResult result) {

        if (result.hasErrors()) {
            //todo: view path
            return "";
        }

        UserDto updatedUser = adminService.updateUser(userDto, rolesId);

        return "redirect:/admin/users/" + updatedUser.getId();
    }

    @AdminPermission
    @GetMapping("/school")
    public String getSchoolInfo(Model model) {

        model.addAttribute("school", adminService.getSchool());

        return "admin/school";
    }

    @AdminPermission
    @GetMapping("/school/edit")
    public String editSchool(Model model) {
        model.addAttribute("school", adminService.getSchool());

        return "admin/editSchool";
    }

    @AdminPermission
    @PostMapping("/school/{schoolId}/{addressId}/update")
    public String updateSchool(@PathVariable Long schoolId,
                               @PathVariable Long addressId,
                               @Valid @ModelAttribute SchoolDto schoolDto,
                               BindingResult result) {

        if (result.hasErrors()) {
            //todo: path
            return "";
        }
        schoolDto.setId(schoolId);
        schoolDto.getAddressDto().setId(addressId);
        adminService.updateSchool(schoolDto);

        return "redirect:/admin/school";
    }

}
