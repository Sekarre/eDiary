package com.ediary.web.controllers;

import com.ediary.DTO.BehaviorDto;
import com.ediary.DTO.EventDto;
import com.ediary.DTO.GradeDto;
import com.ediary.services.TeacherService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.method.P;
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
        return "/teacher/allEvents";
    }

    @GetMapping("/{teacherId}/event/{eventId}")
    public String getEvent(@PathVariable Long teacherId, @PathVariable Long eventId, Model model) {

        model.addAttribute("event", teacherService.getEvent(eventId));
        return "/teacher/event";
    }

    @GetMapping("/{teacherId}/event/new")
    public String newEvent(@PathVariable Long teacherId, Model model) {

        model.addAttribute("event", teacherService.initNewEvent(teacherId));
        return "/teacher/newEvent";
    }

    @PostMapping("/{teacherId}/event/new")
    public String processNewEvent(@Valid @RequestBody EventDto eventDto, BindingResult result) {
        if (result.hasErrors()){
            //TODO
            return "/";
        } else {
            teacherService.saveEvent(eventDto);
            return "redirect:/teacher/event";
        }
    }

    @DeleteMapping("/{teacherId}/event/{eventId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public String deleteEvent(@PathVariable Long teacherId, @PathVariable Long eventId) {

        teacherService.deleteEvent(teacherId, eventId);
        return "/" + teacherId + "/event";
    }

    @PutMapping("/{teacherId}/event/update")
    public String updatePutEvent(@PathVariable Long teacherId,
                                 @Valid @RequestBody EventDto eventDto, BindingResult result) {
        if (result.hasErrors()){
            //TODO
            return "/";
        } else {
            EventDto event = teacherService.updatePutEvent(eventDto);
            return "/" + teacherId + "/event/" + event.getId();
        }
    }

    @PatchMapping("/{teacherId}/event/update")
    public String updatePatchEvent(@PathVariable Long teacherId,
                                   @Valid @RequestBody EventDto eventDto, BindingResult result) {
        if (result.hasErrors()){
            //TODO
            return "/";
        } else {
            EventDto event = teacherService.updatePatchEvent(eventDto);
            return "/" + teacherId + "/event/" + event.getId();
        }
    }

    @GetMapping("/{teacherId}/classes")
    public String getAllClasses(@PathVariable Long teacherId, Model model) {

        model.addAttribute("classes", teacherService.listAllClasses());

        return "/teacher/allClasses";
    }


    @GetMapping("/{teacherId}/behavior")
    public String getAllBehaviorsByTeacher(@PathVariable Long teacherId, Model model) {

        model.addAttribute("behaviors", teacherService.listBehaviors(teacherId));
        return "/teacher/behavior";
    }

    @GetMapping("/{teacherId}/behavior/new")
    public String newBehavior(@PathVariable Long teacherId, Model model) {

        model.addAttribute("behavior", teacherService.initNewBehavior(teacherId));
        return "/teacher/newBehavior";
    }

    @PostMapping("/{teacherId}/behavior/new")
    public String processNewBehavior(@Valid @RequestBody BehaviorDto behaviorDto, BindingResult result) {
        if (result.hasErrors()){
            //TODO
            return "/";
        } else {
            teacherService.saveBehavior(behaviorDto);
            return "redirect:/teacher/behavior";
        }
    }

    @DeleteMapping("/{teacherId}/behavior/{behaviorId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public String deleteBehavior(@PathVariable Long teacherId, @PathVariable Long behaviorId) {

        teacherService.deleteBehavior(teacherId, behaviorId);
        return "/" + teacherId + "/behavior";
    }

    @PutMapping("/{teacherId}/behavior/update")
    public String updatePutBehavior(@PathVariable Long teacherId,
                                    @Valid @RequestBody BehaviorDto behaviorDto, BindingResult result) {
        if (result.hasErrors()){
            //TODO
            return "/";
        } else {
            BehaviorDto behavior = teacherService.updatePutBehavior(behaviorDto);
            //TODO zalezy od widoku
            return "/" + teacherId + "/behavior/" + behavior.getId();
        }
    }

    @PatchMapping("/{teacherId}/behavior/update")
    public String updatePatchBehavior(@PathVariable Long teacherId,
                                   @Valid @RequestBody BehaviorDto behaviorDto, BindingResult result) {
        if (result.hasErrors()){
            //TODO
            return "/";
        } else {
            BehaviorDto behavior = teacherService.updatePatchBehavior(behaviorDto);
            //TODO zalezy od widoku
            return "/" + teacherId + "/behavior/" + behavior.getId();
        }
    }



    @GetMapping("/{teacherId}/grade/subject")
    public String getAllSubjectsGrade(@PathVariable Long teacherId, Model model) {

        model.addAttribute("subjects", teacherService.listSubjects(teacherId));

        return "teacher/grade/subject";
    }


    @GetMapping("/{teacherId}/grade/subject/{subjectId}")
    public String getAllGradesBySubject(@PathVariable Long teacherId, @PathVariable Long subjectId, Model model) {

        model.addAttribute("grades", teacherService.listGradesBySubject(teacherId, subjectId));

        return "/teacher/grade/allGrades";
    }

    //todo: url mapping
    @GetMapping("/{teacherId}/grade/subject/{subjectId}/new")
    public String newGrade(@PathVariable Long teacherId, @PathVariable Long subjectId, Model model) {

        model.addAttribute("grade", teacherService.initNewGrade(teacherId, subjectId));
        return "/teacher/grade/newGrade";
    }

    @PostMapping("/{teacherId}/grade/subject/{subjectId}/new")
    public String processNewGrade(@PathVariable Long teacherId,
                                  @PathVariable Long subjectId,
                                  @Valid @RequestBody GradeDto gradeDto, BindingResult result) {
        if (result.hasErrors()){
            //todo: add view path
            return "/";
        } else {
            teacherService.saveGrade(gradeDto);
            return "redirect:/teacher/" + teacherId + "/grade/subject/" + subjectId;
        }
    }

    @DeleteMapping("/{teacherId}/grade/subject/{subjectId}/{gradeId}")
    public String deleteGrade(@PathVariable Long teacherId,
                              @PathVariable Long subjectId, @PathVariable Long gradeId) {

        teacherService.deleteGrade(teacherId, subjectId, gradeId);
        return "redirect:/teacher/" + teacherId + "/grade/subject/" + subjectId;
    }


    @PutMapping("/{teacherId}/grade/subject/{subjectId}/update")
    public String updatePutGrade(@PathVariable Long teacherId,
                                 @PathVariable Long subjectId,
                                 @Valid @RequestBody GradeDto gradeDto,
                                 BindingResult result) {
        if (result.hasErrors()) {
            //todo: add view path
            return "";
        } else {
            teacherService.updatePutGrade(gradeDto);
            return "redirect:/teacher/" + teacherId +"/grade/subject/" + subjectId;
        }
    }

    @PatchMapping("/{teacherId}/grade/subject/{subjectId}/update")
    public String updatePatchGrade(@PathVariable Long teacherId,
                                   @PathVariable Long subjectId,
                                   @Valid @RequestBody GradeDto gradeDto,
                                   BindingResult result) {
        if (result.hasErrors()){
            //todo: add view path
            return "";
        } else {
            teacherService.updatePatchGrade(gradeDto);
            return "redirect:/teacher/" + teacherId +"/grade/subject/" + subjectId;
        }
    }


    @GetMapping("/{teacherId}/lesson/subject")
    public String getAllSubjects(@PathVariable Long teacherId, Model model) {

        model.addAttribute("subjects", teacherService.listSubjects(teacherId));

        return "/teacher/lesson/subject";
    }


    @GetMapping("/{teacherId}/lesson/subject/{subjectId}")
    public String getAllLessonsBySubject(@PathVariable Long teacherId, @PathVariable Long subjectId,  Model model) {

        model.addAttribute("lessons", teacherService.listLessons(teacherId, subjectId));

        return "/teacher/lesson/subject/allLessons";
    }

}
