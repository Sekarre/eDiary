package com.ediary.services;

import com.ediary.DTO.AttendanceDto;
import com.ediary.DTO.GradeDto;
import com.ediary.converters.AttendanceToAttendanceDto;
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
    public List<Behavior> listBehaviors(Long studentId) {
        return behaviorRepository.findAllByStudentId(studentId);
    }

    @Override
    public List<Event> listEvents(Long studentId) {

        Student student = getStudentById(studentId);

        List<Event> events= eventService.listEventsBySchoolClass(student.getSchoolClass());

        return events;
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
