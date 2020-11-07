package com.ediary.web.controllers;

import com.ediary.converters.UserToUserDto;
import com.ediary.domain.security.User;
import com.ediary.services.HeadmasterService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.beans.PropertyEditorSupport;
import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;

@RequiredArgsConstructor
@Controller
@RequestMapping("/headmaster")
public class HeadmasterController {

    private final HeadmasterService headmasterService;
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
                if (text.length() < 10) {
                    text = "01/" + text;
                }

                DateTimeFormatter f = new DateTimeFormatterBuilder().parseCaseInsensitive()
                        .append(DateTimeFormatter.ofPattern("dd/MM/yyyy")).toFormatter();
                setValue(LocalDate.parse(text, f));
            }


        });
    }


    @GetMapping("/teacherReport/{page}")
    public String getTeacherReport(Model model, @PathVariable Integer page) {

        model.addAttribute("teachers", headmasterService.listAllTeachers(page, 20));
        model.addAttribute("currentPage", page);
        model.addAttribute("timeInterval", headmasterService.initNewTimeInterval());

        return "headmaster/teacherReport";
    }

    @PostMapping("/teacherReport/{page}")
    public String processNewTimeInterval(Model model,
                                         @PathVariable Integer page,
                                         @RequestParam(name = "startTime") @DateTimeFormat(pattern = "MM/yyyy") LocalDate startTime,
                                         @RequestParam(name = "endTime") @DateTimeFormat(pattern = "MM/yyyy") LocalDate endTime) {

        model.addAttribute("teachers", headmasterService.listAllTeachers(page, 20));
        model.addAttribute("currentPage", page);
        model.addAttribute("timeInterval", headmasterService.setTimeInterval(startTime, endTime));

        return "headmaster/teacherReport";
    }

    //todo: tests
    @RequestMapping("/teacherReport/{teacherId}/download/{startTime}/{endTime}")
    public void downloadStudentCardPdf(HttpServletResponse response,
                                       @PathVariable Date startTime,
                                       @PathVariable Date endTime,
                                       @RequestHeader String referer,
                                       @PathVariable Long teacherId) throws Exception {

        //Not allowing to download via url typing
        if (referer != null && !referer.isEmpty()) {
        }

        headmasterService.createTeacherReport(response, teacherId, startTime, endTime);

    }


}
