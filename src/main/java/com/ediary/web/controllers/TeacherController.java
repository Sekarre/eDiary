package com.ediary.web.controllers;

import com.ediary.DTO.*;
import com.ediary.converters.UserToUserDto;
import com.ediary.domain.*;
import com.ediary.domain.security.User;
import com.ediary.services.FormTutorService;
import com.ediary.services.TeacherService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.beans.PropertyEditorSupport;
import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.List;

@RequiredArgsConstructor
@Controller
@RequestMapping("/teacher")
public class TeacherController {

    private final TeacherService teacherService;
    private final FormTutorService formTutorService;

    private final UserToUserDto userToUserDto;

    @ModelAttribute
    public void addAuthenticatedUserAndTeacher(@AuthenticationPrincipal User user, Model model) {
        model.addAttribute("user", userToUserDto.convert(user));
        model.addAttribute("teacher", teacherService.findByUser(user));
    }

    @InitBinder
    public void dataBinder(WebDataBinder dataBinder) {
        dataBinder.setDisallowedFields("id");

        dataBinder.registerCustomEditor(LocalDate.class, new PropertyEditorSupport() {
            @Override
            public void setAsText(String text) throws IllegalArgumentException {
                //correction
                if (text.length() < 10) {
                    text = "01/" + text;
                }

                DateTimeFormatter f = new DateTimeFormatterBuilder().parseCaseInsensitive()
                        .append(DateTimeFormatter.ofPattern("dd/MM/yyyy")).toFormatter();
                setValue(LocalDate.parse(text, f));
            }


        });
    }

