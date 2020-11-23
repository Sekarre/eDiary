package com.ediary.web.controllers;

import com.ediary.DTO.*;
import com.ediary.converters.UserToUserDto;
import com.ediary.domain.*;
import com.ediary.domain.security.User;
import com.ediary.repositories.TeacherRepository;
import com.ediary.security.perms.FormTutorPermission;
import com.ediary.security.perms.TeacherPermission;
import com.ediary.services.FormTutorService;
import com.ediary.services.TeacherService;
import com.ediary.services.WeeklyAttendancesService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
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
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Controller
@RequestMapping("/teacher")
public class TeacherController {

    private final TeacherService teacherService;
    private final FormTutorService formTutorService;
    private final WeeklyAttendancesService weeklyAttendancesService;

    private final UserToUserDto userToUserDto;

    private final Integer pageSize = 15;


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

    @PreAuthorize("hasRole('TEACHER')")
    @GetMapping("/home")
    public String home() {

        return "/teacher/index";
    }

    @TeacherPermission
    @GetMapping("/{teacherId}/event")
    public String getAllEvents(@PathVariable Long teacherId, Model model) {

        model.addAttribute("events", teacherService.listEvents(teacherId));
        return "/teacher/allEvents";
    }

    //todo: hmm
    @TeacherPermission
    @GetMapping("/{teacherId}/event/{eventId}")
    public String getEvent(@PathVariable Long teacherId, @PathVariable Long eventId, Model model) {

        model.addAttribute("event", teacherService.getEvent(eventId));
        return "/teacher/event";
    }

    @TeacherPermission
    @GetMapping("/{teacherId}/event/new")
    public String newEvent(@PathVariable Long teacherId, Model model) {

        model.addAttribute("event", teacherService.initNewEvent(teacherId));
        return "/teacher/newEvent";
    }

    @TeacherPermission
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

    //todo: hmm
    @TeacherPermission
    @DeleteMapping("/{teacherId}/event/{eventId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public String deleteEvent(@PathVariable Long teacherId, @PathVariable Long eventId) {

        teacherService.deleteEvent(teacherId, eventId);
        return "/" + teacherId + "/event";
    }

    @TeacherPermission //todo: hmm
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

    @TeacherPermission //todo: hmm
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

    @TeacherPermission
    @GetMapping("/{teacherId}/classes")
    public String getAllClasses(@PathVariable Long teacherId, Model model) {

        model.addAttribute("classes", teacherService.listAllClasses());

        return "/teacher/allClasses";
    }

    @TeacherPermission
    @GetMapping("/{teacherId}/behavior")
    public String getAllBehaviorsByTeacher(@PathVariable Long teacherId, Model model) {

        model.addAttribute("behaviors", teacherService.listBehaviors(teacherId));
        return "/teacher/behavior";
    }

    @TeacherPermission
    @GetMapping("/{teacherId}/behavior/new")
    public String newBehavior(@PathVariable Long teacherId, Model model) {

        model.addAttribute("behavior", teacherService.initNewBehavior(teacherId));
        return "/teacher/newBehavior";
    }

    @TeacherPermission
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

    @TeacherPermission //todo: hmm
    @PostMapping("/{teacherId}/behavior/{behaviorId}")
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

    @TeacherPermission //todo: hmm
    @PostMapping("/{teacherId}/behavior/update")
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

    @TeacherPermission
    @GetMapping("/{teacherId}/attendances")
    public String getAttendances(@PathVariable Long teacherId) {
        return "teacher/attendances/main";
    }

    @TeacherPermission
    @GetMapping("/{teacherId}/attendances/class")
    public String getAttendancesClass(@PathVariable Long teacherId, Model model) {
        model.addAttribute("classes", teacherService.listClassByTeacher(teacherId));

        return "teacher/attendances/classes";
    }

    @TeacherPermission
    @GetMapping("/{teacherId}/attendances/class/{classId}")
    public String getAttendancesClassStudent(@PathVariable Long teacherId, @PathVariable Long classId, Model model) {

        model.addAttribute("students", teacherService.listClassStudents(teacherId, classId));
        model.addAttribute("classId", classId);

        return "teacher/attendances/students";
    }

