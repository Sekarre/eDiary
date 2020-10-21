package com.ediary.services;

import com.ediary.DTO.ClassDto;
import com.ediary.DTO.EventDto;
import com.ediary.converters.ClassToClassDto;
import com.ediary.converters.EventDtoToEvent;
import com.ediary.converters.EventToEventDto;
import com.ediary.domain.*;
import com.ediary.domain.Class;
import com.ediary.exceptions.NotFoundException;
import com.ediary.repositories.ClassRepository;
import com.ediary.repositories.EventRepository;
import com.ediary.repositories.TeacherRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class TeacherServiceImpl implements TeacherService {

    private final EventService eventService;

    private final TeacherRepository teacherRepository;
    private final ClassRepository classRepository;
    private final EventRepository eventRepository;

    private final EventToEventDto eventToEventDto;
    private final EventDtoToEvent eventDtoToEvent;
    private final ClassToClassDto classToClassDto;


    @Override
    public Lesson saveLesson(Lesson lesson) {
        return null;
    }

    @Override
    public Boolean deleteLesson(Long lessonId) {
        return null;
    }

    @Override
    public List<Lesson> listLessons(Long teacherId, Long subjectId) {
        return null;
    }

    @Override
    public List<Lesson> listLessons(Long teacherId) {
        return null;
    }

    @Override
    public Topic saveTopic(Topic topic) {
        return null;
    }

    @Override
    public Boolean deleteTopic(Long topicId) {
        return null;
    }

    @Override
    public List<Topic> listTopics(Long teacherId, Long subjectId) {
        return null;
    }

    @Override
    public Subject saveSubject(Subject subject) {
        return null;
    }

    @Override
    public Boolean deleteSubject(Long subjectId) {
        return null;
    }

    @Override
    public List<Subject> listSubjects(Long teacherId) {
        return null;
    }

    @Override
    public Attendance saveAttendance(Attendance attendance) {
        return null;
    }

    @Override
    public List<Attendance> listAttendances(Long teacherId, Long lessonId) {
        return null;
    }

    @Override
    public Grade saveGrade(Grade grade) {
        return null;
    }

    @Override
    public Boolean deleteGrade(Long gradeId) {
        return null;
    }

    @Override
    public List<Grade> listClassGrades(Long teacherId, Long schoolClassId) {
        return null;
    }

    @Override
    public List<Grade> listStudentGrades(Long teacherId, Long studentId) {
        return null;
    }

    @Override
    public Event saveEvent(EventDto eventDto) {
        return eventRepository.save(eventDtoToEvent.convert(eventDto));
    }

    @Override
    public EventDto initNewEvent(Long teacherId) {
        Teacher teacher = getTeacherById(teacherId);
        Event event = Event.builder().teacher(teacher).build();
        return eventToEventDto.convert(event);
    }

    @Override
    public Boolean deleteEvent(Long teacherId, Long eventId) {
        Optional<Event> optionalEvent = eventRepository.findById(eventId);
        if(!optionalEvent.isPresent()) {
            return false;
        }
        Event event = optionalEvent.get();

        if (event.getTeacher().getId() != teacherId) {
            return false;
        } else {
            eventRepository.delete(event);
            return true;
        }
    }

    @Override
    public List<EventDto> listEvents(Long teacherId) {

        Teacher teacher = getTeacherById(teacherId);

        return eventService.listEventsByTeacher(teacher)
                .stream()
                .map(eventToEventDto::convert)
                .collect(Collectors.toList());
    }

    @Override
    public EventDto updatePutEvent(EventDto eventDto) {
        Event event = eventDtoToEvent.convert(eventDto);
        Event savedEvent = eventRepository.save(event);

        return eventToEventDto.convert(savedEvent);
    }

    @Override
    public EventDto updatePatchEvent(EventDto eventDto) {
        return null;
    }

    @Override
    public Behavior saveBehavior(Behavior behavior) {
        return null;
    }

    @Override
    public List<Behavior> listBehaviors(Long teacherId, Long studentId) {
        return null;
    }

    @Override
    public List<ClassDto> listAllClasses() {
        return classRepository.findAll().stream()
                .map(classToClassDto::convert)
                .collect(Collectors.toList());
    }


    private Teacher getTeacherById(Long teacherId) {
        Optional<Teacher> teacherOptional = teacherRepository.findById(teacherId);

        if (!teacherOptional.isPresent()) {
            throw new NotFoundException("Teacher Not Found.");
        }

        return teacherOptional.get();
    }

}
