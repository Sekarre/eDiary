package com.ediary.web.controllers;

import com.ediary.DTO.BehaviorDto;
import com.ediary.DTO.ClassDto;
import com.ediary.DTO.EventDto;
import com.ediary.DTO.LessonDto;
import com.ediary.domain.Behavior;
import com.ediary.domain.Event;
import com.ediary.domain.Notice;
import com.ediary.services.TeacherService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class TeacherControllerTest {

    private final Long teacherId = 1L;

    @Mock
    TeacherService teacherService;

    TeacherController teacherController;

    MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        teacherController = new TeacherController(teacherService);
        mockMvc = MockMvcBuilders.standaloneSetup(teacherController).build();
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
                .andExpect(status().isNoContent())
                .andExpect(view().name("/" + teacherId + "/behavior"));

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
                .andExpect(view().name("/" + teacherId + "/behavior/" + behaviorId));

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
                .andExpect(status().isOk())
                .andExpect(view().name("/" + teacherId + "/behavior/" + behaviorId));

        verify(teacherService, times(1)).updatePatchBehavior(any());
    }

    @Test
    void getAllLessonsBySubject() throws Exception {

        Long subjectId = 1L;

        when(teacherService.listLessons(anyLong(), anyLong())).thenReturn(Arrays.asList(
                LessonDto.builder().id(1L).build(),
                LessonDto.builder().id(2L).build()
        ));

        mockMvc.perform(get("/teacher/" + teacherId + "/subject/" + subjectId))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("lessons"))
                .andExpect(view().name("/teacher/subject/lessons"));


        verify(teacherService, times(1)).listLessons(1L, 1L);
        assertEquals(2, teacherService.listLessons(1L, 1L).size());
    }

}