    @GetMapping("/home")
    public String home() {

        return "/teacher/index";
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
        if (result.hasErrors()) {
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
        if (result.hasErrors()) {
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
        if (result.hasErrors()) {
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
        if (result.hasErrors()) {
            //TODO
            return "/";
        } else {
            teacherService.saveBehavior(behaviorDto);
            return "redirect:/teacher/behavior";
        }
    }

    @DeleteMapping("/{teacherId}/behavior/{behaviorId}")
    public String deleteBehavior(@PathVariable Long teacherId, @PathVariable Long behaviorId) {

        teacherService.deleteBehavior(teacherId, behaviorId);
        return "redirect:/teacher/" + teacherId + "/behavior";
    }

    @PutMapping("/{teacherId}/behavior/update")
    public String updatePutBehavior(@PathVariable Long teacherId,
                                    @Valid @RequestBody BehaviorDto behaviorDto, BindingResult result) {
        if (result.hasErrors()) {
            //TODO
            return "/";
        } else {
            BehaviorDto behavior = teacherService.updatePutBehavior(behaviorDto);
            //TODO zalezy od widoku
            //return "/" + teacherId + "/behavior/" + behavior.getId();
            return "/teacher/" + teacherId + "/behavior";
        }
    }

    @PatchMapping("/{teacherId}/behavior/update")
    public String updatePatchBehavior(@PathVariable Long teacherId,
                                      @Valid @RequestBody BehaviorDto behaviorDto, BindingResult result) {
        if (result.hasErrors()) {
            //TODO
            return "/";
        } else {
            BehaviorDto behavior = teacherService.updatePatchBehavior(behaviorDto);
            //TODO zalezy od widoku
            //return "/" + teacherId + "/behavior/" + behavior.getId();
            return "redirect:/teacher/" + teacherId + "/behavior";
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

    @GetMapping("/{teacherId}/grade/subject/{subjectId}/new")
    public String newGrade(@PathVariable Long teacherId, @PathVariable Long subjectId, Model model) {

        model.addAttribute("grade", teacherService.initNewGrade(teacherId, subjectId));
        return "/teacher/grade/newGrade";
    }

    @PostMapping("/{teacherId}/grade/subject/{subjectId}/new")
    public String processNewGrade(@PathVariable Long teacherId,
                                  @PathVariable Long subjectId,
                                  @Valid @RequestBody GradeDto gradeDto, BindingResult result) {
        if (result.hasErrors()) {
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
            return "redirect:/teacher/" + teacherId + "/grade/subject/" + subjectId;
        }
    }

    @PatchMapping("/{teacherId}/grade/subject/{subjectId}/update")
    public String updatePatchGrade(@PathVariable Long teacherId,
                                   @PathVariable Long subjectId,
                                   @Valid @RequestBody GradeDto gradeDto,
                                   BindingResult result) {
        if (result.hasErrors()) {
            //todo: add view path
            return "";
        } else {
            teacherService.updatePatchGrade(gradeDto);
            return "redirect:/teacher/" + teacherId + "/grade/subject/" + subjectId;
        }
    }


    @GetMapping("/{teacherId}/lesson/subject")
    public String getAllSubjects(@PathVariable Long teacherId, Model model) {

        model.addAttribute("subjects", teacherService.listSubjects(teacherId));

        return "/teacher/lesson/subject";
    }


    @GetMapping("/{teacherId}/lesson/subject/{subjectId}")
    public String getAllLessonsBySubject(@PathVariable Long teacherId, @PathVariable Long subjectId, Model model) {

        model.addAttribute("lessons", teacherService.listLessons(teacherId, subjectId));

        return "/teacher/lesson/allLessons";
    }


    @GetMapping("{teacherId}/lesson/subject/{subjectId}/new")
    public String newLesson(@PathVariable Long teacherId, @PathVariable Long subjectId, Model model) {
        model.addAttribute("lesson", teacherService.initNewLesson(subjectId));

        return "/teacher/lesson/newLesson";
    }


    @PostMapping("{teacherId}/lesson/subject/{subjectId}/new")
    public String processNewLesson(@PathVariable Long teacherId,
                                   @PathVariable Long subjectId,
                                   @Valid @RequestBody LessonDto lessonDto,
                                   BindingResult result) {

        if (result.hasErrors()) {
            //todo: add view path
            return "";
        } else {
            Lesson savedLesson = teacherService.saveLesson(lessonDto);
            return "redirect:/teacher/" + teacherId + "/lesson/subject/" + subjectId + "/" + savedLesson.getId();
        }
    }

    @GetMapping("{teacherId}/lesson/subject/{subjectId}/class")
    public String getAllClassesBySubject(@PathVariable Long teacherId,
                                         @PathVariable Long subjectId) {

        ClassDto schoolClass = teacherService.listClassesByTeacherAndSubject(teacherId, subjectId).get(0);

        return "redirect:/teacher/" + teacherId + "/lesson/subject/" + subjectId + "/class/" + schoolClass.getId();
    }

    @GetMapping("{teacherId}/lesson/subject/{subjectId}/class/{classId}")
    public String getClassBySubject(@PathVariable Long teacherId,
                                    @PathVariable Long subjectId,
                                    @PathVariable Long classId,
                                    Model model) {
        model.addAttribute("class", teacherService.getSchoolClassByTeacherAndSubject(classId, subjectId, teacherId));

        return "/teacher/lesson/class";
    }

    @GetMapping("{teacherId}/lesson/subject/{subjectId}/class/{classId}/lessons")
    public String getAllLessonsBySubjectAndClass(@PathVariable Long teacherId,
                                                 @PathVariable Long subjectId,
                                                 @PathVariable Long classId,
                                                 Model model) {

        model.addAttribute("lessons", teacherService.listLessons(teacherId, subjectId, classId));

        return "/teacher/lesson/allClassLessons";
    }

    @GetMapping("{teacherId}/lesson/subject/{subjectId}/class/{classId}/lessons/{lessonId}")
    public String getLesson(@PathVariable Long teacherId,
                            @PathVariable Long subjectId,
                            @PathVariable Long classId,
                            @PathVariable Long lessonId,
                            Model model) {

        model.addAttribute("lesson", teacherService.getLesson(lessonId));

        return "/teacher/lesson/singleLesson";
    }

    @GetMapping("{teacherId}/lesson/subject/{subjectId}/class/{classId}/lessons/{lessonId}/attendances")
    public String getLessonAttendances(@PathVariable Long teacherId,
                                       @PathVariable Long subjectId,
                                       @PathVariable Long classId,
                                       @PathVariable Long lessonId,
                                       Model model) {

        model.addAttribute("attendances", teacherService.listAttendances(teacherId, subjectId, classId, lessonId));

        return "/teacher/lesson/attendances";
    }

    //todo: Modyfikuj frekwencje ucznia
    @PostMapping("{teacherId}/lesson/subject/{subjectId}/class/{classId}/lessons/{lessonId}/attendances/new")
    public String newLessonAttendance(@PathVariable Long teacherId,
                                      @PathVariable Long subjectId,
                                      @PathVariable Long classId,
                                      @PathVariable Long lessonId,
                                      @Valid @RequestBody AttendanceDto attendanceDto,
                                      BindingResult result) {

        if (result.hasErrors()) {
            //todo: add path
            return "";
        } else {
            teacherService.saveAttendance(attendanceDto);
            return "";
        }

    }


    @GetMapping("{teacherId}/lesson/subject/{subjectId}/class/{classId}/lessons/{lessonId}/students")
    public String getLessonStudents(@PathVariable Long teacherId,
                                    @PathVariable Long subjectId,
                                    @PathVariable Long classId,
                                    @PathVariable Long lessonId,
                                    Model model) {

        model.addAttribute("students", teacherService.listLessonStudents(teacherId, subjectId, classId, lessonId));
        model.addAttribute("grade", teacherService.initNewGrade(teacherId, subjectId));
        return "/teacher/lesson/students";
    }


    //todo: Grades
    @PostMapping("{teacherId}/lesson/subject/{subjectId}/class/{classId}/lessons/{lessonId}/students/{studentId}")
    public String processNewLessonGrade(@PathVariable Long teacherId,
                                        @PathVariable Long subjectId,
                                        @PathVariable Long classId,
                                        @PathVariable Long lessonId,
                                        @PathVariable Long studentId,
                                        @Valid @ModelAttribute GradeDto gradeDto,
                                        BindingResult result) {

        if (result.hasErrors()) {
            //todo: view path
            return "";
        }

        Grade grade = teacherService.saveGrade(gradeDto);

        //XD?
        return "redirect:/teacher/" + teacherId + "/lesson/subject/" + subjectId + "/class/" + classId + "/lessons/" +
                lessonId + "/students/" + studentId;
    }





    @GetMapping("{teacherId}/lesson/subject/{subjectId}/class/{classId}/newEvent")
    public String newLessonEvent(@PathVariable Long teacherId,
                                 @PathVariable Long subjectId,
                                 @PathVariable Long classId,
                                 Model model) {

        model.addAttribute("event", teacherService.initNewClassEvent(teacherId, classId));

        return "/teacher/lesson/newLessonEvent";
    }

    @PostMapping("{teacherId}/lesson/subject/{subjectId}/class/{classId}/newEvent")
    public String processNewLessonEvent(@PathVariable Long teacherId,
                                        @PathVariable Long subjectId,
                                        @PathVariable Long classId,
                                        @Valid @RequestBody EventDto eventDto,
                                        BindingResult result) {

        if (result.hasErrors()) {
            //todo: add view path
            return "";
        } else {
            Event event = teacherService.saveEvent(eventDto);
            return "redirect:/teacher/" + teacherId + "/lesson/subject/" + subjectId + "/" + event.getId();
        }
    }


    @GetMapping("/{teacherId}/subject")
    public String getSubjects(@PathVariable Long teacherId, Model model) {

        model.addAttribute("subjects", teacherService.listSubjects(teacherId));
        return "/teacher/subject/allSubject";
    }

    @GetMapping("/{teacherId}/subject/{subjectId}")
    public String getSubject(@PathVariable Long teacherId, @PathVariable Long subjectId, Model model) {

        model.addAttribute("subject", teacherService.getSubjectById(teacherId, subjectId));
        model.addAttribute("topics", teacherService.listTopics(teacherId, subjectId));
        return "/teacher/subject/subject";
    }

    @GetMapping("/{teacherId}/subject/new")
    public String newSubject(@PathVariable Long teacherId, Model model) {

        model.addAttribute("subject", teacherService.initNewSubject(teacherId));
        model.addAttribute("schoolClasses", teacherService.listAllClasses());
        return "/teacher/subject/newSubject";
    }

    @PostMapping("/{teacherId}/subject/new")
    public String processNewSubject(@PathVariable Long teacherId,
                                    @Valid @ModelAttribute SubjectDto subject,
                                    BindingResult result) {
        if (result.hasErrors()) {
            //TODO
            return "/";
        } else {
            teacherService.saveOrUpdateSubject(subject);
            return "redirect:/teacher/" + teacherId + "/subject";
        }
    }

    @PostMapping("/{teacherId}/subject/{subjectId}/delete")
    public String deleteSubject(@PathVariable Long teacherId, @PathVariable Long subjectId) {

        teacherService.deleteSubject(teacherId, subjectId);
        return "redirect:/teacher/" + teacherId + "/subject";
    }

    @GetMapping("/{teacherId}/subject/{subjectId}/update")
    public String updateSubject(@PathVariable Long teacherId,
                                @PathVariable Long subjectId, Model model) {

        model.addAttribute("subject", teacherService.getSubjectById(teacherId, subjectId));
        model.addAttribute("schoolClasses", teacherService.listAllClasses());
        return "/teacher/subject/updateSubject";

    }

    @PutMapping("/{teacherId}/subject/update")
    public String updatePutSubject(@PathVariable Long teacherId,
                                   @Valid @RequestBody SubjectDto subjectDto, BindingResult result) {
        if (result.hasErrors()) {
            //TODO
            return "/";
        } else {
            Subject subject = teacherService.saveOrUpdateSubject(subjectDto);
            //TODO zalezy od widoku
            return "redirect:/teacher/" + teacherId + "/subject";
        }
    }

    @PostMapping("/{teacherId}/subject/{subjectId}/update")
    public String updatePatchSubject(@PathVariable Long teacherId,
                                     @PathVariable Long subjectId,
                                     @Valid @ModelAttribute SubjectDto subjectDto,
                                     BindingResult result) {
        if (result.hasErrors()) {
            //TODO
            return "/";
        } else {
            subjectDto.setId(subjectId);
            SubjectDto subject = teacherService.updatePatchSubject(subjectDto);
            //TODO zalezy od widoku
            return "redirect:/teacher/" + teacherId + "/subject";
        }
    }

    @GetMapping("/{teacherId}/subject/{subjectId}/topic")
    public String getTopicsBySubject(@PathVariable Long teacherId,
                                     @PathVariable Long subjectId,
                                     Model model) {
        model.addAttribute("topics", teacherService.listTopics(teacherId, subjectId));
        return "/teacher/subject/topic";
    }

    @GetMapping("/{teacherId}/subject/{subjectId}/topic/new")
    public String newTopic(@PathVariable Long teacherId,
                           @PathVariable Long subjectId,
                           Model model) {

        model.addAttribute("topic", teacherService.initNewTopic(teacherId, subjectId));
        return "/teacher/subject/topic/new";
    }

    @PostMapping("/{teacherId}/subject/{subjectId}/topic/new")
    public String processNewTopic(@PathVariable Long teacherId,
                                  @PathVariable Long subjectId,
                                  @Valid @RequestBody TopicDto topicDto,
                                  BindingResult result) {
        if (result.hasErrors()) {
            //TODO
            return "/";
        } else {
            teacherService.saveOrUpdateTopic(topicDto);
            return "redirect:/teacher/" + teacherId + "/subject/" + subjectId + "/topic";
        }
    }

    @DeleteMapping("/{teacherId}/subject/{subjectId}/topic/{topicId}")
    public String deleteTopic(@PathVariable Long teacherId,
                              @PathVariable Long subjectId,
                              @PathVariable Long topicId) {

        teacherService.deleteTopic(teacherId, subjectId, topicId);
        return "redirect:/teacher/" + teacherId + "/subject/" + subjectId + "/topic";
    }

    @PutMapping("/{teacherId}/subject/{subjectId}/topic/{topicId}")
    public String updatePutTopic(@PathVariable Long teacherId,
                                 @PathVariable Long subjectId,
                                 @Valid @RequestBody TopicDto topicDto,
                                 BindingResult result) {

        if (result.hasErrors()) {
            //TODO
            return "/";
        } else {
            teacherService.saveOrUpdateTopic(topicDto);
            return "redirect:/teacher/" + teacherId + "/subject/" + subjectId + "/topic";
        }
    }

    @PatchMapping("/{teacherId}/subject/{subjectId}/topic/{topicId}")
    public String updatePatchTopic(@PathVariable Long teacherId,
                                   @PathVariable Long subjectId,
                                   @Valid @RequestBody TopicDto topicDto,
                                   BindingResult result) {

        if (result.hasErrors()) {
            //TODO
            return "/";
        } else {
            TopicDto topic = teacherService.updatePatchTopic(topicDto);
            return "redirect:/teacher/" + teacherId + "/subject/" + subjectId + "/topic";
        }
    }

    //FormTutor

    @GetMapping("/{teacherId}/formTutor/studentCouncil")
    public String getStudentCouncil(@PathVariable Long teacherId, Model model) {

        if (formTutorService.findStudentCouncil(teacherId) != null) {
            model.addAttribute("studentCouncil", formTutorService.findStudentCouncil(teacherId));
        } else {
            model.addAttribute("studentCouncil", formTutorService.initNewStudentCouncil(teacherId));
        }
        model.addAttribute("students", formTutorService.listClassStudents(teacherId));
        return "teacher/formTutor/studentCouncil";

    }


    @PostMapping("/{teacherId}/formTutor/studentCouncil/new")
    public String processNewStudentCouncil(@PathVariable Long teacherId,
                                           @RequestParam(name = "studentId") List<Long> studentsId,
                                           @Valid @RequestBody StudentCouncilDto studentCouncilDto,
                                           BindingResult result) {
        if (result.hasErrors()) {
            //todo: path to view
            return "";
        }

        StudentCouncil studentCouncil = formTutorService.saveStudentCouncil(teacherId, studentCouncilDto, studentsId);

        return "redirect:/teacher/" + teacherId + "/formTutor/studentCouncil";
    }

    @DeleteMapping("/{teacherId}/formTutor/studentCouncil/delete")
    public String deleteStudentCouncil(@PathVariable Long teacherId) {

        formTutorService.deleteStudentCouncil(teacherId);
        return "teacher/formTutor/studentCouncil";
    }


    @PostMapping("/{teacherId}/formTutor/studentCouncil/remove/{studentId}")
    public String removeStudentFromCouncil(@PathVariable Long teacherId,
                                           @PathVariable Long studentId,
                                           @ModelAttribute StudentCouncilDto studentCouncilDto,
                                           Model model) {

        model.addAttribute("students", formTutorService.listClassStudents(teacherId));
        model.addAttribute("studentCouncil", formTutorService.removeStudentFromCouncil(studentCouncilDto, studentId));

        return "teacher/formTutor/studentCouncil";
    }

    @GetMapping("/{teacherId}/formTutor/parentCouncil")
    public String getParentCouncil(@PathVariable Long teacherId, Model model) {

        if (formTutorService.findParentCouncil(teacherId) != null) {
            model.addAttribute("parentCouncil", formTutorService.findParentCouncil(teacherId));
        } else {
            model.addAttribute("parentCouncil", formTutorService.initNewParentCouncil(teacherId));
        }
        model.addAttribute("parents", formTutorService.listClassParents(teacherId));
        return "teacher/formTutor/parentCouncil";

    }


    @PostMapping("/{teacherId}/formTutor/parentCouncil/new")
    public String processNewParentCouncil(@PathVariable Long teacherId,
                                          @RequestParam(name = "parentId") List<Long> parentsId,
                                          @Valid @RequestBody ParentCouncilDto parentCouncilDto,
                                          BindingResult result) {
        if (result.hasErrors()) {
            //todo: path to view
            return "";
        }

        ParentCouncil parentCouncil = formTutorService.saveParentCouncil(teacherId, parentCouncilDto, parentsId);

        return "redirect:/teacher/" + teacherId + "/formTutor/parentCouncil";
    }

    @DeleteMapping("/{teacherId}/formTutor/parentCouncil/delete")
    public String deleteParentCouncil(@PathVariable Long teacherId) {

        formTutorService.deleteParentCouncil(teacherId);

        return "teacher/formTutor/parentCouncil";
    }

    @PostMapping("/{teacherId}/formTutor/parentCouncil/remove/{parentId}")
    public String removeParentFromCouncil(@PathVariable Long teacherId,
                                          @PathVariable Long parentId,
                                          @ModelAttribute ParentCouncilDto parentCouncilDto,
                                          Model model) {

        model.addAttribute("parents", formTutorService.listClassParents(teacherId));
        model.addAttribute("parentCouncil", formTutorService.removeParentFromCouncil(parentCouncilDto, parentId));

        return "teacher/formTutor/parentCouncil";
    }

    @GetMapping("/{teacherId}/formTutor/behaviorGrade")
    public String getClassBehaviorGrades(@PathVariable Long teacherId, Model model) {

        model.addAttribute("behaviorGrades", formTutorService.listBehaviorGrades(teacherId));

        return "teacher/formTutor/behaviorGrades";
    }


    @PostMapping("/{teacherId}/formTutor/behaviorGrade/new")
    public String processNewClassBehaviorGrade(@PathVariable Long teacherId,
                                               @Valid @RequestBody GradeDto gradeDto,
                                               BindingResult result) {

        if (result.hasErrors()) {
            //todo: view path
            return "";
        }
        Grade grade = formTutorService.saveBehaviorGrade(teacherId, gradeDto);

        return "redirect:/teacher/" + teacherId + "/formTutor/behaviorGrade";
    }

    @GetMapping("/{teacherId}/formTutor/studentCard")
    public String getStudentCard(@PathVariable Long teacherId, Model model) {

        model.addAttribute("students", formTutorService.listClassStudents(teacherId));
        model.addAttribute("timeInterval", formTutorService.initNewTimeInterval());

        return "teacher/formTutor/studentCards";
    }

    @PostMapping("/{teacherId}/formTutor/studentCard")
    public String processNewTimeInterval(@PathVariable Long teacherId, Model model,
                                         @RequestParam(name = "startTime") @DateTimeFormat(pattern = "MM/yyyy") LocalDate startTime,
                                         @RequestParam(name = "endTime") @DateTimeFormat(pattern = "MM/yyyy") LocalDate endTime) {

        model.addAttribute("students", formTutorService.listClassStudents(teacherId));
        model.addAttribute("timeInterval", formTutorService.setTimeInterval(startTime, endTime));

        return "teacher/formTutor/studentCards";
    }

    //todo: tests
    @RequestMapping("/studentCard/{studentId}/download/{startTime}/{endTime}")
    public void downloadStudentCardPdf(HttpServletResponse response,
                                       @PathVariable Date startTime,
                                       @PathVariable Date endTime,
                                       @RequestHeader String referer,
                                       @PathVariable Long studentId) throws Exception {

//        System.out.println(startTime.toString());
//        System.out.println(endTime.toString());

            //Not allowing to download via url typing
        if (referer != null && !referer.isEmpty()) {
        }

        formTutorService.createStudentCard(response, studentId, startTime, endTime);
    }
}
