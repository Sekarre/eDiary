package com.ediary.services;

import com.ediary.DTO.EventDto;
import com.ediary.converters.EventToEventDto;
import com.ediary.domain.Event;
import com.ediary.domain.Teacher;
import com.ediary.repositories.TeacherRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TeacherServiceImplTest {

    private final Long teacherId = 1L;

    @Mock
    EventService eventService;

    @Mock
    TeacherRepository teacherRepository;

    @Mock
    EventToEventDto eventToEventDto;

    TeacherService teacherService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        teacherService = new TeacherServiceImpl(eventService, teacherRepository, eventToEventDto);
    }

    @Test
    void listEvents() {
        Teacher teacher = Teacher.builder().id(1L).build();
        when(teacherRepository.findById(teacherId)).thenReturn(Optional.of(teacher));

        when(eventToEventDto.convert(any())).thenReturn(EventDto.builder().id(3L).build());

        when(eventService.listEventsByTeacher(teacher)).thenReturn(Arrays.asList(
                Event.builder().id(1L).build(),
                Event.builder().id(2L).build()
        ));

        List<EventDto> events = teacherService.listEvents(teacherId);

        assertEquals(2, events.size());
        assertEquals(3L, events.get(0).getId());
        verify(teacherRepository, times(1)).findById(teacherId);
        verify(eventService, times(1)).listEventsByTeacher(teacher);
        verify(eventToEventDto, times(2)).convert(any());

    }
}