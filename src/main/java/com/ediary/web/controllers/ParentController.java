package com.ediary.web.controllers;

import com.ediary.services.ParentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/parent")
public class ParentController {

    private final ParentService parentService;

    @GetMapping("/{parentId}/students")
    public String getAllStudents(@PathVariable Long parentId, Model model) {

        model.addAttribute("students", parentService.listStudents(parentId));

        return "parent/allStudents";
    }


}
