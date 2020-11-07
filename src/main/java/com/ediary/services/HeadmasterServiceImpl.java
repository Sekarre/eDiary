package com.ediary.services;

import com.ediary.DTO.TeacherDto;
import com.ediary.converters.TeacherToTeacherDto;
import com.ediary.domain.*;
import com.ediary.domain.helpers.TimeInterval;
import com.ediary.exceptions.NotFoundException;
import com.ediary.repositories.EventRepository;
import com.ediary.repositories.GradeRepository;
import com.ediary.repositories.LessonRepository;
import com.ediary.repositories.TeacherRepository;
import com.ediary.services.pdf.PdfService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class HeadmasterServiceImpl implements HeadmasterService {

    private final TeacherRepository teacherRepository;
    private final LessonRepository lessonRepository;
    private final GradeRepository gradeRepository;
    private final EventRepository eventRepository;
    private final TeacherToTeacherDto teacherToTeacherDto;

    private final PdfService pdfService;

    @Override
    public Report saveReport(Report report) {
        return null;
    }

    @Override
    public List<TeacherDto> listAllTeachers(Integer page, Integer size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "User.firstName"));

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


        //+1 day, cuz dateBefore in repos doesnt count that day in select query
        //simpler was '<', now is '<='
        Date correctedEndTime = Date.valueOf(endTime.toLocalDate().plusDays(1));

        //same here
        Date correctedStartTime = Date.valueOf(startTime.toLocalDate().minusDays(1));


        return pdfService.createReportPdf(response, teacher, timeInterval,
                getTeacherLessonsNumber(teacher, correctedStartTime, correctedEndTime).intValue(),
                getTeacherSubjectsNames(teacher), getTeacherGradesNumber(teacher, correctedStartTime, correctedEndTime),
                getTeacherEventsNumber(teacher, correctedStartTime, correctedEndTime));
    }

    private Long getTeacherLessonsNumber(Teacher teacher, Date startTime, Date endTime) {
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
        subjectsNames.delete(subjectsNames.length() - 2, subjectsNames.length() - 1);

        return subjectsNames.toString();
    }

    private Long getTeacherGradesNumber(Teacher teacher, Date startTime, Date endTime) {
        return gradeRepository.findAllByTeacherIdAndDateAfterAndDateBefore(teacher.getId(), startTime, endTime)
                .stream().count();
    }

    private Long getTeacherEventsNumber(Teacher teacher, Date startTime, Date endTime) {
        return eventRepository.findAllByTeacherIdAndDateAfterAndDateBefore(teacher.getId(), startTime, endTime)
                .stream().count();
    }
}
