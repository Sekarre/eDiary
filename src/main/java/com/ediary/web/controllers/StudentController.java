package com.ediary.web.controllers;
import com.ediary.converters.UserToUserDto;
import com.ediary.domain.security.User;
import com.ediary.services.SubjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import com.ediary.services.StudentService;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.beans.PropertyEditorSupport;
import java.time.LocalDate;


@RequiredArgsConstructor
@Controller
@RequestMapping("/student")
public class StudentController {

    private final StudentService studentService;
    private final SubjectService subjectService;

    private final UserToUserDto userToUserDto;


    @ModelAttribute
    public void addAuthenticatedUserAndStudent(@AuthenticationPrincipal User user, Model model) {
        model.addAttribute("user", userToUserDto.convert(user));
        model.addAttribute("student", studentService.findByUser(user));
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

    @GetMapping("/{studentId}/grade")
    public String getAllGrades(@PathVariable Long studentId, Model model) {

        model.addAttribute("grades", studentService.listGrades(studentId));
        return "student/allGrades";
    }

    @GetMapping("/{studentId}/grade/{gradeId}")
    public String getGrade(){
        //TODO
        return null;
    }

    @GetMapping("/{studentId}/grade/subject/{subjectId}")
    public String getAllGradesBySubject(@PathVariable Long studentId,
                                        @PathVariable Long subjectId, Model model) {

        model.addAttribute("subjectName", subjectService.getNameById(subjectId));
        model.addAttribute("grades", studentService.listGrades(studentId, subjectId));
        return "student/allGradesBySubject";
    }

    @GetMapping("/{studentId}/attendance")
    public String getAllAttendances(@PathVariable Long studentId, Model model) {

        model.addAttribute("attendances", studentService.listAttendances(studentId));
        return "student/allAttendances";
    }

    @GetMapping("/{studentId}/behavior")
    public String getAllBehaviors(@PathVariable Long studentId, Model model) {

        model.addAttribute("behaviors", studentService.listBehaviors(studentId));
        return "student/allBehaviors";
    }

    @GetMapping("/{studentId}/event")
    public String getAllEvents(@PathVariable Long studentId, Model model) {

        model.addAttribute("events", studentService.listEvents(studentId));
        return "student/allEvents";
    }

    @GetMapping("/{studentId}/timetable")
    public String getTimetable(@PathVariable Long studentId, Model model){

        model.addAttribute("timetable", studentService.getTimetableByStudentId(studentId));
        return "student/timetable";
    }
}
