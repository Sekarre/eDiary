package com.ediary.web.controllers;
import com.ediary.services.SubjectService;
import org.springframework.ui.Model;
import com.ediary.services.StudentService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/students")
public class StudentController {

    private final StudentService studentService;
    private final SubjectService subjectService;

    public StudentController(StudentService studentService, SubjectService subjectService) {
        this.studentService = studentService;
        this.subjectService = subjectService;
    }


    @GetMapping("/{studentId}")
    public String getAllGrades(@PathVariable Long studentId, Model model) {

        model.addAttribute("grades", studentService.listGrades(studentId));
        return "student/allGrades";
    }

    @GetMapping("/{studentId}/{subjectId}")
    public String getAllGradesBySubject(@PathVariable Long studentId,
                                        @PathVariable Long subjectId, Model model) {

        model.addAttribute("subjectName", subjectService.getNameById(subjectId));
        model.addAttribute("grades", studentService.listGrades(studentId, subjectId));
        return "student/allGradesBySubject";
    }
}
