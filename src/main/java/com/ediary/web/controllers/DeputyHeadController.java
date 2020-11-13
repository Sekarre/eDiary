package com.ediary.web.controllers;


import com.ediary.DTO.ClassDto;
import com.ediary.converters.UserToUserDto;
import com.ediary.domain.Class;
import com.ediary.domain.security.User;
import com.ediary.services.DeputyHeadService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import javax.validation.Valid;
import java.beans.PropertyEditorSupport;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Controller
@RequestMapping("/deputyHead")
public class DeputyHeadController {

    private final DeputyHeadService deputyHeadService;
    private final UserToUserDto userToUserDto;
    private final Integer pageSize = 15;

    @ModelAttribute
    public void addAuthenticatedUser(@AuthenticationPrincipal User user, Model model) {
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

    @GetMapping("/home")
    public String home() {

        return "deputyHead/index";
    }


    @GetMapping("/newClass")
    public String newClass(Model model) {

        model.addAttribute("schoolClass", deputyHeadService.initNewClass());
        //just for now, random values
        model.addAttribute("students", deputyHeadService.listAllStudentsWithoutClass(1,2));
        model.addAttribute("teachers", deputyHeadService.listAllTeachersWithoutClass(1,2));

        return "deputyHead/newClass";
    }

    @PostMapping("/newClass")
    public String processNewClass(@Valid @ModelAttribute ClassDto classDto, BindingResult result,
                                  @RequestParam(name = "studentsId") List<Long> studentsId) {

        if (result.hasErrors()) {
            //todo: path view
            return "";
        }

        Class schoolClass = deputyHeadService.saveClass(classDto, studentsId);

        return "redirect:/deputyHead/classes/" + schoolClass.getId();
    }


    @GetMapping("/classes")
    public String getClasses(Model model) {

        model.addAttribute("classes", deputyHeadService.listAllClasses());

        return "deputyHead/classes";
    }

    @GetMapping("/classes/{classId}")
    public String getOneClass(@PathVariable Long classId, Model model) {

        model.addAttribute("schoolClass", deputyHeadService.getSchoolClass(classId));

        return "deputyHead/oneClass";
    }


    @DeleteMapping("/classes/{classId}")
    public String deleteClass(@PathVariable Long classId) {

        deputyHeadService.deleteClass(classId);

        return "redirect:/deputyHead/classes";
    }

    @GetMapping("/classes/{classId}/changeTeacher/{teacherId}")
    public String editTeacher(@PathVariable Long classId,
                              @PathVariable Long teacherId,
                              @RequestParam(name = "page", required = false) Optional<Integer> page,
                              Model model) {

        model.addAttribute("page", page);
        model.addAttribute("currentTeacher", deputyHeadService.findTeacher(teacherId, classId));
        model.addAttribute("teachers", deputyHeadService.listAllTeachersWithoutClass(page.orElse(0), pageSize));
        model.addAttribute("schoolClass", deputyHeadService.getSchoolClass(classId));


        return "deputyHead/editFormTutor";
    }


    @PostMapping("/classes/{classId}/removeStudent/{studentId}")
    public String removeStudentFromClass(@PathVariable Long studentId,
                                         @PathVariable Long classId,
                                         @RequestParam(name = "page", required = false) Optional<Integer> page,
                                         @RequestParam(name = "addStudentsView", required = false) Boolean addStudentsView) {

        ClassDto schoolClass = deputyHeadService.removeStudentFromClass(classId, studentId);

        if (addStudentsView != null && addStudentsView) {
            return "redirect:/deputyHead/classes/" + schoolClass.getId() + "/addStudents?page=" + page.orElse(0);
        }

        return "redirect:/deputyHead/classes/" + schoolClass.getId();
    }

    @GetMapping("/classes/{classId}/addStudents")
    public String addStudents(@PathVariable Long classId,
                              @RequestParam(name = "page", required = false) Optional<Integer> page,
                              Model model) {


        model.addAttribute("page", page);
        model.addAttribute("studentsCount", deputyHeadService.countStudentsWithoutClass());
        model.addAttribute("students", deputyHeadService.listAllStudentsWithoutClass(page.orElse(0), pageSize));
        model.addAttribute("schoolClass", deputyHeadService.getSchoolClass(classId));

        return "deputyHead/addStudents";
    }

    @PostMapping("/classes/{classId}/addTeacher/{teacherId}")
    public String addFormTutorToClass(@PathVariable Long teacherId,
                                      @PathVariable Long classId) {

        ClassDto schoolClass = deputyHeadService.addFormTutorToClass(classId, teacherId);

        return "redirect:/deputyHead/classes/" + schoolClass.getId();
    }

    @PostMapping("/classes/{classId}/addStudent/{studentId}")
    public String addStudentToClass(@PathVariable Long studentId,
                                    @PathVariable Long classId,
                                    @RequestParam(name = "page", required = false) Optional<Integer> page) {

        ClassDto schoolClass = deputyHeadService.addStudentToClass(classId, studentId);


        return "redirect:/deputyHead/classes/" + schoolClass.getId() + "/addStudents?page=" + page.orElse(0);
    }


}
