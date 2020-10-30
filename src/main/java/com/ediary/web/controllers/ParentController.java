package com.ediary.web.controllers;

import com.ediary.DTO.GradeDto;
import com.ediary.DTO.StudentDto;
import com.ediary.DTO.SubjectDto;
import com.ediary.converters.UserToUserDto;
import com.ediary.domain.Student;
import com.ediary.domain.helpers.WeeklyAttendances;
import com.ediary.domain.security.User;
import com.ediary.services.ParentService;
import com.ediary.services.StudentService;
import com.ediary.services.SubjectService;
import com.ediary.services.WeeklyAttendancesService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.beans.PropertyEditorSupport;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
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
    private final WeeklyAttendancesService weeklyAttendancesService;

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

    @GetMapping("/{studentId}")
    public String getStudentIndex(@PathVariable Long parentId, @PathVariable Long studentId, Model model) {

        model.addAttribute("students", parentService.findStudent(parentId, studentId));

        return "parent/index";
    }

    @GetMapping("/{studentId}/grade")
    public String getAllGrades(@PathVariable Long studentId, @PathVariable Long parentId, Model model) {

        model.addAttribute("subjects", parentService.getAllStudentSubjectNames(parentId, studentId));

        model.addAttribute("grades", studentService.listGrades(studentId));

        model.addAttribute("student", parentService.findStudent(parentId, studentId));
        return "parent/allGrades";
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

        model.addAttribute("weeklyAttendances",
                weeklyAttendancesService.getAttendancesByWeek(studentId, 7, Date.valueOf(LocalDate.now().minusDays(6))));
        model.addAttribute("attendances", studentService.listAttendances(studentId));
        model.addAttribute("studentId", studentId);
        return "parent/allAttendances";
    }


    @GetMapping("/{studentId}/attendance/{direction}/{dateValue}")
    public String getAllAttendancesWithDate(@PathVariable Long studentId,
                                            @PathVariable Long parentId,
                                            @PathVariable String direction,
                                            @PathVariable String dateValue,
                                            Model model) {
        Date date;
        if (direction.equals("next")){
            date = Date.valueOf(LocalDate.parse(dateValue).plusDays(7));
        } else {
            date = Date.valueOf(LocalDate.parse(dateValue).minusDays(7));
        }

        model.addAttribute("weeklyAttendances",
                weeklyAttendancesService.getAttendancesByWeek(studentId, 7, date));
        model.addAttribute("attendances", studentService.listAttendances(studentId));
        model.addAttribute("studentId", studentId);
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


    @PostMapping("/{studentId}/attendance/save")
    public String saveAttendance(Model model) {
        //todo: Zalezy od widoku
        return "";
    }

}
