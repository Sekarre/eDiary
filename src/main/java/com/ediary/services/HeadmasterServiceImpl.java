package com.ediary.services;

import com.ediary.DTO.GradeDto;
import com.ediary.DTO.StudentDto;
import com.ediary.DTO.TeacherDto;
import com.ediary.converters.TeacherToTeacherDto;
import com.ediary.domain.*;
import com.ediary.domain.Class;
import com.ediary.domain.helpers.GradeWeight;
import com.ediary.domain.helpers.TimeInterval;
import com.ediary.exceptions.NotFoundException;
import com.ediary.repositories.*;
import com.ediary.services.pdf.PdfService;
import com.mysql.cj.protocol.a.NativeUtils;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.connector.Response;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class HeadmasterServiceImpl implements HeadmasterService {

    private final TeacherRepository teacherRepository;
    private final LessonRepository lessonRepository;
    private final GradeRepository gradeRepository;
    private final EventRepository eventRepository;
    private final ClassRepository classRepository;
    private final StudentRepository studentRepository;
    private final SubjectRepository subjectRepository;
    private final ExtenuationRepository extenuationRepository;
    private final EndYearReportRepository endYearReportRepository;
    private final SchoolPeriodRepository schoolPeriodRepository;
    private final StudentCouncilRepository studentCouncilRepository;
    private final ParentCouncilRepository parentCouncilRepository;
    private final TopicRepository topicRepository;
    private final ParentRepository parentRepository;
    private final AttendanceRepository attendanceRepository;
    private final BehaviorRepository behaviorRepository;
    private final NoticeRepository noticeRepository;

    private final TeacherToTeacherDto teacherToTeacherDto;

    private final PdfService pdfService;

    @Override
    public Report saveReport(Report report) {
        return null;
    }

    @Override
    public List<TeacherDto> listAllTeachers(Integer page, Integer size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "User.lastName"));

        Page<Teacher> teachers = teacherRepository.findAll(pageable);

        return teachers.stream()
                .map(teacherToTeacherDto::convert)
                .collect(Collectors.toList());
    }

    @Override
    public TimeInterval initNewTimeInterval() {
        return TimeInterval.builder()
                .startTime(Date.valueOf(LocalDate.now().minusYears(1)))
                .endTime(Date.valueOf(LocalDate.now()))
                .build();
    }

    @Override
    public TimeInterval setTimeInterval(LocalDate startTime, LocalDate endTime) {
        return TimeInterval.builder()
                .startTime(Date.valueOf(startTime))
                .endTime(Date.valueOf(endTime))
                .build();
    }

    @Override
    public Boolean createTeacherReport(HttpServletResponse response, Long teacherId, Date startTime, Date endTime) throws Exception {

        if (response == null) {
            return false;
        }

        if (startTime.toLocalDate().isAfter(endTime.toLocalDate())) {
            return false;
        }

        Teacher teacher = teacherRepository
                .findById(teacherId).orElseThrow(() -> new NotFoundException("Teacher not found"));

        response.setContentType("application/pdf");
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=nauczyciel_raport_" + System.currentTimeMillis() + ".pdf";
        response.setHeader(headerKey, headerValue);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");

        String timeInterval = simpleDateFormat.format(startTime) + " - " + simpleDateFormat.format(endTime);

        LocalDate localStartTime = startTime.toLocalDate();

        java.util.Date startOfDayDate;


        //+1 day, cuz dateBefore in repos doesnt count that day in select query
        //simpler was '<', now is '<='
        Date correctedEndTime = Date.valueOf(endTime.toLocalDate().plusDays(1));

        //same here
        startOfDayDate = new java.util.Date(Timestamp.valueOf(LocalDateTime.of(localStartTime, LocalTime.MIDNIGHT)).getTime());


        return pdfService.createReportPdf(response, teacher, timeInterval,
                getTeacherLessonsNumber(teacher, startOfDayDate, correctedEndTime).intValue(),
                getTeacherSubjectsNames(teacher), getTeacherGradesNumber(teacher, startOfDayDate, correctedEndTime),
                getTeacherEventsNumber(teacher, startOfDayDate, correctedEndTime));
    }

    @Override
    public Boolean savePdfToDatabaseTest() {

        createEndYearReport(studentRepository.findAll().get(0));

        return true;
    }

    @Override
    public void getPdf(HttpServletResponse response) throws Exception {
        OutputStream out = response.getOutputStream();
        response.setContentType("application/pdf");
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=raport_koncowy" + System.currentTimeMillis() + ".pdf";
        response.setHeader(headerKey, headerValue);

    //todo
        out.write(endYearReportRepository.findAll().get(0).getEndYearPdf());
        out.close();
    }

    private Boolean createEndYearReport(Student student) {
        byte[] endYearReportInBytes = pdfService.createEndYearReport(listSubjectsGrades(student.getId()),
                listSubjectsFinalGrades(student.getId()),
                student, getAttendancesNumber(student.getId()),
                getBehaviorGrade(student.getId()));

        EndYearReport endYearReport = EndYearReport.builder().endYearPdf(endYearReportInBytes).build();

        if (endYearReportInBytes.length != 0) {

            endYearReportRepository.save(endYearReport);
            return true;
        }

        return false;
    }




    private Map<Subject, List<Grade>> listSubjectsGrades(Long studentId) {
        Student student = studentRepository.findById(studentId).orElse(null);

        if (student == null) {
            return null;
        }


        Map<Subject, List<Grade>> gradesWithSubjects = new HashMap<>();


        student.getSchoolClass().getSubjects()
                .forEach(subject -> gradesWithSubjects.put(subject,
                        gradeRepository.findAllByStudentIdAndSubjectId(studentId, subject.getId())));


        if (gradesWithSubjects.keySet().isEmpty()) {
            return null;
        }

        return gradesWithSubjects;
    }


    private Map<Long, Grade> listSubjectsFinalGrades(Long studentId) {
        Student student = studentRepository.findById(studentId).orElse(null);

        if (student == null) {
            return null;
        }

        Map<Long, Grade> studentFinalGradesListMap = new LinkedHashMap<>();

        student.getSchoolClass().getSubjects()
                .forEach(subject -> studentFinalGradesListMap.put(subject.getId(),
                        gradeRepository.findBySubjectIdAndStudentIdAndWeight(
                                subject.getId(), student.getId(), GradeWeight.FINAL_GRADE.getWeight())));


        if (studentFinalGradesListMap.keySet().isEmpty()) {
            return null;
        }

        return studentFinalGradesListMap;
    }

    private Map<String, Long> getAttendancesNumber(Long studentId) {
        Student student = studentRepository.findById(studentId).orElse(null);

        if (student == null) {
            return null;
        }

        Map<String, Long> attendancesNumber = new HashMap<>();

        List<Attendance> attendanceList = student.getAttendance()
                .stream()
                .filter(attendance -> attendance.getStatus().equals(Attendance.Status.ABSENT)
                        || attendance.getStatus().equals(Attendance.Status.UNEXCUSED)
                        || attendance.getStatus().equals(Attendance.Status.EXCUSED))
                .collect(Collectors.toList());

        Long excusedAttendances = attendanceList
                .stream()
                .filter(attendance -> attendance.getStatus().equals(Attendance.Status.EXCUSED))
                .count();


        attendancesNumber.put("total", (long) attendanceList.size());
        attendancesNumber.put("excused", excusedAttendances);

        return attendancesNumber;
    }


    private String getBehaviorGrade(Long studentId) {
        Grade behaviorGrade = gradeRepository.findByStudentIdAndWeight(studentId, GradeWeight.BEHAVIOR_GRADE.getWeight());
        String behaviorGradeValue = "nie wystawiono";
        if (behaviorGrade != null && behaviorGrade.getValue() != null) {
            behaviorGradeValue = behaviorGrade.getValue();
        }

        return behaviorGradeValue;
    }

    private String changeSchoolClassName(String currentClassName) {

        StringBuilder newClassName = new StringBuilder(currentClassName);
        Integer classNumber = 1;


        if (currentClassName.length() > 5) {
            StringBuilder addition = new StringBuilder(newClassName.substring(currentClassName.length() - 5, currentClassName.length() - 1));

            try {
                classNumber = (int) addition.charAt(addition.length() - 1);
            } catch (Exception exception) {
                return null;
            }

            addition.deleteCharAt(addition.length() - 1).append(classNumber.toString());

            return newClassName.replace(currentClassName.length() - 5,
                    currentClassName.length() - 1, addition.toString()).toString();
        }


        return newClassName.append(" ").append(classNumber.toString()).toString();
    }

    private Long getTeacherLessonsNumber(Teacher teacher, java.util.Date startTime, Date endTime) {
        Long[] sumOfLessons = {0L};

        List<List<Lesson>> lessons = teacher.getSubjects().
                stream()
                .map(subject ->
                        lessonRepository.findAllBySubjectAndDateAfterAndDateBefore(subject, startTime, endTime))
                .collect(Collectors.toList());

        lessons.forEach(lessonList -> sumOfLessons[0] += lessonList.stream().count());

        return sumOfLessons[0];
    }

    private String getTeacherSubjectsNames(Teacher teacher) {

        StringBuilder subjectsNames = new StringBuilder();

        teacher.getSubjects()
                .forEach(subject -> subjectsNames.append(subject.getName()).append(", "));

        if (subjectsNames.length() != 0) {
            subjectsNames.delete(subjectsNames.length() - 2, subjectsNames.length() - 1);
        }

        return subjectsNames.toString();
    }

    private Long getTeacherGradesNumber(Teacher teacher, java.util.Date startTime, Date endTime) {
        return gradeRepository.findAllByTeacherIdAndDateAfterAndDateBefore(teacher.getId(), startTime, endTime)
                .stream().count();
    }

    private Long getTeacherEventsNumber(Teacher teacher, java.util.Date startTime, Date endTime) {
        return eventRepository.findAllByTeacherIdAndDateAfterAndDateBefore(teacher.getId(), startTime, endTime)
                .stream().count();
    }

    @Override
    public Boolean performYearClosing() {

        //GENERATING REPORT

        //Classes
        List<Class> classes = classRepository.findAll();
        if (classes != null) {
            classes.forEach(schoolClass -> deleteClass(schoolClass));
        }

        clearTeacher();
        clearParent();
        clearStudents();

        //StudentCouncil
        studentCouncilRepository.deleteAll();

        //ParentCouncil
        parentCouncilRepository.deleteAll();

        //Events
        List<Event> events = eventRepository.findAll();
        if (events != null) {
            eventRepository.deleteAll(events);
        }

        //Extenuations
        List<Extenuation> extenuations = extenuationRepository.findAll();
        if (extenuations != null) {
            extenuations.forEach(extenuation -> deleteExtenuation(extenuation));
        }

        //Attendance
        List<Attendance> attendances = attendanceRepository.findAll();
        if (attendances != null) {
            attendances.forEach(attendance -> deleteAttendance(attendance));
        }

        //Behaviors
        List<Behavior> behaviors = behaviorRepository.findAll();
        if (behaviors != null) {
            behaviorRepository.deleteAll(behaviors);
        }

        //Grade
        List<Grade> grades = gradeRepository.findAll();
        if (grades != null) {
            gradeRepository.deleteAll(grades);
        }

        //Lesson
        List<Lesson> lessons = lessonRepository.findAll();
        if (lessons != null) {
            lessonRepository.deleteAll(lessons);
        }

        //SchoolPerion
        List<SchoolPeriod> schoolPeriods = schoolPeriodRepository.findAll();
        if (schoolPeriods != null) {
            schoolPeriodRepository.deleteAll(schoolPeriods);
        }

        //Topics
        List<Topic> topics = topicRepository.findAll();
        if (topics != null) {
            topics.forEach(topic -> deleteTopic(topic));
        }

        //Subjects
        List<Subject> subjects = subjectRepository.findAll();
        if (subjects != null) {
            subjects.forEach(subject -> deleteSubject(subject));
        }

        //Notices
        List<Notice> notices = noticeRepository.findAll();
        if (notices != null) {
            noticeRepository.deleteAll(notices);
        }

        return true;

    }

    public Boolean deleteClass(Class schoolClass ) {

        //Students - null
        List<Student> students = studentRepository.findAllById(schoolClass.getStudents()
                .stream()
                .map(Student::getId)
                .collect(Collectors.toList()));
        students.forEach(s -> s.setSchoolClass(null));
        studentRepository.saveAll(students);


        //Teacher - null
        Teacher teacher = teacherRepository.findById(schoolClass.getTeacher().getId()).orElse(null);

        if (teacher != null) {
            teacher.setSchoolClass(null);
            teacherRepository.save(teacher);
        }

        //Lessons
        List<Lesson> lessons = lessonRepository.findAllBySchoolClassId(schoolClass.getId());
        lessons.forEach(l -> l.setSchoolClass(null));
        lessonRepository.saveAll(lessons);

        //School periods
        List<SchoolPeriod> schoolPeriods = schoolPeriodRepository.findAllBySchoolClassId(schoolClass.getId());
        schoolPeriods.forEach(s -> s.setSchoolClass(null));
        schoolPeriodRepository.saveAll(schoolPeriods);

        //StudentCouncil - delete
        StudentCouncil studentCouncil = studentCouncilRepository.findBySchoolClassId(schoolClass.getId());
        if (studentCouncil != null) {
            studentCouncil.setStudents(null);
            studentCouncil.setSchoolClass(null);
            studentCouncilRepository.save(studentCouncil);
        }

        //ParentCouncil - delete
        ParentCouncil parentCouncil = parentCouncilRepository.findBySchoolClassId(schoolClass.getId());
        if (parentCouncil != null) {
            parentCouncil.setParents(null);
            parentCouncil.setSchoolClass(null);
            parentCouncilRepository.save(parentCouncil);
        }

        //Events - null
        List<Event> events = eventRepository.findAllBySchoolClassId(schoolClass.getId());
        events.forEach(event -> event.setSchoolClass(null));
        eventRepository.saveAll(events);

        //Subjects - null
        List<Subject> subjects = subjectRepository.findAllBySchoolClassId(schoolClass.getId());
        subjects.forEach(subject -> subject.setSchoolClass(null));
        subjectRepository.saveAll(subjects);

        schoolClass.setName(null);

        classRepository.delete(schoolClass);

        return true;
    }

    public Boolean deleteTopic(Topic topic) {
        topic.setSubject(null);
        topicRepository.save(topic);
        topicRepository.delete(topic);
        return true;
    }

    public Boolean deleteExtenuation(Extenuation extenuation) {
        extenuation.setAttendances(null);
        extenuationRepository.save(extenuation);
        extenuationRepository.delete(extenuation);
        return true;
    }

    public Boolean deleteAttendance(Attendance attendance) {
        attendance.setLesson(null);
        attendanceRepository.save(attendance);
        attendanceRepository.delete(attendance);
        return true;
    }

    public Boolean deleteSubject(Subject subject) {
        subject.setTopics(null);
        subject.setLessons(null);
        subjectRepository.save(subject);
        subjectRepository.delete(subject);
        return true;
    }

    public Boolean clearTeacher() {
        List<Teacher> teachers = teacherRepository.findAll();
        if (teachers != null) {
            for (Teacher teacher : teachers) {
                teacher.setGrades(null);
                teacher.setSubjects(null);
                teacher.setSchoolPeriods(null);
                teacher.setStudentCards(null);
                teacher.setBehaviors(null);
                teacher.setEvents(null);
                teacherRepository.save(teacher);
            }
        }
        return true;
    }

    public Boolean clearParent() {
        List<Parent> parents = parentRepository.findAll();
        if (parents != null) {
            for (Parent parent : parents) {
                parent.setParentCouncils(null);
                parent.setExtenuations(null);
                parentRepository.save(parent);
            }
        }
        return true;
    }

    public Boolean clearStudents() {
        List<Student> students = studentRepository.findAll();
        if (students != null) {
            for (Student student : students) {
                student.setStudentCouncils(null);
                student.setAttendance(null);
                student.setGrades(null);
                student.setBehaviors(null);
                studentRepository.save(student);
            }
        }
        return true;
    }

}
