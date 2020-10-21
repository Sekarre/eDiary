package com.ediary.web.controllers;

import com.ediary.DTO.EventDto;
import com.ediary.services.TeacherService;
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
@RequestMapping("/teacher")
public class TeacherController {

    private final TeacherService teacherService;

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

    @GetMapping("/{teacherId}/event")
    public String getAllEvents(@PathVariable Long teacherId, Model model) {

        model.addAttribute("events", teacherService.listEvents(teacherId));
        return "teacher/allEvents";
    }

    @GetMapping("/{teacherId}/event/new")
    public String newEvent(@PathVariable Long teacherId, Model model) {

        model.addAttribute("event", teacherService.initNewEvent(teacherId));
        return "teacher/newEvent";
    }

    @PostMapping("/{teacherId}/event/new")
    public String processNewEvent(@Valid @ModelAttribute EventDto eventDto, BindingResult result) {
        if (result.hasErrors()){
            //TODO
            return "/";
        } else {
            teacherService.saveEvent(eventDto);
            return "redirect:teacher/newEvent";
        }
    }

    @GetMapping("/{teacherId}/classes")
    public String getAllClasses(@PathVariable Long teacherId, Model model) {

        model.addAttribute("classes", teacherService.listAllClasses());

        return "teacher/allClasses";
    }
}
