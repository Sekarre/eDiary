package com.ediary.services;

import com.ediary.DTO.*;
import com.ediary.converters.*;
import com.ediary.domain.*;
import com.ediary.domain.Class;
import com.ediary.domain.security.User;
import com.ediary.domain.timetable.Timetable;
import com.ediary.exceptions.NoAccessException;
import com.ediary.exceptions.NotFoundException;
import com.ediary.repositories.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;

import javax.persistence.Transient;
import java.util.*;
import java.util.function.Predicate;
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
    private final AttendanceRepository attendanceRepository;
    private final StudentRepository studentRepository;
    private final TopicRepository topicRepository;
    private final SchoolPeriodRepository schoolPeriodRepository;
    private final ExtenuationRepository extenuationRepository;
    private final ParentRepository parentRepository;

    private final EventToEventDto eventToEventDto;
    private final EventDtoToEvent eventDtoToEvent;
    private final ClassToClassDto classToClassDto;
    private final BehaviorToBehaviorDto behaviorToBehaviorDto;
    private final BehaviorDtoToBehavior behaviorDtoToBehavior;
    private final LessonToLessonDto lessonToLessonDto;
    private final LessonDtoToLesson lessonDtoToLesson;
    private final SubjectToSubjectDto subjectToSubjectDto;
    private final SubjectDtoToSubject subjectDtoToSubject;
    private final GradeToGradeDto gradeToGradeDto;
    private final GradeDtoToGrade gradeDtoToGrade;
    private final TeacherToTeacherDto teacherToTeacherDto;
    private final AttendanceDtoToAttendance attendanceDtoToAttendance;
    private final AttendanceToAttendanceDto attendanceToAttendanceDto;
    private final StudentToStudentDto studentToStudentDto;
    private final TopicToTopicDto topicToTopicDto;
    private final TopicDtoToTopic topicDtoToTopic;
    private final ExtenuationToExtenuationDto extenuationToExtenuationDto;
    private final ExtenuationDtoToExtenuation extenuationDtoToExtenuation;

    private final TimetableService timetableService;

    @Override
    public TeacherDto findByUser(User user) {
        Optional<Teacher> teacherOptional = teacherRepository.findByUser(user);
        if (!teacherOptional.isPresent()) {
            throw new NotFoundException("Teacher Not Found.");
        }

        return teacherToTeacherDto.convert(teacherOptional.get());
    }

    @Override
    public List<StudentDto> listLessonStudents(Long teacherId, Long subjectId, Long classId, Long lessonId) {
        Teacher teacher = getTeacherById(teacherId);

        teacher.getSubjects().stream()
                .filter(s -> s.getSchoolClass().getLessons()
                        .stream()
                        .anyMatch(lesson -> lesson.getId().equals(lessonId)))
                .findAny().orElseThrow(() -> new NoAccessException("Class -> Lesson"));

        return studentRepository.findAllBySchoolClassId(classId).stream()
                .map(studentToStudentDto::convert)
                .collect(Collectors.toList());
    }

    @Override
    public LessonDto initNewLesson(Long subjectId) {

        Subject subject = getSubjectById(subjectId);

        Class schoolClass = classRepository.findAllBySubjects(subject)
                .stream()
                .findFirst()
                .orElse(null);

        return lessonToLessonDto.convert(Lesson.builder()
                .subject(subject)
                .schoolClass(schoolClass)
                .build());
    }

    @Override
    public Lesson saveLesson(LessonDto lesson) {
        return lessonRepository.save(lessonDtoToLesson.convert(lesson));
    }

    @Override
    public Boolean deleteLesson(Long lessonId) {
        return null;
    }


    @Override
    public List<LessonDto> listLessons(Integer page, Integer size, Long teacherId, Long subjectId) {

        Pageable pageable = PageRequest.of(page, size);

        Teacher teacher = getTeacherById(teacherId);

        Subject teacherSubject = teacher.getSubjects().stream()
                .filter(subject -> subject.getId().equals(subjectId))
                .findFirst()
                .orElseThrow(() -> new NoAccessException("No such subject for teacher"));


        return lessonRepository.findAllBySubjectIdOrderByDateDesc(teacherSubject.getId(), pageable)
                .stream()
                .map(lessonToLessonDto::convert)
                .collect(Collectors.toList());
    }

    @Override
    public List<LessonDto> listLessons(Long teacherId) {
        return null;
    }

    @Override
    public List<LessonDto> listLessons(Long teacherId, Long subjectId, Long classId) {

        Teacher teacher = getTeacherById(teacherId);

        checkIfTeacherHasSubject(subjectId, teacher);

        checkIfSubjectHasClass(classId, teacher);

        return lessonRepository.findAllBySubjectIdAndSchoolClassId(subjectId, classId).stream()
                .map(lessonToLessonDto::convert)
                .collect(Collectors.toList());
    }

    @Override
    public LessonDto getLesson(Long lessonId) {
        return lessonToLessonDto.convert(lessonRepository
                .findById(lessonId).orElseThrow(() -> new NotFoundException("Lesson not found")));
    }

    @Override
    public TopicDto initNewTopic(Long teacherId, Long subjectId) {
        Subject subject = getSubjectById(subjectId);

        final Topic topic = new Topic();
        topic.setSubject(subject);

        Optional<Topic> optionalTopic = topicRepository.findBySubjectOrderByNumberDesc(subject);
        if (optionalTopic.isPresent()) {
            topic.setNumber(optionalTopic.get().getNumber() + 1);
        }

        return topicToTopicDto.convert(topic);
    }

    @Override
    public Topic updateTopic(TopicDto topicDto) {
        return topicRepository.save(topicDtoToTopic.convert(topicDto));
    }

    @Override
    public Topic saveTopic(TopicDto topicDto) {
        return topicRepository.save(topicDtoToTopic.convertForSave(topicDto));
    }

    @Override
    @Transactional
    public Boolean deleteTopic(Long teacherId, Long subjectId, Long topicId) {

        Optional<Topic> topicOptional = topicRepository.findById(topicId);

        if (!topicOptional.isPresent()) {
            return false;
        }

        Topic topic = topicOptional.get();
        if (topic.getSubject().getId().equals(subjectId) && topic.getSubject().getTeacher().getId().equals(teacherId)) {

            // Nullable in Lessons
            List<Lesson> lessons = lessonRepository.findAllByTopic(topic);
            lessons.forEach(lesson -> lesson.setTopic(null));
            lessonRepository.saveAll(lessons);

            topicRepository.delete(topic);
            return true;
        }

        return false;
    }

    @Override
    public List<TopicDto> listTopics(Long teacherId, Long subjectId) {
        Subject subject = getSubjectById(subjectId);

        return topicRepository.findAllBySubjectOrderByNumber(subject)
                .stream()
                .map(topicToTopicDto::convert)
                .collect(Collectors.toList());
    }

    @Override
    public TopicDto updatePatchTopic(TopicDto topicUpdated) {
        Optional<Topic> topicOptional = topicRepository.findById(topicUpdated.getId());
        if (!topicOptional.isPresent()) {
            throw new NotFoundException("Topic Not Found.");
        }

        TopicDto topic = topicToTopicDto.convert(topicOptional.get());

        if(topicUpdated.getNumber() != null) {
            topic.setNumber(topicUpdated.getNumber());
        }

        if (topicUpdated.getName() != null) {
            topic.setName(topicUpdated.getName());
        }

        if (topicUpdated.getDescription() != null) {
            topic.setDescription(topicUpdated.getDescription());
        }


        Topic savedTopic = topicRepository.save(topicDtoToTopic.convert(topic));

        return topicToTopicDto.convert(savedTopic);
    }

    @Override
    public SubjectDto getSubjectById(Long teacherId, Long subjectId) {
        Teacher teacher = getTeacherById(teacherId);
        Subject subject = getSubjectById(subjectId);

        if (subject.getTeacher().getId() == teacher.getId()) {
            return subjectToSubjectDto.convert(subject);
        } else {
            throw new NoAccessException("Teacher -> Subject");
        }
    }

    @Override
    public Subject saveOrUpdateSubject(SubjectDto subject) {
        return subjectRepository.save(subjectDtoToSubject.convert(subject));
    }

    @Override
    @Transactional
    public Boolean deleteSubject(Long teacherId, Long subjectId) {
        Optional<Subject> subjectOptional = subjectRepository.findById(subjectId);
        if (!subjectOptional.isPresent()) {
            return false;
        }
        Subject subject = subjectOptional.get();

        if (!subject.getTeacher().getId().equals(teacherId)) {
            return false;
        } else {
            // Nullable in Topics
            List<Topic> topics = topicRepository.findAllBySubjectOrderByNumber(subject);
            topics.forEach(topic -> topic.setSubject(null));
            topicRepository.saveAll(topics);

            // Nullable in Grades
            List<Grade> grades = gradeRepository.findAllBySubject(subject);
            grades.forEach(grade -> grade.setSubject(null));
            gradeRepository.saveAll(grades);

            // Nullable in SchoolPeriods
            List<SchoolPeriod> schoolPeriods = schoolPeriodRepository.findAllBySubject(subject);
            schoolPeriods.forEach(schoolPeriod -> schoolPeriod.setSubject(null));
            schoolPeriodRepository.saveAll(schoolPeriods);

            // Nullable in Lessons
            List<Lesson> lessons = lessonRepository.findAllBySubject(subject);
            lessons.forEach(lesson -> lesson.setSubject(null));
            lessonRepository.saveAll(lessons);

            subjectRepository.delete(subject);
            return true;
        }
    }

    @Override
    public List<SubjectDto> listSubjects(Long teacherId) {

        Teacher teacher = getTeacherById(teacherId);

        return subjectRepository.findAllByTeacher(teacher).stream()
                .map(subjectToSubjectDto::convert)
                .collect(Collectors.toList());
    }

    @Override
    public SubjectDto initNewSubject(Long teacherId) {
        Teacher teacher = getTeacherById(teacherId);
        return subjectToSubjectDto.convert(Subject.builder().teacher(teacher).build());
    }

    @Override
    public SubjectDto updatePatchSubject(SubjectDto subjectUpdated) {
        Optional<Subject> subjectOptional = subjectRepository.findById(subjectUpdated.getId());
        if (!subjectOptional.isPresent()) {
            throw new NotFoundException("Subject Not Found.");
        }

        SubjectDto subject = subjectToSubjectDto.convert(subjectOptional.get());

        if (subjectUpdated.getName() != null) {
            subject.setName(subjectUpdated.getName());
        }

        if (subjectUpdated.getClassId() != subject.getClassId()) {
            subject.setClassId(subjectUpdated.getClassId());
        }

        Subject savedSubject = subjectRepository.save(subjectDtoToSubject.convert(subject));

        return subjectToSubjectDto.convert(savedSubject);
    }

    @Override
    public Attendance saveAttendance(AttendanceDto attendance) {

        Attendance existingAtt = attendanceRepository.findDistinctByStudentIdAndLessonId(attendance.getStudentId(),
                attendance.getLessonId());

        if (existingAtt != null) {
            existingAtt.setStatus(attendance.getStatus());
            return attendanceRepository.save(existingAtt);
        }

        return attendanceRepository.save(attendanceDtoToAttendance.convert(attendance));
    }


    @Override
    public List<AttendanceDto> listAttendances(Long teacherId, Long subjectId, Long classId, Long lessonId) {
        Teacher teacher = getTeacherById(teacherId);

        teacher.getSubjects().stream()
                .filter(s -> s.getSchoolClass().getLessons()
                        .stream()
                        .anyMatch(lesson -> lesson.getId().equals(lessonId)))
                .findAny().orElseThrow(() -> new NoAccessException("Class -> Lesson"));

        return attendanceRepository.findAllByLesson_Id(lessonId).stream()
                .map(attendanceToAttendanceDto::convert)
                .sorted(Comparator.comparingLong(AttendanceDto::getId))
                .collect(Collectors.toList());
    }

    @Override
    public List<AttendanceDto> listAttendancesByStudent(Long studentId) {
        return attendanceRepository.findAllByStudentId(studentId)
                .stream()
                .map(attendanceToAttendanceDto::convert)
                .collect(Collectors.toList());
    }

    @Override
    public AttendanceDto initNewLessonAttendance(Long teacherId, Long subjectId, Long lessonId) {
        return attendanceToAttendanceDto.convert(Attendance.builder()
                .lesson(lessonRepository
                        .findById(lessonId).orElseThrow(() -> new NotFoundException("Lesson not found")))
                .build());
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
    public List<GradeDto> listClassGrades(Long teacherId, Long schoolClassId) {
        return null;
    }

    @Override
    public List<GradeDto> listStudentGrades(Long teacherId, Long studentId) {
        return null;
    }

    @Override
    public Boolean deleteLessonGrade(Long studentId, Long gradeId) {
        Student student = studentRepository.findById(studentId).orElseThrow(() -> new NotFoundException("Student not found"));
        Grade grade = gradeRepository.findById(gradeId).orElse(null);

        if (grade != null && grade.getStudent().equals(student)) {

            //Student - null
            Student gradeStudent = grade.getStudent();
            gradeStudent.setGrades(gradeRepository.findAllByStudentId(studentId)
                    .stream()
                    .filter(grade1 -> !grade1.equals(grade))
                    .collect(Collectors.toSet()));
            studentRepository.save(student);

            //Teacher - null
            Teacher teacher = grade.getTeacher();
            teacher.setGrades(gradeRepository.findAllByTeacherIdAndDate(teacher.getId(), grade.getDate())
                    .stream()
                    .filter(grade1 -> !grade1.equals(grade))
                    .collect(Collectors.toSet()));
            teacherRepository.save(teacher);

            gradeRepository.delete(grade);

            return true;
        }

        return false;
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
    public Grade saveOrUpdateGrade(GradeDto grade) {

        if (grade.getId() != null) {
            Grade gradeToUpdate = gradeRepository.findById(grade.getId()).orElse(null);

            if (gradeToUpdate != null) {
                gradeToUpdate.setWeight(grade.getWeight());
                gradeToUpdate.setValue(grade.getValue());
                gradeToUpdate.setDescription(grade.getDescription());
                return gradeRepository.save(gradeToUpdate);
            }
        }

        Grade savedGrade = gradeRepository.save(gradeDtoToGrade.convert(grade));

        return savedGrade;
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
    public GradeDto initNewLessonGrade(Long teacherId, Long subjectId, Long lessonId) {
        return gradeToGradeDto.convert(Grade.builder()
                .teacher(getTeacherById(teacherId))
                .subject(getSubjectById(subjectId))
                .date(lessonRepository
                        .findById(lessonId).orElseThrow(() -> new NotFoundException("Lesson not found")).getDate())
                .build());
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
    public EventDto initNewClassEvent(Long teacherId, Long classId) {
        Event event = Event.builder()
                .teacher(getTeacherById(teacherId))
                .schoolClass(getClassById(classId))
                .build();

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

        if (!behavior.getTeacher().getId().equals(teacherId)) {
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

    @Override
    public List<ClassDto> listClassesByTeacherAndSubject(Long teacherId, Long subjectId) {
        Teacher teacher = getTeacherById(teacherId);
        checkIfTeacherHasSubject(subjectId, teacher);


        return classRepository.findAllBySubjects(getSubjectById(subjectId)).stream()
                .map(classToClassDto::convert)
                .collect(Collectors.toList());
    }

    @Override
    public ClassDto getSchoolClassByTeacherAndSubject(Long classId, Long subjectId, Long teacherId) {

        Teacher teacher = getTeacherById(teacherId);

        checkIfTeacherHasSubject(subjectId, teacher);

        checkIfSubjectHasClass(classId, teacher);


        return classToClassDto.convert(classRepository
                .findById(classId).orElseThrow(() -> new NotFoundException("Class not found")));
    }


    @Override
    public List<ClassDto> listClassByTeacher(Long teacherId) {
        Teacher teacher = getTeacherById(teacherId);


        return classRepository.findAllBySubjectsIn(teacher.getSubjects())
                .stream()
                .distinct()
                .map(classToClassDto::convert)
                .collect(Collectors.toList());

    }

    @Override
    public List<ExtenuationDto> listExtenuations(Long teacherId) {
        Teacher teacher = getTeacherById(teacherId);

        List<Extenuation> extenuations = new ArrayList<>();

        List<Parent> parents = parentRepository.findAllByStudentsIn(teacher.getSchoolClass().getStudents());

        parents.forEach(parent -> extenuations
                .addAll(extenuationRepository.findAllByParentIdAndStatus(parent.getId(), Extenuation.Status.SENT)));

        return extenuations
                .stream()
                .map(extenuationToExtenuationDto::convert)
                .collect(Collectors.toList());
    }

    @Override
    public Boolean acceptExtenuation(Long extenuationId) {
        Extenuation extenuation = extenuationRepository
                .findById(extenuationId).orElseThrow(() -> new NotFoundException("Extenuation not found"));

        extenuation.getAttendances().forEach(attendance -> attendance.setStatus(Attendance.Status.EXCUSED));
        extenuation.setStatus(Extenuation.Status.ACCEPT);

        extenuationRepository.save(extenuation);

        return true;
    }

    @Override
    public Boolean rejectExtenuation(Long extenuationId) {
        Extenuation extenuation = extenuationRepository
                .findById(extenuationId).orElseThrow(() -> new NotFoundException("Extenuation not found"));

        extenuation.getAttendances().forEach(attendance -> attendance.setStatus(Attendance.Status.UNEXCUSED));
        extenuation.setStatus(Extenuation.Status.REJECT);

        extenuationRepository.save(extenuation);

        return true;
    }

    @Override
    public List<StudentDto> listClassStudents(Long teacherId, Long classId) {
        Teacher teacher = getTeacherById(teacherId);
        Class schoolClass = getClassById(classId);


        return studentRepository.findAllBySchoolClassId(schoolClass.getId())
                .stream()
                .map(studentToStudentDto::convert)
                .collect(Collectors.toList());

    }

    @Override
    public Boolean isFormTutor(Long teacherId, Long classId) {
        Teacher teacher = getTeacherById(teacherId);

        return teacher.getSchoolClass().getId().equals(classId);
    }

    private Teacher getTeacherById(Long teacherId) {
        Optional<Teacher> teacherOptional = teacherRepository.findById(teacherId);

        if (!teacherOptional.isPresent()) {
            throw new NotFoundException("Teacher Not Found.");
        }

        return teacherOptional.get();
    }

    @Override
    public Boolean excuseAttendances(List<Long> ids, Long teacherId, Long studentId, Long classId) {
        Teacher teacher = getTeacherById(teacherId);
        Student student = studentRepository.findById(studentId).orElseThrow(() -> new NotFoundException("Student not found"));

        if (student.getSchoolClass().equals(teacher.getSchoolClass())) {
            List<Attendance> attendances = attendanceRepository.findAllById(ids);
            attendances.forEach(attendance -> attendance.setStatus(Attendance.Status.EXCUSED));
            attendanceRepository.saveAll(attendances);

            return true;
        }

        return false;
    }

    @Override
    public Timetable getTimetableByTeacherId(Long teacherId) {
        Teacher teacher = getTeacherById(teacherId);

        if (teacher.getSubjects() == null) {
            return null;
        }

        return timetableService.getTimetableByTeacherId(teacher.getId());

    }

    @Override
    public Map<StudentDto, List<GradeDto>> listStudentsLessonGrades(Long teacherId, Long lessonId) {
        Lesson lesson = lessonRepository.findById(lessonId).orElseThrow(() -> new NotFoundException("Lesson not found"));

        Class schoolClass = lesson.getSchoolClass();

        Map<StudentDto, List<GradeDto>> studentGradesListMap = new TreeMap<>(
                ((o1, o2) -> Arrays.stream(o1.getUserName().split(" ")).skip(1).findFirst().get()
                                .compareToIgnoreCase(Arrays.stream(o2.getUserName().split(" ")).skip(1).findFirst().get())));

        schoolClass.getStudents().forEach(student -> {
            studentGradesListMap.put(studentToStudentDto.convert(student),
                    gradeRepository.findAllByStudentIdAndDate(student.getId(), lesson.getDate())
                            .stream()
                            .map(gradeToGradeDto::convert)
                            .collect(Collectors.toList()));
        });


        if (studentGradesListMap.keySet().isEmpty())
            return null;

        return studentGradesListMap;
    }


    @Override
    public Map<StudentDto, AttendanceDto> listStudentsLessonAttendances(Long teacherId, Long lessonId) {
        Lesson lesson = lessonRepository.findById(lessonId).orElseThrow(() -> new NotFoundException("Lesson not found"));

        Class schoolClass = lesson.getSchoolClass();

        Map<StudentDto, AttendanceDto> studentAttendancesListMap = new TreeMap<>(
                ((o1, o2) -> Arrays.stream(o1.getUserName().split(" ")).skip(1).findFirst().get()
                        .compareToIgnoreCase(Arrays.stream(o2.getUserName().split(" ")).skip(1).findFirst().get())));

        schoolClass.getStudents().forEach(student -> {
            studentAttendancesListMap.put(
                    studentToStudentDto.convert(student),
                    attendanceToAttendanceDto.convert(attendanceRepository.findDistinctByStudentIdAndLesson(student.getId(), lesson)));
        });


        if (studentAttendancesListMap.keySet().isEmpty())
            return null;

        return studentAttendancesListMap;
    }

    @Override
    public List<Long> maxGradesCount(Long teacherId, Long lessonId) {
        Long[] maxCount = {Long.MIN_VALUE};

        Lesson lesson = lessonRepository.findById(lessonId).orElseThrow(() -> new NotFoundException("Lesson not found"));

        Class schoolClass = lesson.getSchoolClass();

        schoolClass.getStudents().forEach(student -> {
            Long count = gradeRepository.countAllByStudentIdAndDate(student.getId(), lesson.getDate());
            if (count > maxCount[0])
                maxCount[0] = count;
        });

        return new ArrayList<>(){{
            for (int i = 0; i <= maxCount[0]; i++){
                add(null);
            }
        }};
    }

    private Subject getSubjectById(Long subjectId) {
        return subjectRepository
                .findById(subjectId).orElseThrow(() -> new NotFoundException("Subject not found"));
    }

    private Class getClassById(Long classId) {
        return classRepository
                .findById(classId).orElseThrow(() -> new NotFoundException("Class not found"));
    }


    private void checkIfSubjectHasClass(Long classId, Teacher teacher) {
        teacher.getSubjects().stream()
                .filter(s -> s.getSchoolClass().getId().equals(classId))
                .findAny().orElseThrow(() -> new NoAccessException("Subject -> Class"));
    }

    private void checkIfTeacherHasSubject(Long subjectId, Teacher teacher) {
        teacher.getSubjects().stream()
                .filter(s -> s.getId().equals(subjectId))
                .findAny().orElseThrow(() -> new NoAccessException("Teacher -> Subject"));
    }


}
