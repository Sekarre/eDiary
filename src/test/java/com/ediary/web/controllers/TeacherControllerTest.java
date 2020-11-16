package com.ediary.web.controllers;

import com.ediary.DTO.*;
import com.ediary.converters.UserToUserDto;
import com.ediary.domain.*;
import com.ediary.domain.helpers.TimeInterval;
import com.ediary.services.FormTutorService;
import com.ediary.services.TeacherService;
import com.ediary.services.WeeklyAttendancesService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class TeacherControllerTest {

    private final Long teacherId = 1L;

    @Mock
    TeacherService teacherService;
    @Mock
    FormTutorService formTutorService;
    @Mock
    WeeklyAttendancesService weeklyAttendancesService;

    @Mock
    UserToUserDto userToUserDto;

    TeacherController teacherController;

    MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        teacherController = new TeacherController(teacherService, formTutorService, weeklyAttendancesService, userToUserDto);
        mockMvc = MockMvcBuilders.standaloneSetup(teacherController).build();
    }

    @Test
    void authenticatedUserAndSTeacher() throws Exception {

        when(userToUserDto.convert(any())).thenReturn(UserDto.builder().build());
        when(teacherService.findByUser(any())).thenReturn(TeacherDto.builder().build());

        mockMvc.perform(get("/teacher/" + teacherId + "/event"))
                .andExpect(model().attributeExists("user"))
                .andExpect(model().attributeExists("teacher"));

        verify(userToUserDto, times(1)).convert(any());
        verify(teacherService, times(1)).findByUser(any());
    }

    @Test
    void getAllEvents() throws Exception {

        when(teacherService.listEvents(teacherId)).thenReturn(Arrays.asList(
                EventDto.builder().id(1L).build(),
                EventDto.builder().id(2L).build()
        ));

        mockMvc.perform(get("/teacher/" + teacherId + "/event"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("events"))
                .andExpect(view().name("/teacher/allEvents"));

        verify(teacherService, times(1)).listEvents(teacherId);
        assertEquals(2, teacherService.listEvents(teacherId).size());
    }

    @Test
    void getEvent() throws Exception {

        Long eventId = 2L;

        when(teacherService.getEvent(eventId)).thenReturn(EventDto.builder().build());

        mockMvc.perform(get("/teacher/" + teacherId + "/event/" + eventId))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("event"))
                .andExpect(view().name("/teacher/event"));

        verify(teacherService, times(1)).getEvent(eventId);
    }

    @Test
    void newEvent() throws Exception {
        when(teacherService.initNewEvent(teacherId)).thenReturn(EventDto.builder().build());

        mockMvc.perform(get("/teacher/" + teacherId + "/event/new"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("event"))
                .andExpect(view().name("/teacher/newEvent"));

        verify(teacherService, times(1)).initNewEvent(teacherId);
    }

    @Test
    void processNewEvent() throws Exception {
        when(teacherService.saveEvent(any())).thenReturn(Event.builder().build());

        mockMvc.perform(post("/teacher/" + teacherId + "/event/new")
                .contentType(MediaType.APPLICATION_JSON)
                .content(AbstractAsJsonControllerTest.asJsonString(EventDto.builder().build())))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/teacher/event"));

        verify(teacherService, times(1)).saveEvent(any());
    }

    @Test
    void deleteEvent() throws Exception {
        Long eventId = 3L;

        mockMvc.perform(delete("/teacher/" + teacherId + "/event/" + eventId))
                .andExpect(status().isNoContent())
                .andExpect(view().name("/" + teacherId + "/event"));

        verify(teacherService, times(1)).deleteEvent(teacherId, eventId);
    }

    @Test
    void updatePutEvent() throws Exception {
        Long eventId = 2L;
        EventDto eventDto = EventDto.builder().id(eventId).build();

        when(teacherService.updatePutEvent(any())).thenReturn(EventDto.builder().id(eventId).build());

        mockMvc.perform(put("/teacher/" + teacherId + "/event/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(AbstractAsJsonControllerTest.asJsonString(eventDto)))
                .andExpect(status().isOk())
                .andExpect(view().name("/" + teacherId + "/event/" + eventId));

        verify(teacherService, times(1)).updatePutEvent(any());
    }

    @Test
    void updatePatchEvent() throws Exception {
        Long eventId = 2L;
        EventDto eventDto = EventDto.builder().id(eventId).build();

        when(teacherService.updatePatchEvent(any())).thenReturn(EventDto.builder().id(eventId).build());

        mockMvc.perform(patch("/teacher/" + teacherId + "/event/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(AbstractAsJsonControllerTest.asJsonString(eventDto)))
                .andExpect(status().isOk())
                .andExpect(view().name("/" + teacherId + "/event/" + eventId));

        verify(teacherService, times(1)).updatePatchEvent((any()));
    }

    @Test
    void getAllClasses() throws Exception {

        when(teacherService.listAllClasses()).thenReturn(Arrays.asList(
                ClassDto.builder().id(1L).build(),
                ClassDto.builder().id(2L).build()
        ));

        mockMvc.perform(get("/teacher/" + teacherId + "/classes"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("classes"))
                .andExpect(view().name("/teacher/allClasses"));


        verify(teacherService, times(1)).listAllClasses();
        assertEquals(2, teacherService.listAllClasses().size());
    }

    @Test
    void getAllBehaviorsByTeacher() throws Exception {

        when(teacherService.listBehaviors(teacherId)).thenReturn(Arrays.asList(
                BehaviorDto.builder().id(1L).build(),
                BehaviorDto.builder().id(2L).build()
        ));

        mockMvc.perform(get("/teacher/" + teacherId + "/behavior"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("behaviors"))
                .andExpect(view().name("/teacher/behavior"));


        verify(teacherService, times(1)).listBehaviors(teacherId);
        assertEquals(2, teacherService.listBehaviors(teacherId).size());
    }

    @Test
    void newBehavior() throws Exception {
        when(teacherService.initNewBehavior(teacherId)).thenReturn(BehaviorDto.builder().build());

        mockMvc.perform(get("/teacher/" + teacherId + "/behavior/new"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("behavior"))
                .andExpect(view().name("/teacher/newBehavior"));

        verify(teacherService, times(1)).initNewBehavior(teacherId);
    }

    @Test
    void processNewBehavior() throws Exception {
        when(teacherService.saveBehavior(any())).thenReturn(Behavior.builder().build());

        mockMvc.perform(post("/teacher/" + teacherId + "/behavior/new")
                .contentType(MediaType.APPLICATION_JSON)
                .content(AbstractAsJsonControllerTest.asJsonString(BehaviorDto.builder().build())))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/teacher/behavior"));

        verify(teacherService, times(1)).saveBehavior(any());
    }

    @Test
    void deleteBehavior() throws Exception {
        Long behaviorId = 3L;

        mockMvc.perform(delete("/teacher/" + teacherId + "/behavior/" + behaviorId))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/teacher/" + teacherId + "/behavior"));

        verify(teacherService, times(1)).deleteBehavior(teacherId, behaviorId);
    }

    @Test
    void updatePutBehavior() throws Exception {
        Long behaviorId = 2L;
        BehaviorDto behaviorDto = BehaviorDto.builder().id(behaviorId).build();

        when(teacherService.updatePutBehavior(any())).thenReturn(BehaviorDto.builder().id(behaviorId).build());

        mockMvc.perform(put("/teacher/" + teacherId + "/behavior/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(AbstractAsJsonControllerTest.asJsonString(behaviorDto)))
                .andExpect(status().isOk())
                .andExpect(view().name("/teacher/" + teacherId + "/behavior"));

        verify(teacherService, times(1)).updatePutBehavior(any());
    }

    @Test
    void updatePatchBehavior() throws Exception {
        Long behaviorId = 2L;
        BehaviorDto behaviorDto = BehaviorDto.builder().id(behaviorId).build();

        when(teacherService.updatePatchBehavior(any())).thenReturn(BehaviorDto.builder().id(behaviorId).build());

        mockMvc.perform(patch("/teacher/" + teacherId + "/behavior/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(AbstractAsJsonControllerTest.asJsonString(behaviorDto)))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/teacher/" + teacherId + "/behavior"));

        verify(teacherService, times(1)).updatePatchBehavior(any());
    }


    @Test
    void getAllGradesBySubject() throws Exception {
        Long subjectId = 1L;

        when(teacherService.listGradesBySubject(anyLong(), anyLong())).thenReturn(Arrays.asList(
                GradeDto.builder().id(1L).build(),
                GradeDto.builder().id(2L).build()
        ));

        mockMvc.perform(get("/teacher/" + teacherId + "/grade/subject/" + subjectId))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("grades"))
                .andExpect(view().name("/teacher/grade/allGrades"));


        verify(teacherService, times(1)).listGradesBySubject(1L, 1L);
        assertEquals(2, teacherService.listGradesBySubject(1L, 1L).size());
    }

    @Test
    void newGrade() throws Exception {
        Long subjectId = 1L;

        when(teacherService.initNewGrade(teacherId, subjectId)).thenReturn(GradeDto.builder().build());

        mockMvc.perform(get("/teacher/" + teacherId + "/grade/subject/" + subjectId + "/new"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("grade"))
                .andExpect(view().name("/teacher/grade/newGrade"));

        verify(teacherService, times(1)).initNewGrade(teacherId, subjectId);
    }

    @Test
    void processNewGrade() throws Exception {
        Long subjectId = 1L;

        when(teacherService.saveGrade(any())).thenReturn(Grade.builder().build());

        mockMvc.perform(post("/teacher/" + teacherId + "/grade/subject/" + subjectId + "/new")
                .contentType(MediaType.APPLICATION_JSON)
                .content(AbstractAsJsonControllerTest.asJsonString(BehaviorDto.builder().build())))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/teacher/" + teacherId + "/grade/subject/" + subjectId));

        verify(teacherService, times(1)).saveGrade(any());
    }

    @Test
    void deleteGrade() throws Exception {
        Long subjectId = 1L;
        Long gradeId = 1L;

        mockMvc.perform(delete("/teacher/" + teacherId + "/grade/subject/" + subjectId + "/" + gradeId))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/teacher/" + teacherId + "/grade/subject/" + subjectId));

        verify(teacherService, times(1)).deleteGrade(teacherId, subjectId, gradeId);
    }

    @Test
    void updatePutGrade() throws Exception {
        Long gradeId = 1L;
        Long subjectId = 1L;
        GradeDto gradeDto = GradeDto.builder().id(gradeId).build();

        when(teacherService.updatePutGrade(any())).thenReturn(gradeDto);

        mockMvc.perform(put("/teacher/" + teacherId + "/grade/subject/" + subjectId + "/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(AbstractAsJsonControllerTest.asJsonString(gradeDto)))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/teacher/" + teacherId + "/grade/subject/" + subjectId));

        verify(teacherService, times(1)).updatePutGrade(any());
    }

    @Test
    void updatePatchGrade() throws Exception {
        Long gradeId = 1L;
        Long subjectId = 1L;
        GradeDto gradeDto = GradeDto.builder().id(gradeId).build();

        when(teacherService.updatePatchGrade(any())).thenReturn(gradeDto);

        mockMvc.perform(patch("/teacher/" + teacherId + "/grade/subject/" + subjectId + "/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(AbstractAsJsonControllerTest.asJsonString(gradeDto)))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/teacher/" + teacherId + "/grade/subject/" + subjectId));

        verify(teacherService, times(1)).updatePatchGrade(any());
    }


    @Test
    void getAllSubjects() throws Exception {

        Long teacherId = 1L;

        when(teacherService.listSubjects(anyLong())).thenReturn(Arrays.asList(
                SubjectDto.builder().id(1L).build(),
                SubjectDto.builder().id(2L).build()
        ));

        mockMvc.perform(get("/teacher/" + teacherId + "/lesson/subject"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("subjects"))
                .andExpect(view().name("/teacher/lesson/subject"));


        verify(teacherService, times(1)).listSubjects(teacherId);
        assertEquals(2, teacherService.listSubjects(teacherId).size());
    }

    @Test
    void getAllLessonsBySubject() throws Exception {

        Long subjectId = 1L;

        when(teacherService.listLessons(anyLong(), anyLong(), any())).thenReturn(Arrays.asList(
                LessonDto.builder().id(1L).build(),
                LessonDto.builder().id(2L).build()
        ));

        mockMvc.perform(get("/teacher/" + teacherId + "/lesson/subject/" + subjectId))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("lessons"))
                .andExpect(view().name("/teacher/lesson/allLessons"));


        verify(teacherService, times(1)).listLessons(1L, 1L, 1L);
        assertEquals(2, teacherService.listLessons(1L, 1L, 1L).size());
    }

    @Test
    void newLesson() throws Exception {
        Long subjectId = 1L;

        when(teacherService.initNewLesson(subjectId)).thenReturn(LessonDto.builder().build());

        mockMvc.perform(get("/teacher/" + teacherId + "/lesson/subject/" + subjectId + "/new"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("lesson"))
                .andExpect(view().name("/teacher/lesson/newLesson"));

        verify(teacherService, times(1)).initNewLesson(subjectId);
    }

    @Test
    void processNewLesson() throws Exception {
        Long subjectId = 1L;

        Lesson lesson = Lesson.builder()
                .id(1L)
                .build();

        when(teacherService.saveLesson(any())).thenReturn(lesson);

        mockMvc.perform(post("/teacher/" + teacherId + "/lesson/subject/" + subjectId + "/new")
                .contentType(MediaType.APPLICATION_JSON)
                .content(AbstractAsJsonControllerTest.asJsonString(BehaviorDto.builder().build())))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name(
                        "redirect:/teacher/" + teacherId + "/lesson/subject/" + subjectId + "/" + lesson.getId()));

        verify(teacherService, times(1)).saveLesson(any());
    }

    @Test
    void getAllClassesBySubject() throws Exception {

        Long subjectId = 1L;

        ClassDto schoolClass = ClassDto.builder().id(1L).build();

        when(teacherService.listClassesByTeacherAndSubject(teacherId, subjectId)).thenReturn(Collections.singletonList(
                schoolClass
        ));

        mockMvc.perform(get("/teacher/" + teacherId + "/lesson/subject/" + subjectId + "/class"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/teacher/" + teacherId
                        + "/lesson/subject/" + subjectId + "/class/" + schoolClass.getId()));

        verify(teacherService, times(1)).listClassesByTeacherAndSubject(teacherId, subjectId);
        assertEquals(1, teacherService.listClassesByTeacherAndSubject(teacherId, subjectId).size());
    }

    @Test
    void getClassBySubject() throws Exception {
        Long subjectId = 1L;
        Long classId = 1L;

        when(teacherService.getSchoolClassByTeacherAndSubject(classId, subjectId, teacherId)).thenReturn(
                ClassDto.builder().id(1L).build()
        );

        mockMvc.perform(get("/teacher/" + teacherId + "/lesson/subject/" + subjectId + "/class/" + classId))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("class"))
                .andExpect(view().name("/teacher/lesson/class"));

        verify(teacherService, times(1)).getSchoolClassByTeacherAndSubject(classId, subjectId, teacherId);
        assertNotNull(teacherService.getSchoolClassByTeacherAndSubject(classId, subjectId, teacherId));
    }

    @Test
    void getAllLessonsBySubjectAndClass() throws Exception {
        Long subjectId = 1L;
        Long classId = 1L;

        when(teacherService.getSchoolClassByTeacherAndSubject(classId, subjectId, teacherId)).thenReturn(
                ClassDto.builder().id(1L).build()
        );

        mockMvc.perform(get("/teacher/" + teacherId + "/lesson/subject/" + subjectId + "/class/" + classId + "/lessons"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("lessons"))
                .andExpect(view().name("/teacher/lesson/allClassLessons"));

        verify(teacherService, times(1)).listLessons(teacherId, subjectId, classId);
        assertNotNull(teacherService.listLessons(teacherId, subjectId, classId));
    }

    @Test
    void getLesson() throws Exception {
        Long classId = 1L;
        Long subjectId = 1L;
        Long lessonId = 1L;

        when(teacherService.getLesson(classId)).thenReturn(
                LessonDto.builder().id(lessonId).build()
        );

        mockMvc.perform(get("/teacher/" + teacherId + "/lesson/subject/" + subjectId
                + "/class/" + classId + "/lessons/" + lessonId))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("lesson"))
                .andExpect(view().name("/teacher/lesson/singleLesson"));

        verify(teacherService, times(1)).getLesson(classId);
        assertNotNull(teacherService.getLesson(classId));
    }

    @Test
    void getLessonAttendances() throws Exception {
        Long classId = 1L;
        Long subjectId = 1L;
        Long lessonId = 1L;

        when(teacherService.listAttendances(teacherId, subjectId, classId, lessonId)).thenReturn(
                Collections.singletonList(AttendanceDto.builder().build())
        );

        mockMvc.perform(get("/teacher/" + teacherId + "/lesson/subject/" + subjectId
                + "/class/" + classId + "/lessons/" + lessonId + "/attendances"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("attendances"))
                .andExpect(view().name("/teacher/lesson/attendances"));

        verify(teacherService, times(1)).listAttendances(teacherId, subjectId, classId, lessonId);
        assertNotNull(teacherService.listAttendances(teacherId, subjectId, classId, lessonId));
    }

    @Test
    void newLessonAttendance() throws Exception {
        Long classId = 1L;
        Long subjectId = 1L;
        Long lessonId = 1L;

        Attendance attendance = Attendance.builder()
                .id(1L)
                .build();

        when(teacherService.saveAttendance(any())).thenReturn(attendance);

        mockMvc.perform(post(
                "/teacher/" + teacherId + "/lesson/subject/" + subjectId + "/class/" + classId + "/lessons/"
                        + lessonId + "/attendances/new")
                .contentType(MediaType.APPLICATION_JSON)
                .content(AbstractAsJsonControllerTest.asJsonString(BehaviorDto.builder().build())))
                .andExpect(status().isOk())
                .andExpect(view().name(""));


        verify(teacherService, times(1)).saveAttendance(any());
    }

    @Test
    void getLessonStudents() throws Exception {
        Long classId = 1L;
        Long subjectId = 1L;
        Long lessonId = 1L;

        when(teacherService.listLessonStudents(teacherId, subjectId, classId, lessonId)).thenReturn(
                Collections.singletonList(StudentDto.builder().build())
        );

        mockMvc.perform(get("/teacher/" + teacherId + "/lesson/subject/" + subjectId
                + "/class/" + classId + "/lessons/" + lessonId + "/students"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("students"))
                .andExpect(view().name("/teacher/lesson/students"));

        verify(teacherService, times(1)).listLessonStudents(teacherId, subjectId, classId, lessonId);
        assertNotNull(teacherService.listLessonStudents(teacherId, subjectId, classId, lessonId));
    }

    @Test
    void processNewLessonGrade() throws Exception {
        Long classId = 1L;
        Long subjectId = 1L;
        Long lessonId = 1L;
        Long studentId = 1L;


        when(teacherService.saveGrade(any())).thenReturn(Grade.builder().build());

        mockMvc.perform(post(
                "/teacher/" + teacherId + "/lesson/subject/" + subjectId
                        + "/class/" + classId + "/lessons/" + lessonId + "/students/" + studentId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(AbstractAsJsonControllerTest.asJsonString(GradeDto.builder().build())))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name(
                        "redirect:/teacher/" + teacherId + "/lesson/subject/" + subjectId + "/class/" + classId + "/lessons/" +
                                lessonId + "/students/" + studentId));

        assertNotNull(teacherService.saveGrade(any()));
    }

    @Test
    void newLessonEvent() throws Exception {
        Long subjectId = 1L;
        Long classId = 1l;

        when(teacherService.initNewClassEvent(teacherId, classId)).thenReturn(EventDto.builder().build());

        mockMvc.perform(get(
                "/teacher/" + teacherId + "/lesson/subject/" + subjectId + "/class/" + classId + "/newEvent"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("event"))
                .andExpect(view().name("/teacher/lesson/newLessonEvent"));

        verify(teacherService, times(1)).initNewClassEvent(subjectId, classId);
    }

    @Test
    void processNewLessonEvent() throws Exception {
        Long classId = 1L;
        Long subjectId = 1L;

        Event event = Event.builder()
                .id(1L)
                .build();

        when(teacherService.saveEvent(any())).thenReturn(event);

        mockMvc.perform(post(
                "/teacher/" + teacherId + "/lesson/subject/" + subjectId + "/class/" + classId + "/newEvent")
                .contentType(MediaType.APPLICATION_JSON)
                .content(AbstractAsJsonControllerTest.asJsonString(BehaviorDto.builder().build())))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name(
                        "redirect:/teacher/" + teacherId + "/lesson/subject/" + subjectId + "/" + event.getId()));

        verify(teacherService, times(1)).saveEvent(any());
    }


    @Test
    void getSubjects() throws Exception {

        when(teacherService.listSubjects(teacherId)).thenReturn(Arrays.asList(
                SubjectDto.builder().id(1L).build(),
                SubjectDto.builder().id(2L).build()
        ));

        mockMvc.perform(get("/teacher/" + teacherId + "/subject"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("subjects"))
                .andExpect(view().name("/teacher/subject/allSubject"));

        verify(teacherService, times(1)).listSubjects(teacherId);
        assertEquals(2, teacherService.listSubjects(teacherId).size());
    }

    @Test
    void getSubject() throws Exception {

        Long subjectId = 2L;

        when(teacherService.getSubjectById(teacherId, subjectId)).thenReturn(SubjectDto.builder().id(subjectId).build());
        when(teacherService.listTopics(teacherId, subjectId)).thenReturn(Arrays.asList(
                TopicDto.builder().id(1L).build(),
                TopicDto.builder().id(1L).build()
        ));

        mockMvc.perform(get("/teacher/" + teacherId + "/subject/" + subjectId))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("subject"))
                .andExpect(model().attributeExists("topics"))
                .andExpect(view().name("/teacher/subject/subject"));

        verify(teacherService, times(1)).getSubjectById(teacherId, subjectId);
        verify(teacherService, times(1)).listTopics(teacherId, subjectId);
    }

    @Test
    void initNewSubject() throws Exception {

        Long subjectId = 2L;

        when(teacherService.initNewSubject(teacherId)).thenReturn(SubjectDto.builder().id(subjectId).build());

        mockMvc.perform(get("/teacher/" + teacherId + "/subject/new"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("subject"))
                .andExpect(view().name("/teacher/subject/newSubject"));

        verify(teacherService, times(1)).initNewSubject(teacherId);
    }

    @Test
    void processNewSubject() throws Exception {
        when(teacherService.saveOrUpdateSubject(any())).thenReturn(Subject.builder().build());

        mockMvc.perform(post("/teacher/" + teacherId + "/subject/new")
                .contentType(MediaType.APPLICATION_JSON)
                .content(AbstractAsJsonControllerTest.asJsonString(SubjectDto.builder().build())))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/teacher/" + teacherId + "/subject"));

        verify(teacherService, times(1)).saveOrUpdateSubject(any());
    }

    @Test
    void deleteSubject() throws Exception {
        Long subjectId = 3L;

        mockMvc.perform(post("/teacher/" + teacherId + "/subject/" + subjectId + "/delete"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/teacher/" + teacherId + "/subject"));

        verify(teacherService, times(1)).deleteSubject(teacherId, subjectId);
    }

    @Test
    void updateSubject() throws Exception {
        Long subjectId = 2L;

        when(teacherService.getSubjectById(teacherId, subjectId)).thenReturn(SubjectDto.builder().build());
        when(teacherService.listAllClasses()).thenReturn(Arrays.asList(ClassDto.builder().build()));

        mockMvc.perform(get("/teacher/" + teacherId + "/subject/" + subjectId + "/update"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("subject"))
                .andExpect(model().attributeExists("schoolClasses"))
                .andExpect(view().name("/teacher/subject/updateSubject"));

        verify(teacherService, times(1)).getSubjectById(teacherId, subjectId);
        verify(teacherService, times(1)).listAllClasses();
    }

    @Test
    void updatePutSubject() throws Exception {
        Long subjectId = 3L;

        when(teacherService.saveOrUpdateSubject(any())).thenReturn(Subject.builder().build());

        mockMvc.perform(put("/teacher/" + teacherId + "/subject/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(AbstractAsJsonControllerTest.asJsonString(SubjectDto.builder().build())))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/teacher/" + teacherId + "/subject"));

        verify(teacherService, times(1)).saveOrUpdateSubject(any());
    }

    @Test
    void updatePatchSubject() throws Exception {
        Long subjectId = 3L;

        when(teacherService.updatePatchSubject(any())).thenReturn(SubjectDto.builder().id(subjectId).build());

        mockMvc.perform(post("/teacher/" + teacherId + "/subject/" + subjectId + "/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(AbstractAsJsonControllerTest.asJsonString(SubjectDto.builder().id(subjectId).build())))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/teacher/" + teacherId + "/subject"));

        verify(teacherService, times(1)).updatePatchSubject(any());
    }

    @Test
    void getTopicsBySubject() throws Exception {

        Long subjectId = 25L;

        when(teacherService.listTopics(teacherId, subjectId)).thenReturn(Arrays.asList(
                TopicDto.builder().id(1L).build(),
                TopicDto.builder().id(2L).build()
        ));

        mockMvc.perform(get("/teacher/" + teacherId + "/subject/" + subjectId + "/topic"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("topics"))
                .andExpect(view().name("/teacher/subject/topic"));

        verify(teacherService, times(1)).listTopics(teacherId, subjectId);
        assertEquals(2, teacherService.listTopics(teacherId, subjectId).size());
    }

    @Test
    void newTopic() throws Exception {

        Long subjectId = 25L;

        when(teacherService.initNewTopic(teacherId, subjectId)).thenReturn(TopicDto.builder().id(1L).build());

        mockMvc.perform(get("/teacher/" + teacherId + "/subject/" + subjectId + "/topic/new"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("topic"))
                .andExpect(view().name("/teacher/subject/topic/new"));

        verify(teacherService, times(1)).initNewTopic(teacherId, subjectId);
    }

    @Test
    void processNewTopic() throws Exception {

        Long subjectId = 25L;

        when(teacherService.updateTopic(any())).thenReturn(Topic.builder().build());

        mockMvc.perform(post("/teacher/" + teacherId + "/subject/" + subjectId + "/topic/new")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(AbstractAsJsonControllerTest.asJsonString(TopicDto.builder().build())))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/teacher/" + teacherId + "/subject"));

        verify(teacherService, times(1)).saveTopic(any());
    }

    @Test
    void deleteTopic() throws Exception {

        Long subjectId = 25L;
        Long topicId = 10L;

        mockMvc.perform(post("/teacher/" + teacherId + "/subject/" + subjectId + "/topic/" + topicId))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/teacher/" + teacherId + "/subject/" + subjectId));

        verify(teacherService, times(1)).deleteTopic(teacherId, subjectId, topicId);
    }

    @Test
    void updatePutTopic() throws Exception {

        Long subjectId = 25L;
        Long topicId = 10L;

        when(teacherService.updateTopic(any())).thenReturn(Topic.builder().build());

        mockMvc.perform(put("/teacher/" + teacherId + "/subject/" + subjectId + "/topic/" + topicId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(AbstractAsJsonControllerTest.asJsonString(TopicDto.builder().build())))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/teacher/" + teacherId + "/subject/" + subjectId + "/topic"));

        verify(teacherService, times(1)).updateTopic(any());
    }

    @Test
    void updatePatchTopic() throws Exception {

        Long subjectId = 25L;
        Long topicId = 10L;

        when(teacherService.updatePatchTopic(any())).thenReturn(TopicDto.builder().build());

        mockMvc.perform(post("/teacher/" + teacherId + "/subject/" + subjectId + "/topic/edit/" + topicId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(AbstractAsJsonControllerTest.asJsonString(TopicDto.builder().build())))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/teacher/" + teacherId + "/subject/" + subjectId));

        verify(teacherService, times(1)).updatePatchTopic(any());
    }

    @Test
    void getStudentCouncil() throws Exception {
        Long teacherId = 1L;

        when(formTutorService.findStudentCouncil(teacherId)).thenReturn(StudentCouncilDto.builder().build());
        when(formTutorService.listClassStudents(teacherId)).thenReturn(Arrays.asList(
                StudentDto.builder().id(1L).build(),
                StudentDto.builder().id(2L).build()
        ));

        mockMvc.perform(get("/teacher/" + teacherId + "/formTutor/studentCouncil"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("studentCouncil"))
                .andExpect(model().attributeExists("students"))
                .andExpect(view().name("teacher/formTutor/studentCouncil"));

        verify(formTutorService, times(2)).findStudentCouncil(teacherId);
        assertEquals(2, formTutorService.listClassStudents(teacherId).size());
    }

    @Test
    void getStudentCouncilNotExisting() throws Exception {
        Long teacherId = 1L;

        when(formTutorService.findStudentCouncil(teacherId)).thenReturn(null);
        when(formTutorService.initNewStudentCouncil(teacherId)).thenReturn(StudentCouncilDto.builder().build());
        when(formTutorService.listClassStudents(teacherId)).thenReturn(Arrays.asList(
                StudentDto.builder().id(1L).build(),
                StudentDto.builder().id(2L).build()
        ));

        mockMvc.perform(get("/teacher/" + teacherId + "/formTutor/studentCouncil"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("studentCouncil"))
                .andExpect(model().attributeExists("students"))
                .andExpect(view().name("teacher/formTutor/studentCouncil"));

        verify(formTutorService, times(1)).findStudentCouncil(teacherId);
        verify(formTutorService, times(1)).initNewStudentCouncil(teacherId);
        assertEquals(2, formTutorService.listClassStudents(teacherId).size());
    }

    @Test
    void processNewStudentCouncil() throws Exception {
        Long teacherId = 1L;

        when(formTutorService.saveStudentCouncil(any(), any(), any())).thenReturn(StudentCouncil.builder().build());

        mockMvc.perform(post("/teacher/" + teacherId + "/formTutor/studentCouncil/new")
                .param("studentId", String.valueOf(1L))
                .contentType(MediaType.APPLICATION_JSON)
                .content(AbstractAsJsonControllerTest.asJsonString(StudentCouncilDto.builder().build())))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/teacher/" + teacherId + "/formTutor/studentCouncil"));

        verify(formTutorService, times(1)).saveStudentCouncil(any(), any(), any());
        assertNotNull(formTutorService.saveStudentCouncil(any(), any(), any()));
    }

    @Test
    void deleteStudentCouncil() throws Exception {

        mockMvc.perform(delete("/teacher/" + teacherId + "/formTutor/studentCouncil/delete"))
                .andExpect(status().isOk())
                .andExpect(view().name("teacher/formTutor/studentCouncil"));

        verify(formTutorService, times(1)).deleteStudentCouncil(any());
    }

    @Test
    void removeStudentFromCouncil() throws Exception {
        Long parentId = 1L;

        when(formTutorService.removeStudentFromCouncil(any(), any()))
                .thenReturn(StudentCouncilDto.builder().build());
        when(formTutorService.listClassStudents(teacherId)).thenReturn(Arrays.asList(
                StudentDto.builder().id(1L).build(),
                StudentDto.builder().id(2L).build()
        ));

        mockMvc.perform(post("/teacher/" + teacherId + "/formTutor/studentCouncil/remove/" + parentId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(AbstractAsJsonControllerTest.asJsonString(StudentCouncilDto.builder().build())))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("studentCouncil"))
                .andExpect(model().attributeExists("students"))
                .andExpect(view().name("teacher/formTutor/studentCouncil"));

        assertEquals(2, formTutorService.listClassStudents(teacherId).size());
    }


    @Test
    void getParentCouncil() throws Exception {
        Long teacherId = 1L;

        when(formTutorService.findParentCouncil(teacherId)).thenReturn(ParentCouncilDto.builder().build());
        when(formTutorService.listClassParents(teacherId)).thenReturn(Arrays.asList(
                ParentDto.builder().id(1L).build(),
                ParentDto.builder().id(2L).build()
        ));

        mockMvc.perform(get("/teacher/" + teacherId + "/formTutor/parentCouncil"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("parentCouncil"))
                .andExpect(model().attributeExists("parents"))
                .andExpect(view().name("teacher/formTutor/parentCouncil"));

        verify(formTutorService, times(2)).findParentCouncil(teacherId);
        assertEquals(2, formTutorService.listClassParents(teacherId).size());
    }

    @Test
    void processNewParentCouncil() throws Exception {
        Long teacherId = 1L;

        when(formTutorService.saveParentCouncil(any(), any(), any())).thenReturn(ParentCouncil.builder().build());

        mockMvc.perform(post("/teacher/" + teacherId + "/formTutor/parentCouncil/new")
                .param("parentId", String.valueOf(1L))
                .contentType(MediaType.APPLICATION_JSON)
                .content(AbstractAsJsonControllerTest.asJsonString(StudentCouncilDto.builder().build())))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/teacher/" + teacherId + "/formTutor/parentCouncil"));

        verify(formTutorService, times(1)).saveParentCouncil(any(), any(), any());
        assertNotNull(formTutorService.saveParentCouncil(any(), any(), any()));
    }

    @Test
    void deleteParentCouncil() throws Exception {
        mockMvc.perform(delete("/teacher/" + teacherId + "/formTutor/parentCouncil/delete"))
                .andExpect(status().isOk())
                .andExpect(view().name("teacher/formTutor/parentCouncil"));

        verify(formTutorService, times(1)).deleteParentCouncil(any());
    }

    @Test
    void removeParentFromCouncil() throws Exception {
        Long parentId = 1L;
        ParentCouncilDto parentCouncilDto = ParentCouncilDto.builder().build();

        when(formTutorService.removeParentFromCouncil(any(), any()))
                .thenReturn(parentCouncilDto);
        when(formTutorService.listClassParents(teacherId)).thenReturn(Arrays.asList(
                ParentDto.builder().id(1L).build(),
                ParentDto.builder().id(2L).build()
        ));

        mockMvc.perform(post("/teacher/" + teacherId + "/formTutor/parentCouncil/remove/" + parentId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(AbstractAsJsonControllerTest.asJsonString(StudentCouncilDto.builder().build())))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("parentCouncil"))
                .andExpect(model().attributeExists("parents"))
                .andExpect(view().name("teacher/formTutor/parentCouncil"));

        assertEquals(2, formTutorService.listClassParents(teacherId).size());
    }

    @Test
    void getClassBehaviorGrades() throws Exception {
        Long teacherId = 1L;

        when(formTutorService.listBehaviorGrades(teacherId)).thenReturn(Arrays.asList(
                GradeDto.builder().id(1L).build(),
                GradeDto.builder().id(2L).build()
        ));

        mockMvc.perform(get("/teacher/" + teacherId + "/formTutor/behaviorGrade"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("behaviorGrades"))
                .andExpect(view().name("teacher/formTutor/behaviorGrades"));

        verify(formTutorService, times(1)).listBehaviorGrades(teacherId);
        assertEquals(2, formTutorService.listBehaviorGrades(teacherId).size());
    }

    @Test
    void processNewClassBehaviorGrade() throws Exception {
        Long teacherId = 1L;

        when(formTutorService.saveBehaviorGrade(any(), any())).thenReturn(Grade.builder().build());

        mockMvc.perform(post("/teacher/" + teacherId + "/formTutor/behaviorGrade/new")
                .contentType(MediaType.APPLICATION_JSON)
                .content(AbstractAsJsonControllerTest.asJsonString(Grade.builder().build())))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/teacher/" + teacherId + "/formTutor/behaviorGrade"));

        verify(formTutorService, times(1)).saveBehaviorGrade(any(), any());
        assertNotNull(formTutorService.saveBehaviorGrade(any(), any()));
    }

    @Test
    void getStudentCard() throws Exception {
        Long teacherId = 1L;
        Long studentId = 1L;

        when(formTutorService.listClassStudents(teacherId)).thenReturn(Arrays.asList(
                StudentDto.builder().id(1L).build(),
                StudentDto.builder().id(2L).build()
        ));

        mockMvc.perform(get("/teacher/" + teacherId + "/formTutor/studentCard"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("students"))
                .andExpect(view().name("teacher/formTutor/studentCards"));

        assertEquals(2, formTutorService.listClassStudents(teacherId).size());
    }

    @Test
    void processNewTimeInterval() throws Exception {
        Long teacherId = 1L;

        when(formTutorService.listClassStudents(any())).thenReturn(Arrays.asList(
                StudentDto.builder().id(1L).build(),
                StudentDto.builder().id(2L).build()
        ));

        when(formTutorService.setTimeInterval(any(), any())).thenReturn(TimeInterval.builder().build());

        mockMvc.perform(post("/teacher/" + teacherId + "/formTutor/studentCard")
                .param("startTime", LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")))
                .param("endTime", LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("students"))
                .andExpect(model().attributeExists("timeInterval"))
                .andExpect(view().name("teacher/formTutor/studentCards"));

        assertEquals(2, formTutorService.listClassStudents(teacherId).size());
    }


    //todo
    @Test
    void downloadStudentCardPdf() throws Exception {
    }

}