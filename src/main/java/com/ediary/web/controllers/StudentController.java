package com.ediary.web.controllers;
import org.springframework.ui.Model;
import com.ediary.services.StudentService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/students")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping("/{studentId}")
    public String getAllGrades(@PathVariable Long studentId, Model model){

        model.addAttribute("grades", studentService.listGrades(studentId));
        return "student/allGrades";
    }
}