    @TeacherPermission
    @GetMapping("/{teacherId}/attendances/class/{classId}/{studentId}")
    public String getAttendancesClassSelectedStudent(@PathVariable Long teacherId,
                                                     @PathVariable Long classId,
                                                     @PathVariable Long studentId,
                                                     Model model) {

        model.addAttribute("weeklyAttendances",
                weeklyAttendancesService.getAttendancesByWeek(studentId, 7, Date.valueOf(LocalDate.now().minusDays(6))));
        model.addAttribute("isFormTutor", teacherService.isFormTutor(teacherId, classId));

        return "teacher/attendances/studentAttendances";
    }

    @TeacherPermission
    @GetMapping("/{teacherId}/attendances/class/{classId}/{studentId}/{direction}/{dateValue}")
    public String getAllAttendancesClassSelectedStudentWithDate(@PathVariable Long studentId,
                                                                @PathVariable Long teacherId,
                                                                @PathVariable Long classId,
                                                                @PathVariable String direction,
                                                                @PathVariable String dateValue,
                                                                Model model) {
        Date date;
        if (direction.equals("next")) {
            date = Date.valueOf(LocalDate.parse(dateValue).plusDays(7));
        } else {
            date = Date.valueOf(LocalDate.parse(dateValue).minusDays(7));
        }

        model.addAttribute("weeklyAttendances",
                weeklyAttendancesService.getAttendancesByWeek(studentId, 7, date));
        model.addAttribute("studentId", studentId);
        model.addAttribute("classId", classId);
        model.addAttribute("isFormTutor", teacherService.isFormTutor(teacherId, classId));


        return "teacher/attendances/studentAttendances";
    }



    @TeacherPermission
    @GetMapping("{teacherId}/timetable")
    public String getTeacherTimetable(@PathVariable Long teacherId, Model model) {
        model.addAttribute("timetable", teacherService.getTimetableByTeacherId(teacherId));

        return "teacher/timetable/timetable";
    }

    //end of todo

    @TeacherPermission
    @GetMapping("/{teacherId}/grade/subject")
    public String getAllSubjectsGrade(@PathVariable Long teacherId, Model model) {

        model.addAttribute("subjects", teacherService.listSubjects(teacherId));

        return "teacher/grade/subject";
    }

    @TeacherPermission
    @GetMapping("/{teacherId}/grade/subject/{subjectId}")
    public String getAllGradesBySubject(@PathVariable Long teacherId, @PathVariable Long subjectId, Model model) {

        model.addAttribute("grades", teacherService.listGradesBySubject(teacherId, subjectId));

        return "/teacher/grade/allGrades";
    }

    @TeacherPermission
    @GetMapping("/{teacherId}/grade/subject/{subjectId}/new")
    public String newGrade(@PathVariable Long teacherId, @PathVariable Long subjectId, Model model) {

        model.addAttribute("grade", teacherService.initNewGrade(teacherId, subjectId));
        return "/teacher/grade/newGrade";
    }

    @TeacherPermission
    @PostMapping("/{teacherId}/grade/subject/{subjectId}/new")
    public String processNewGrade(@PathVariable Long teacherId,
                                  @PathVariable Long subjectId,
                                  @Valid @RequestBody GradeDto gradeDto, BindingResult result) {
        if (result.hasErrors()) {
            //todo: add view path
            return "/";
        } else {
            teacherService.saveOrUpdateGrade(gradeDto);
            return "redirect:/teacher/" + teacherId + "/grade/subject/" + subjectId;
        }
    }

    @TeacherPermission
    @DeleteMapping("/{teacherId}/grade/subject/{subjectId}/{gradeId}")
    public String deleteGrade(@PathVariable Long teacherId,
                              @PathVariable Long subjectId, @PathVariable Long gradeId) {

        teacherService.deleteGrade(teacherId, subjectId, gradeId);
        return "redirect:/teacher/" + teacherId + "/grade/subject/" + subjectId;
    }

    @TeacherPermission
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

    @TeacherPermission
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

    @TeacherPermission
    @GetMapping("/{teacherId}/lesson/subject")
    public String getAllSubjects(@PathVariable Long teacherId, Model model) {

        model.addAttribute("subjects", teacherService.listSubjects(teacherId));

        return "/teacher/lesson/subject";
    }

