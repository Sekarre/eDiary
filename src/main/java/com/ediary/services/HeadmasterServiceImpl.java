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


    /*
    @Override
    public Boolean performYearClosing() {

        List<Class> schoolClasses = classRepository.findAll();


        for (Class currentClass : schoolClasses) {
            List<Student> students = studentRepository.findAllBySchoolClassId(currentClass.getId());

            students.forEach(student -> {

                //save endYearReport to db
                createEndYearReport(student);


                //remove behavior grade
                Grade behaviorGrade = gradeRepository.findByStudentIdAndWeight(student.getId(), GradeWeight.BEHAVIOR_GRADE.getWeight());
                if (behaviorGrade != null) {
                    behaviorGrade.setStudent(null);
                    gradeRepository.save(behaviorGrade);
                }

                //removing from class
                student.setSchoolClass(null);
                studentRepository.save(student);

                student.setBehaviors(null);
                studentRepository.save(student);

            });

            List<Subject> subjects = new ArrayList<>(currentClass.getSubjects());

            for (Subject subject : subjects) {

                //delete lessons
                subject.getLessons().forEach(lesson -> {
                    lesson.setTopic(null);
                    lesson.setAttendances(null);
                    lesson.setSchoolClass(null);
                    lesson.setSubject(null);

                    lessonRepository.save(lesson);
                    lessonRepository.delete(lesson);
                });

                //delete subjects
                subjectRepository.delete(subject);
            }

            //change class name
            currentClass.setName(changeSchoolClassName(currentClass.getName()));
            classRepository.save(currentClass);
        }

        return false;
    }

     */

    @Override
    public Boolean savePdfToDatabaseTest() {

        createEndYearReport(studentRepository.findById(90L).orElse(null));

        return true;
    }

    @Override
    public void getPdf(HttpServletResponse response) throws Exception {
        OutputStream out = response.getOutputStream();
        response.setContentType("application/pdf");
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=report_końowy" + System.currentTimeMillis() + ".pdf";
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
}
