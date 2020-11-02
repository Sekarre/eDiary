package com.ediary.web.controllers;


import com.ediary.DTO.ClassDto;
import com.ediary.converters.UserToUserDto;
import com.ediary.domain.security.User;
import com.ediary.services.DeputyHeadService;
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
import java.util.List;

@RequiredArgsConstructor
@Controller
@RequestMapping("/deputyHead")
public class DeputyHeadController {

    private final DeputyHeadService deputyHeadService;
    private final UserToUserDto userToUserDto;

    @ModelAttribute
    public void addAuthenticatedUserAndParent(@AuthenticationPrincipal User user, Model model) {
        model.addAttribute("user", userToUserDto.convert(user));
    }

    @InitBinder
    public void dataBinder(WebDataBinder dataBinder) {
        dataBinder.setDisallowedFields("id");

        dataBinder.registerCustomEditor(LocalDate.class, new PropertyEditorSupport() {
            @Override
            public void setAsText(String text) throws IllegalArgumentException {
                setValue(LocalDate.parse(text));
            }
        });
    }


    @GetMapping("/newClass")
    public String newClass(Model model) {

        model.addAttribute("schoolClass", deputyHeadService.initNewClass());
        model.addAttribute("students", deputyHeadService.listAllStudentsWithoutClass());
        model.addAttribute("teachers", deputyHeadService.listAllTeachersWithoutClass());

        return "deputyHead/newClass";
    }

    @PostMapping("/newClass")
    public String processNewClass(@Valid @ModelAttribute ClassDto classDto, BindingResult result,
                                  @RequestParam(name = "studentsId") List<Long> studentsId) {

        if (result.hasErrors()) {
            //todo: path view
            return "";
        }

        deputyHeadService.saveClass(classDto, studentsId);

        return "deputyHead/classes";
    }

}
