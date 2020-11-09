package com.ediary.services;

import com.ediary.DTO.*;
import com.ediary.converters.*;
import com.ediary.domain.*;
import com.ediary.domain.security.User;
import com.ediary.domain.timetable.Timetable;
import com.ediary.exceptions.NotFoundException;
import com.ediary.repositories.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class StudentServiceImpl implements StudentService {

    private final TimetableService timetableService;
    private final EventService eventService;

    private final GradeRepository gradeRepository;
    private final AttendanceRepository attendanceRepository;
    private final BehaviorRepository behaviorRepository;
    private final StudentRepository studentRepository;
    private final EventRepository eventRepository;

    private final GradeToGradeDto gradeToGradeDto;
    private final AttendanceToAttendanceDto attendanceToAttendanceDto;
    private final BehaviorToBehaviorDto behaviorToBehaviorDto;
    private final EventToEventDto eventToEventDto;
    private final StudentToStudentDto studentToStudentDto;


    @Override
    public List<GradeDto> listGrades(Long studentId) {
        return gradeRepository.findAllByStudentId(studentId)
                .stream()
                .map(gradeToGradeDto::convert)
                .collect(Collectors.toList());
    }

    @Override
    public List<GradeDto> listGrades(Long studentId, Long subjectId) {
        return gradeRepository.findAllByStudentIdAndSubjectId(studentId, subjectId)
                .stream()
                .map(gradeToGradeDto::convert)
                .collect(Collectors.toList());
    }

    @Override
    public List<AttendanceDto> listAttendances(Long studentId) {
        return attendanceRepository.findAllByStudentId(studentId)
                .stream()
                .map(attendanceToAttendanceDto::convert)
                .collect(Collectors.toList());
    }

    @Override
    public List<BehaviorDto> listBehaviors(Long studentId) {
        return behaviorRepository.findAllByStudentId(studentId)
                .stream()
                .map(behaviorToBehaviorDto::convert)
                .collect(Collectors.toList());
    }

    @Override
    public List<EventDto> listEvents(Long studentId, Integer page, Integer size) {

        Student student = getStudentById(studentId);

        Pageable pageable = PageRequest.of(page, size);

        return eventRepository.findAllBySchoolClassIdOrderByDate(student.getSchoolClass().getId(), pageable)
                .stream()
                .map(eventToEventDto::convert)
                .collect(Collectors.toList());
    }

    @Override
    public Timetable getTimetableByStudentId(Long studentId) {

        Student student = getStudentById(studentId);

        return timetableService.getTimetableByClassId(student.getSchoolClass().getId());
    }

    @Override
    public StudentDto findByUser(User user) {
        Optional<Student> studentOptional = studentRepository.findByUser(user);
        if (!studentOptional.isPresent()) {
            throw new NotFoundException("Student Not Found");
        }
        return studentToStudentDto.convert(studentOptional.get());
    }

    @Override
    public Map<Integer, String> getDayNames() {
        return new HashMap<>(){{
            put(0, "Niedziela");
            put(1, "Poniedziałek");
            put(2, "Wtorek");
            put(3, "Środa");
            put(4, "Czwartek");
            put(5, "Piątek");
            put(6, "Sobota");
        }};
    }

    @Override
    public Map<Integer, String> getMonthsNames() {
        return new HashMap<>(){{
            put(0, "Styczeń");
            put(1, "Luty");
            put(2, "Marzec");
            put(3, "Kwiecień");
            put(4, "Maj");
            put(5, "Czerwiec");
            put(6, "Lipiec");
            put(7, "Sierpień");
            put(8, "Wrzesień");
            put(9, "Październik");
            put(10, "Listopad");
            put(11, "Grudzień");
        }};
    }

    private Student getStudentById(Long studentId) {
        Optional<Student> studentOptional = studentRepository.findById(studentId);

        if(!studentOptional.isPresent()) {
            throw new NotFoundException("Student Not Found.");
        }

        return studentOptional.get();
    }


}
