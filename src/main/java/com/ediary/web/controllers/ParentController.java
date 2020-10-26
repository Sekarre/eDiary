package com.ediary.web.controllers;

import com.ediary.DTO.GradeDto;
import com.ediary.DTO.StudentDto;
import com.ediary.DTO.SubjectDto;
import com.ediary.converters.UserToUserDto;
import com.ediary.domain.Student;
import com.ediary.domain.security.User;
import com.ediary.services.ParentService;
import com.ediary.services.StudentService;
import com.ediary.services.SubjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.beans.PropertyEditorSupport;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
@RequiredArgsConstructor
@RequestMapping("/parent/{parentId}/")
public class ParentController {

    private final ParentService parentService;
    private final StudentService studentService;
    private final SubjectService subjectService;

    private final UserToUserDto userToUserDto;

    @ModelAttribute
    public void addAuthenticatedUserAndParent(@AuthenticationPrincipal User user, Model model) {
        model.addAttribute("user", userToUserDto.convert(user));
        model.addAttribute("parent", parentService.findByUser(user));
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

    @GetMapping("/students")
    public String getAllStudents(@PathVariable Long parentId, Model model) {

        model.addAttribute("students", parentService.listStudents(parentId));

        return "parent/allStudents";
    }

    @GetMapping("/{studentId}/grade")
    public String getAllGrades(@PathVariable Long studentId, @PathVariable Long parentId, Model model) {

        List<GradeDto> studentList = studentService.listGrades(studentId);
        Set<String> subjectDtoSet = new HashSet<>();
        studentList.forEach(grade -> subjectDtoSet.add(grade.getSubjectName()));
        model.addAttribute("subjects", subjectDtoSet);

        model.addAttribute("grades", studentService.listGrades(studentId));
        return "parent/allGrades";
    }

    @GetMapping("/{studentId}/grade/{gradeId}")
    public String getGrade(){
        return null;
    }

    @GetMapping("/{studentId}/grade/subject/{subjectId}")
    public String getAllGradesBySubject(@PathVariable Long studentId,
                                        @PathVariable Long subjectId, Model model) {

        model.addAttribute("subjectName", subjectService.getNameById(subjectId));
        model.addAttribute("grades", studentService.listGrades(studentId, subjectId));
        return "parent/allGradesBySubject";
    }

    @GetMapping("/{studentId}/attendance")
    public String getAllAttendances(@PathVariable Long studentId, @PathVariable String parentId, Model model) {

        model.addAttribute("attendances", studentService.listAttendances(studentId));
        return "parent/allAttendances";
    }

    @GetMapping("/{studentId}/behavior")
    public String getAllBehaviors(@PathVariable Long studentId, @PathVariable String parentId, Model model) {

        model.addAttribute("behaviors", studentService.listBehaviors(studentId));
        return "parent/allBehaviors";
    }

    @GetMapping("/{studentId}/event")
    public String getAllEvents(@PathVariable Long studentId, @PathVariable String parentId, Model model) {

        model.addAttribute("events", studentService.listEvents(studentId));
        return "parent/allEvents";
    }

    @GetMapping("/{studentId}/timetable")
    public String getTimetable(@PathVariable Long studentId, @PathVariable String parentId, Model model){

        model.addAttribute("timetable", studentService.getTimetableByStudentId(studentId));
        return "parent/timetable";
    }


    @GetMapping("/{studentId}/attendance/save")
    public String saveAttendance(Model model) {
        //todo: Zalezy od widoku
        return "";
    }

}
