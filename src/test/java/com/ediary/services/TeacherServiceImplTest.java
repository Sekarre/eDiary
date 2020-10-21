package com.ediary.services;

import com.ediary.DTO.ClassDto;
import com.ediary.DTO.EventDto;
import com.ediary.converters.ClassToClassDto;
import com.ediary.converters.EventDtoToEvent;
import com.ediary.converters.EventToEventDto;
import com.ediary.domain.Class;
import com.ediary.domain.Event;
import com.ediary.domain.Teacher;
import com.ediary.repositories.ClassRepository;
import com.ediary.repositories.EventRepository;
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
    ClassRepository classRepository;

    @Mock
    EventRepository eventRepository;

    @Mock
    TeacherRepository teacherRepository;

    @Mock
    EventToEventDto eventToEventDto;

    @Mock
    EventDtoToEvent eventDtoToEvent;

    @Mock
    ClassToClassDto classToClassDto;


    TeacherService teacherService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        teacherService = new TeacherServiceImpl(eventService, teacherRepository, classRepository, eventRepository,
                eventToEventDto, eventDtoToEvent, classToClassDto);
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
    @Test
    void saveEvent() {
        Long eventId = 4L;
        EventDto eventToSave = EventDto.builder().id(eventId).build();
        Event eventReturned = Event.builder().id(eventId).build();

        when(eventDtoToEvent.convert(eventToSave)).thenReturn(Event.builder().id(eventToSave.getId()).build());
        when(eventRepository.save(any())).thenReturn(eventReturned);

        Event savedEvent = teacherService.saveEvent(eventToSave);

        assertEquals(eventId, savedEvent.getId());
        verify(eventDtoToEvent, times(1)).convert(eventToSave);
        verify(eventRepository, times(1)).save(any());


    }

    @Test
    void initNewEvent() {
        Teacher teacher = Teacher.builder().id(1L).build();
        when(teacherRepository.findById(teacherId)).thenReturn(Optional.of(teacher));

        when(eventToEventDto.convert(any())).thenReturn(EventDto.builder().id(2L).build());

        EventDto newEvent = teacherService.initNewEvent(teacherId);

        assertEquals(2L, newEvent.getId());
        verify(teacherRepository, times(1)).findById(teacherId);
        verify(eventToEventDto, times(1)).convert(any());
    }

    @Test
    void deleteEvent() {
        Teacher teacher = Teacher.builder().id(teacherId).build();

        Long eventId = 2L;
        Event event = Event.builder().id(eventId).teacher(teacher).build();

        when(eventRepository.findById(eventId)).thenReturn(Optional.of(event));

        Boolean deleteStatus = teacherService.deleteEvent(teacherId, eventId);

        assertTrue(deleteStatus);
        verify(eventRepository, times(1)).findById(eventId);
        verify(eventRepository, times(1)).delete(any());
    }

    @Test
    void deleteEventNotOwner() {
        Teacher teacher = Teacher.builder().id(teacherId).build();

        Long eventId = 2L;
        Event event = Event.builder().id(eventId).teacher(Teacher.builder().id(teacherId + 1).build()).build();

        when(eventRepository.findById(eventId)).thenReturn(Optional.of(event));

        Boolean deleteStatus = teacherService.deleteEvent(teacherId, eventId);

        assertFalse(deleteStatus);
        verify(eventRepository, times(1)).findById(eventId);
        verify(eventRepository, times(0)).delete(any());
    }

    @Test
    void deleteEventNotFound() {

        Long eventId = 2L;

        when(eventRepository.findById(eventId)).thenReturn(Optional.empty());

        Boolean deleteStatus = teacherService.deleteEvent(teacherId, eventId);

        assertFalse(deleteStatus);
        verify(eventRepository, times(1)).findById(eventId);
        verify(eventRepository, times(0)).delete(any());
    }


    @Test
    void listAllClasses() {

        when(classRepository.findAll()).thenReturn(Arrays.asList(
                Class.builder().id(1L).build(),
                Class.builder().id(2L).build()
        ));

        when(classToClassDto.convert(any())).thenReturn(ClassDto.builder().build());

        List<ClassDto> returnedClasses = teacherService.listAllClasses();

        assertEquals(2, returnedClasses.size());
        verify(classRepository, times(1)).findAll();
        verify(classToClassDto, times(2)).convert(any());
    }
}