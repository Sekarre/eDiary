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


    @PostMapping("/classes/{classId}/removeTeacher/{teacherId}")
    public String removeFormTutorFromClass(@PathVariable Long teacherId,
                                           @PathVariable Long classId,
                                           Model model) {
        model.addAttribute("students", deputyHeadService.listAllStudentsWithoutClass());
        model.addAttribute("teachers", deputyHeadService.listAllTeachersWithoutClass());
        model.addAttribute("schoolClass", deputyHeadService.removeFormTutorFromClass(classId, teacherId));

        return "deputyHead/oneClass";
    }

    @PostMapping("/classes/{classId}/removeStudent/{studentId}")
    public String removeStudentFromClass(@PathVariable Long studentId,
                                         @PathVariable Long classId,
                                         Model model) {

        model.addAttribute("students", deputyHeadService.listAllStudentsWithoutClass());
        model.addAttribute("teachers", deputyHeadService.listAllTeachersWithoutClass());
        model.addAttribute("schoolClass", deputyHeadService.removeStudentFromClass(classId, studentId));

        return "deputyHead/oneClass";
    }

    @PostMapping("/classes/{classId}/addTeacher/{teacherId}")
    public String addFormTutorToClass(@PathVariable Long teacherId,
                                      @PathVariable Long classId,
                                      Model model) {

        model.addAttribute("students", deputyHeadService.listAllStudentsWithoutClass());
        model.addAttribute("teachers", deputyHeadService.listAllTeachersWithoutClass());
        model.addAttribute("schoolClass", deputyHeadService.addFormTutorToClass(classId, teacherId));

        return "deputyHead/oneClass";
    }

    @PostMapping("/classes/{classId}/addStudent/{studentId}")
    public String addStudentToClass(@PathVariable Long studentId,
                                      @PathVariable Long classId,
                                      Model model) {

        model.addAttribute("students", deputyHeadService.listAllStudentsWithoutClass());
        model.addAttribute("teachers", deputyHeadService.listAllTeachersWithoutClass());
        model.addAttribute("schoolClass", deputyHeadService.addStudentToClass(classId, studentId));

        return "deputyHead/oneClass";
    }

    /**
     * Dodawanie wielu uczniow takie samo jak metoda processNewClass, wiec chyba nie bede pisal
     **/

}