    @TeacherPermission
    @GetMapping("/{teacherId}/lesson/subject/{subjectId}")
    public String getAllLessonsBySubject(@PathVariable Long teacherId,
                                         @RequestParam(name = "page", required = false) Optional<Integer> page,
                                         @PathVariable Long subjectId, Model model) {

        model.addAttribute("page", page);
        model.addAttribute("lessons", teacherService.listLessons(page.orElse(0), pageSize, teacherId, subjectId));
        model.addAttribute("subjectId", subjectId);

        return "/teacher/lesson/allLessons";
    }

    @TeacherPermission
    @GetMapping("{teacherId}/lesson/subject/{subjectId}/new")
    public String newLesson(@PathVariable Long teacherId, @PathVariable Long subjectId, Model model) {

        model.addAttribute("topics", teacherService.listTopics(teacherId, subjectId));
        model.addAttribute("newLesson", teacherService.initNewLesson(subjectId));

        return "/teacher/lesson/newLesson";
    }

    @TeacherPermission
    @PostMapping("{teacherId}/lesson/subject/{subjectId}/new")
    public String processNewLesson(@PathVariable Long teacherId,
                                   @PathVariable Long subjectId,
                                   @Valid @ModelAttribute LessonDto newLesson,
                                   BindingResult result) {

        if (result.hasErrors()) {
            //todo: add view path
            return "";
        } else {
            Lesson savedLesson = teacherService.saveLesson(newLesson);
        }
        return "redirect:/teacher/" + teacherId + "/lesson/subject/" + subjectId;
    }

    @TeacherPermission
    @GetMapping("/{teacherId}/lesson/subject/{subjectId}/events")
    public String getClassEvents(@PathVariable Long teacherId,
                                 @PathVariable Long subjectId,
                                 @RequestParam(name = "page", required = false) Optional<Integer> page,
                                 Model model) {
        model.addAttribute("events", teacherService.listClassEvents(teacherId, subjectId, page.orElse(0), 10));
        model.addAttribute("subjectId", subjectId);
        model.addAttribute("page", page);

        return "/teacher/lesson/events";
    }


    @TeacherPermission
    @GetMapping("/{teacherId}/lesson/subject/{subjectId}/newEvent")
    public String newClassEvent(@PathVariable Long teacherId,
                                @PathVariable Long subjectId,
                                Model model) {

        model.addAttribute("newEvent", teacherService.initNewClassEvent(teacherId, subjectId));
        model.addAttribute("subjectId", subjectId);

        return "/teacher/lesson/newEvent";
    }

    @TeacherPermission
    @PostMapping("{teacherId}/lesson/subject/{subjectId}/newEvent")
    public String processNewClassEvent(@PathVariable Long teacherId,
                                        @PathVariable Long subjectId,
                                        @ModelAttribute EventDto newEvent) {


        Event event = teacherService.saveEvent(newEvent);
        return "redirect:/teacher/" + teacherId + "/lesson/subject/" + subjectId + "/events";

    }


    @TeacherPermission
    @GetMapping("{teacherId}/lesson/subject/{subjectId}/{classId}/{lessonId}")
    public String getLesson(@PathVariable Long teacherId,
                            @PathVariable Long subjectId,
                            @PathVariable Long classId,
                            @PathVariable Long lessonId,
                            Model model) {

        model.addAttribute("studentsWithGrades", teacherService.listStudentsLessonGrades(teacherId, lessonId));
        model.addAttribute("studentsWithAttendances", teacherService.listStudentsLessonAttendances(teacherId, lessonId));
        model.addAttribute("maxGrades", teacherService.maxGradesCount(teacherId, lessonId));
        model.addAttribute("subjectId", subjectId);
        model.addAttribute("lessonId", lessonId);
        model.addAttribute("classId", classId);
        model.addAttribute("grade", teacherService.initNewLessonGrade(teacherId, subjectId, lessonId));
        model.addAttribute("attendance", teacherService.initNewLessonAttendance(teacherId, subjectId, lessonId));

        return "/teacher/lesson/lesson";
    }

    @TeacherPermission
    @PostMapping("{teacherId}/lesson/subject/{subjectId}/{classId}/{lessonId}/delete")
    public String deleteLesson(@PathVariable Long teacherId,
                               @PathVariable Long subjectId,
                               @PathVariable Long classId,
                               @PathVariable Long lessonId) {

        teacherService.deleteLesson(lessonId);

        return "redirect:/teacher/" + teacherId + "/lesson/subject/" + subjectId;
    }

