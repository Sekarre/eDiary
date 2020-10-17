package com.ediary.services;

import com.ediary.domain.*;
import com.ediary.domain.timetable.Timetable;
import com.ediary.exceptions.NotFoundException;
import com.ediary.repositories.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class StudentServiceImpl implements StudentService {

    private final TimetableService timetableService;
    private final EventService eventService;

    private final GradeRepository gradeRepository;
    private final AttendanceRepository attendanceRepository;
    private final BehaviorRepository behaviorRepository;
    private final StudentRepository studentRepository;


    @Override
    public List<Grade> listGrades(Long studentId) {
        return gradeRepository.findAllByStudentId(studentId);
    }

    @Override
    public List<Grade> listGrades(Long studentId, Long subjectId) {
        return gradeRepository.findAllByStudentIdAndSubjectId(studentId, subjectId);
    }

    @Override
    public List<Attendance> listAttendances(Long studentId) {
        return attendanceRepository.findAllByStudentId(studentId);
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
