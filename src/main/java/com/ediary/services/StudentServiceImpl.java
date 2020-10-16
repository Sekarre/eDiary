package com.ediary.services;

import com.ediary.domain.*;
import com.ediary.exceptions.NotFoundException;
import com.ediary.repositories.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StudentServiceImpl implements StudentService {

    private final GradeRepository gradeRepository;
    private final AttendanceRepository attendanceRepository;
    private final BehaviorRepository behaviorRepository;
    private final EventRepository eventRepository;
    private final StudentRepository studentRepository;

    public StudentServiceImpl(GradeRepository gradeRepository, AttendanceRepository attendanceRepository,
                              BehaviorRepository behaviorRepository, EventRepository eventRepository,
                              StudentRepository studentRepository) {

        this.gradeRepository = gradeRepository;
        this.attendanceRepository = attendanceRepository;
        this.behaviorRepository = behaviorRepository;
        this.eventRepository = eventRepository;
        this.studentRepository = studentRepository;
    }

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

        Optional<Student> studentOptional = studentRepository.findById(studentId);

        if(!studentOptional.isPresent()) {
            throw new NotFoundException("Student Not Found.");
        }

        Student student = studentOptional.get();

        List<Event> eventList = eventRepository.findAllBySchoolClassId(student.getSchoolClass().getId());

        return eventList;
    }
}
