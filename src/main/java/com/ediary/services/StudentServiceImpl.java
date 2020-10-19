package com.ediary.services;

import com.ediary.DTO.AttendanceDto;
import com.ediary.DTO.BehaviorDto;
import com.ediary.DTO.EventDto;
import com.ediary.DTO.GradeDto;
import com.ediary.converters.AttendanceToAttendanceDto;
import com.ediary.converters.BehaviorToBehaviorDto;
import com.ediary.converters.EventToEventDto;
import com.ediary.converters.GradeToGradeDto;
import com.ediary.domain.*;
import com.ediary.domain.timetable.Timetable;
import com.ediary.exceptions.NotFoundException;
import com.ediary.repositories.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
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

    private final GradeToGradeDto gradeToGradeDto;
    private final AttendanceToAttendanceDto attendanceToAttendanceDto;
    private final BehaviorToBehaviorDto behaviorToBehaviorDto;
    private final EventToEventDto eventToEventDto;


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
    public List<EventDto> listEvents(Long studentId) {

        Student student = getStudentById(studentId);

        return eventService.listEventsBySchoolClass(student.getSchoolClass())
                .stream()
                .map(eventToEventDto::convert)
                .collect(Collectors.toList());
    }

    @Override
    public Timetable getTimetableByStudentId(Long studentId) {

        Student student = getStudentById(studentId);

        return timetableService.getTimetableByClassId(student.getSchoolClass().getId());
    }

    private Student getStudentById(Long studentId) {
        Optional<Student> studentOptional = studentRepository.findById(studentId);

        if(!studentOptional.isPresent()) {
            throw new NotFoundException("Student Not Found.");
        }

        return studentOptional.get();
    }


}
