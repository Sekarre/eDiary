package com.ediary.services;

import com.ediary.DTO.*;
import com.ediary.converters.*;
import com.ediary.domain.*;
import com.ediary.exceptions.NotFoundException;
import com.ediary.repositories.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class TeacherServiceImpl implements TeacherService {

    private final EventService eventService;

    private final TeacherRepository teacherRepository;
    private final ClassRepository classRepository;
    private final EventRepository eventRepository;
    private final BehaviorRepository behaviorRepository;
    private final LessonRepository lessonRepository;
    private final SubjectRepository subjectRepository;
    private final GradeRepository gradeRepository;

    private final EventToEventDto eventToEventDto;
    private final EventDtoToEvent eventDtoToEvent;
    private final ClassToClassDto classToClassDto;
    private final BehaviorToBehaviorDto behaviorToBehaviorDto;
    private final BehaviorDtoToBehavior behaviorDtoToBehavior;
    private final LessonToLessonDto lessonToLessonDto;
    private final SubjectToSubjectDto subjectToSubjectDto;
    private final GradeToGradeDto gradeToGradeDto;
    private final GradeDtoToGrade gradeDtoToGrade;


    @Override
    public Lesson saveLesson(Lesson lesson) {
        return null;
    }

    @Override
    public Boolean deleteLesson(Long lessonId) {
        return null;
    }


    @Override
    public List<LessonDto> listLessons(Long teacherId, Long subjectId) {

        Teacher teacher = getTeacherById(teacherId);

        Subject teacherSubject = teacher.getSubjects().stream()
                .filter(subject -> subject.getId().equals(subjectId))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("No such subject for teacher"));


        return new ArrayList<>(lessonRepository.findAllBySubjectId(teacherSubject.getId()))
                .stream()
                .map(lessonToLessonDto::convert)
                .collect(Collectors.toList());
    }

    @Override
    public List<LessonDto> listLessons(Long teacherId) {
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
    public List<SubjectDto> listSubjects(Long teacherId) {

        Teacher teacher = getTeacherById(teacherId);

        return subjectRepository.findAllByTeachers(teacher).stream()
                .map(subjectToSubjectDto::convert)
                .collect(Collectors.toList());
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
    public Grade saveClassGrade(Grade grade) {
        return null;
    }

    @Override
    public Boolean deleteClassGrade(Long gradeId) {
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
    public List<GradeDto> listGradesBySubject(Long teacherId, Long subjectId) {

        Teacher teacher = getTeacherById(teacherId);

        Subject subject = getSubjectById(subjectId);

        try {
            return gradeRepository.findAllBySubjectIdAndTeacherId(subject.getId(), teacher.getId()).stream()
                    .map(gradeToGradeDto::convert)
                    .sorted(Comparator.comparingLong(GradeDto::getId))
                    .collect(Collectors.toList());
        } catch (NullPointerException ex) {
            return null;
        }
    }

    @Override
    public GradeDto updatePutGrade(GradeDto gradeDto) {
        Grade grade = gradeDtoToGrade.convert(gradeDto);

        if (grade != null) {
            return gradeToGradeDto.convert(gradeRepository.save(grade));
        }

        return null;
    }

    @Override
    public GradeDto updatePatchGrade(GradeDto gradeDto) {

        GradeDto grade = gradeToGradeDto.convert(gradeRepository
                .findById(gradeDto.getId()).orElseThrow(() -> new NotFoundException("Grade not found")));


        if (gradeDto.getDescription() != null) {
            grade.setDescription(gradeDto.getDescription());
        }

        if (gradeDto.getValue() != null) {
            grade.setValue(gradeDto.getValue());
        }

        if (gradeDto.getWeight() != null) {
            grade.setWeight(gradeDto.getWeight());
        }

        return gradeToGradeDto.convert(gradeRepository.save(gradeDtoToGrade.convert(grade)));
    }

    @Override
    public Boolean deleteGrade(Long teacherId, Long subjectId, Long gradeId) {
        Grade grade = gradeRepository.findById(gradeId).orElse(null);

        if (grade == null) {
            return false;
        }

        if ((grade.getTeacher().getId().equals(teacherId) && grade.getSubject().getId().equals(subjectId))) {
            gradeRepository.delete(grade);
            return true;
        }

        return false;
    }

    @Override
    public Grade saveGrade(GradeDto grade) {
            return gradeRepository.save(gradeDtoToGrade.convert(grade));
    }

    @Override
    public GradeDto initNewGrade(Long teacherId, Long subjectId) {
        Grade grade = Grade.builder()
                .teacher(getTeacherById(teacherId))
                .subject(getSubjectById(subjectId))
                .build();

        return gradeToGradeDto.convert(grade);
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
        if (!optionalEvent.isPresent()) {
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
    public EventDto updatePatchEvent(EventDto eventUpdated) {

        Optional<Event> eventOptional = eventRepository.findById(eventUpdated.getId());
        if (!eventOptional.isPresent()) {
            throw new NotFoundException("Event Not Found.");
        }

        EventDto event = eventToEventDto.convert(eventOptional.get());

        if (eventUpdated.getDescription() != null) {
            event.setDescription(eventUpdated.getDescription());
        }

        if (eventUpdated.getCreateDate() != null) {
            event.setCreateDate(eventUpdated.getCreateDate());
        }

        if (eventUpdated.getDate() != null) {
            event.setDate(eventUpdated.getDate());
        }

        if (eventUpdated.getType() != null) {
            event.setType(eventUpdated.getType());
        }

        Event savedEvent = eventRepository.save(eventDtoToEvent.convert(event));

        return eventToEventDto.convert(savedEvent);
    }

    @Override
    public EventDto getEvent(Long eventId) {
        Optional<Event> eventOptional = eventRepository.findById(eventId);
        if (!eventOptional.isPresent()) {
            throw new NotFoundException("Event Not Found.");
        }

        return eventToEventDto.convert(eventOptional.get());
    }

    @Override
    public Behavior saveBehavior(BehaviorDto behaviorDto) {
        return behaviorRepository.save(behaviorDtoToBehavior.convert(behaviorDto));
    }

    @Override
    public List<BehaviorDto> listBehaviors(Long teacherId) {

        Teacher teacher = getTeacherById(teacherId);

        return behaviorRepository.findAllByTeacher(teacher)
                .stream()
                .map(behaviorToBehaviorDto::convert)
                .collect(Collectors.toList());
    }

    @Override
    public List<BehaviorDto> listBehaviors(Long teacherId, Long studentId) {
        return null;
    }

    @Override
    public BehaviorDto initNewBehavior(Long teacherId) {
        Teacher teacher = getTeacherById(teacherId);
        Behavior behavior = Behavior.builder().teacher(teacher).build();

        return behaviorToBehaviorDto.convert(behavior);
    }

    @Override
    public Boolean deleteBehavior(Long teacherId, Long behaviorId) {
        Optional<Behavior> behaviorOptional = behaviorRepository.findById(behaviorId);
        if (!behaviorOptional.isPresent()) {
            return false;
        }
        Behavior behavior = behaviorOptional.get();

        if (behavior.getTeacher().getId() != teacherId) {
            return false;
        } else {
            behaviorRepository.delete(behavior);
            return true;
        }
    }

    @Override
    public BehaviorDto updatePutBehavior(BehaviorDto behaviorDto) {
        Behavior behavior = behaviorDtoToBehavior.convert(behaviorDto);
        Behavior savedBehavior = behaviorRepository.save(behavior);

        return behaviorToBehaviorDto.convert(savedBehavior);
    }

    @Override
    public BehaviorDto updatePatchBehavior(BehaviorDto behaviorUpdated) {
        Optional<Behavior> behaviorOptional = behaviorRepository.findById(behaviorUpdated.getId());
        if (!behaviorOptional.isPresent()) {
            throw new NotFoundException("Behavior Not Found.");
        }

        BehaviorDto behavior = behaviorToBehaviorDto.convert(behaviorOptional.get());

        if (behaviorUpdated.getDate() != null) {
            behavior.setDate(behaviorUpdated.getDate());
        }

        if (behaviorUpdated.getContent() != null) {
            behavior.setContent(behaviorUpdated.getContent());
        }

        if (behaviorUpdated.isPositive() == !behavior.isPositive()) {
            behavior.setPositive(behaviorUpdated.isPositive());
        }

        Behavior savedBehavior = behaviorRepository.save(behaviorDtoToBehavior.convert(behavior));

        return behaviorToBehaviorDto.convert(savedBehavior);
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

    private Subject getSubjectById(Long subjectId) {
        return subjectRepository
                .findById(subjectId).orElseThrow(() -> new NotFoundException("Subject not found"));
    }

}