    @TeacherPermission
    @PostMapping("{teacherId}/lesson/subject/{subjectId}/{classId}/{lessonId}/newAttendance")
    public String newLessonAttendance(@PathVariable Long teacherId,
                                      @PathVariable Long subjectId,
                                      @PathVariable Long classId,
                                      @PathVariable Long lessonId,
                                      @Valid @RequestBody AttendanceDto attendanceDto) {


        Attendance attendance = teacherService.saveAttendance(attendanceDto);
        return "redirect:/teacher/" + teacherId + "/lesson/subject/" + subjectId +  "/" + classId + "/" +
                lessonId;
    }

    @TeacherPermission
    @PostMapping("{teacherId}/lesson/subject/{subjectId}/{classId}/{lessonId}/newAttendancesClass")
    public String newLessonAttendancesForClass(@PathVariable Long teacherId,
                                      @PathVariable Long subjectId,
                                      @PathVariable Long classId,
                                      @PathVariable Long lessonId,
                                      @Valid @RequestBody AttendanceDto attendanceDto) {


        List<Attendance> attendance = teacherService.saveAttendancesForClass(attendanceDto, classId, teacherId);
        return "redirect:/teacher/" + teacherId + "/lesson/subject/" + subjectId +  "/" + classId + "/" +
                lessonId;
    }

    @TeacherPermission
    @PostMapping("{teacherId}/lesson/subject/{subjectId}/{lessonId}/newGrade")
    public String processNewLessonGrade(@PathVariable Long teacherId,
                                        @PathVariable Long subjectId,
                                        @PathVariable Long lessonId,
                                        @Valid @RequestBody GradeDto gradeDto,
                                        BindingResult result) {

        if (result.hasErrors()) {
            //todo: view path
            return "";
        }

        Grade grade = teacherService.saveOrUpdateGrade(gradeDto);
        Long classId = grade.getSubject().getSchoolClass().getId();

        return "redirect:/teacher/" + teacherId + "/lesson/subject/" + subjectId +  "/" + classId + "/" +
                lessonId;
    }

    @TeacherPermission
    @PostMapping("{teacherId}/lesson/subject/{subjectId}/{lessonId}/{classId}/{studentId}/{gradeId}/delete")
    public String deleteGrade(@PathVariable Long teacherId,
                              @PathVariable Long subjectId,
                              @PathVariable Long lessonId,
                              @PathVariable Long classId,
                              @PathVariable Long studentId,
                              @PathVariable Long gradeId) {

        teacherService.deleteLessonGrade(studentId, gradeId);

        return "redirect:/teacher/" + teacherId + "/lesson/subject/" + subjectId +  "/" + classId + "/" +
                lessonId;
    }


    @TeacherPermission
    @GetMapping("/{teacherId}/subject")
    public String getSubjects(@PathVariable Long teacherId, Model model) {

        model.addAttribute("subjects", teacherService.listSubjects(teacherId));
        return "/teacher/subject/allSubject";
    }

    @TeacherPermission
    @GetMapping("/{teacherId}/subject/{subjectId}")
    public String getSubject(@PathVariable Long teacherId, @PathVariable Long subjectId, Model model) {

        model.addAttribute("subject", teacherService.getSubjectById(teacherId, subjectId));
        model.addAttribute("topics", teacherService.listTopics(teacherId, subjectId));
        return "/teacher/subject/subject";
    }

    @TeacherPermission
    @GetMapping("/{teacherId}/subject/new")
    public String newSubject(@PathVariable Long teacherId, Model model) {

        model.addAttribute("subject", teacherService.initNewSubject(teacherId));
        model.addAttribute("schoolClasses", teacherService.listAllClasses());
        return "/teacher/subject/newSubject";
    }

    @TeacherPermission
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

    @TeacherPermission
    @PostMapping("/{teacherId}/subject/{subjectId}/delete")
    public String deleteSubject(@PathVariable Long teacherId, @PathVariable Long subjectId) {

        teacherService.deleteSubject(teacherId, subjectId);
        return "redirect:/teacher/" + teacherId + "/subject";
    }

    @TeacherPermission
    @GetMapping("/{teacherId}/subject/{subjectId}/update")
    public String updateSubject(@PathVariable Long teacherId,
                                @PathVariable Long subjectId, Model model) {

        model.addAttribute("subject", teacherService.getSubjectById(teacherId, subjectId));
        model.addAttribute("schoolClasses", teacherService.listAllClasses());
        return "/teacher/subject/updateSubject";

    }

    @TeacherPermission
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

    @TeacherPermission
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

    @TeacherPermission
    @GetMapping("/{teacherId}/subject/{subjectId}/topic")
    public String getTopicsBySubject(@PathVariable Long teacherId,
                                     @PathVariable Long subjectId,
                                     Model model) {
        model.addAttribute("topics", teacherService.listTopics(teacherId, subjectId));
        return "/teacher/subject/topic";
    }

    @TeacherPermission
    @GetMapping("/{teacherId}/subject/{subjectId}/topic/new")
    public String newTopic(@PathVariable Long teacherId,
                           @PathVariable Long subjectId,
                           Model model) {

        model.addAttribute("topic", teacherService.initNewTopic(teacherId, subjectId));
        return "/teacher/subject/topic/new";
    }

    @TeacherPermission
    @PostMapping("/{teacherId}/subject/{subjectId}/topic/new")
    public String processNewTopic(@PathVariable Long teacherId,
                                  @PathVariable Long subjectId,
                                  @Valid @RequestBody TopicDto topicDto,
                                  BindingResult result) {
        if (result.hasErrors()) {
            //TODO
            return "/";
        } else {
            teacherService.saveTopic(topicDto);
            return "redirect:/teacher/" + teacherId + "/subject";
        }
    }

    @TeacherPermission
    @PostMapping("/{teacherId}/subject/{subjectId}/topic/{topicId}")
    public String deleteTopic(@PathVariable Long teacherId,
                              @PathVariable Long subjectId,
                              @PathVariable Long topicId) {

        teacherService.deleteTopic(teacherId, subjectId, topicId);
        return "redirect:/teacher/" + teacherId + "/subject/" + subjectId;
    }

    @TeacherPermission
    @PutMapping("/{teacherId}/subject/{subjectId}/topic/{topicId}")
    public String updatePutTopic(@PathVariable Long teacherId,
                                 @PathVariable Long subjectId,
                                 @Valid @RequestBody TopicDto topicDto,
                                 BindingResult result) {

        if (result.hasErrors()) {
            //TODO
            return "/";
        } else {
            teacherService.updateTopic(topicDto);
            return "redirect:/teacher/" + teacherId + "/subject/" + subjectId + "/topic";
        }
    }

    @TeacherPermission
    @PostMapping("/{teacherId}/subject/{subjectId}/topic/edit/{topicId}")
    public String updatePatchTopic(@PathVariable Long teacherId,
                                   @PathVariable Long subjectId,
                                   @PathVariable Long topicId,
                                   @Valid @RequestBody TopicDto topicDto,
                                   BindingResult result) {

        if (result.hasErrors()) {
            //TODO
            return "/";
        } else {
            topicDto.setId(topicId);
            TopicDto topic = teacherService.updatePatchTopic(topicDto);
            return "redirect:/teacher/" + teacherId + "/subject/" + subjectId;
        }
    }

    //FormTutor

    @FormTutorPermission
    @PostMapping("/{teacherId}/attendances/class/{classId}/{studentId}/excuse")
    public String processExcuseAttendancesFormTutor(@PathVariable Long teacherId,
                                                    @PathVariable Long classId,
                                                    @PathVariable Long studentId,
                                                    @RequestParam(name = "toExcuse", required = false) List<Long> ids) {

        teacherService.excuseAttendances(ids, teacherId, studentId, classId);

        return "redirect:/teacher/" + teacherId + "/attendances/class/" + classId + "/" + studentId;
    }

    @FormTutorPermission
    @GetMapping("{teacherId}/attendances/extenuations")
    public String getAllExtenuations(@PathVariable Long teacherId, Model model) {
        model.addAttribute("extenuations", teacherService.listExtenuations(teacherId));

        return "teacher/attendances/extenuations";
    }

    @FormTutorPermission
    @PostMapping("{teacherId}/attendances/extenuation")
    public String processNewExtenuationStatus(@PathVariable Long teacherId,
                                              @RequestParam(name = "accept", required = false) String accept,
                                              @RequestParam(name = "reject", required = false) String reject,
                                              @RequestParam(name = "extId", required = false) Long extenuationId) {
        if (accept != null) {
            teacherService.acceptExtenuation(extenuationId);
        } else {
            teacherService.rejectExtenuation(extenuationId);
        }

        return "redirect:/teacher/" + teacherId + "/attendances/extenuations";
    }

    @FormTutorPermission
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

    @FormTutorPermission
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

    @FormTutorPermission
    @DeleteMapping("/{teacherId}/formTutor/studentCouncil/delete")
    public String deleteStudentCouncil(@PathVariable Long teacherId) {

        formTutorService.deleteStudentCouncil(teacherId);
        return "teacher/formTutor/studentCouncil";
    }

    @FormTutorPermission
    @PostMapping("/{teacherId}/formTutor/studentCouncil/remove/{studentId}")
    public String removeStudentFromCouncil(@PathVariable Long teacherId,
                                           @PathVariable Long studentId,
                                           @ModelAttribute StudentCouncilDto studentCouncilDto,
                                           Model model) {

        model.addAttribute("students", formTutorService.listClassStudents(teacherId));
        model.addAttribute("studentCouncil", formTutorService.removeStudentFromCouncil(studentCouncilDto, studentId));

        return "teacher/formTutor/studentCouncil";
    }

    @FormTutorPermission
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

    @FormTutorPermission
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

    @FormTutorPermission
    @DeleteMapping("/{teacherId}/formTutor/parentCouncil/delete")
    public String deleteParentCouncil(@PathVariable Long teacherId) {

        formTutorService.deleteParentCouncil(teacherId);

        return "teacher/formTutor/parentCouncil";
    }

    @FormTutorPermission
    @PostMapping("/{teacherId}/formTutor/parentCouncil/remove/{parentId}")
    public String removeParentFromCouncil(@PathVariable Long teacherId,
                                          @PathVariable Long parentId,
                                          @ModelAttribute ParentCouncilDto parentCouncilDto,
                                          Model model) {

        model.addAttribute("parents", formTutorService.listClassParents(teacherId));
        model.addAttribute("parentCouncil", formTutorService.removeParentFromCouncil(parentCouncilDto, parentId));

        return "teacher/formTutor/parentCouncil";
    }

    @FormTutorPermission
    @GetMapping("/{teacherId}/formTutor/behaviorGrade")
    public String getClassBehaviorGrades(@PathVariable Long teacherId, Model model) {

        model.addAttribute("behaviorGrades", formTutorService.listBehaviorGrades(teacherId));

        return "teacher/formTutor/behaviorGrades";
    }

    @FormTutorPermission
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

    @FormTutorPermission
    @GetMapping("/{teacherId}/formTutor/studentCard")
    public String getStudentCard(@PathVariable Long teacherId, Model model) {

        model.addAttribute("students", formTutorService.listClassStudents(teacherId));
        model.addAttribute("timeInterval", formTutorService.initNewTimeInterval());

        return "teacher/formTutor/studentCards";
    }

    @FormTutorPermission
    @PostMapping("/{teacherId}/formTutor/studentCard")
    public String processNewTimeInterval(@PathVariable Long teacherId, Model model,
                                         @RequestParam(name = "startTime") @DateTimeFormat(pattern = "MM/yyyy") LocalDate startTime,
                                         @RequestParam(name = "endTime") @DateTimeFormat(pattern = "MM/yyyy") LocalDate endTime) {

        model.addAttribute("students", formTutorService.listClassStudents(teacherId));
        model.addAttribute("timeInterval", formTutorService.setTimeInterval(startTime, endTime));

        return "teacher/formTutor/studentCards";
    }

    //todo: tests
    @FormTutorPermission
    @RequestMapping("/studentCard/{studentId}/download/{startTime}/{endTime}")
    public void downloadStudentCardPdf(HttpServletResponse response,
                                       @PathVariable Date startTime,
                                       @PathVariable Date endTime,
                                       @RequestHeader String referer,
                                       @PathVariable Long studentId) throws Exception {

        //Not allowing to download via url typing
        if (referer != null && !referer.isEmpty()) {
        }

        formTutorService.createStudentCard(response, studentId, startTime, endTime);
    }
}
