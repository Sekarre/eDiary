package com.ediary.services;

import com.ediary.domain.Attendance;
import com.ediary.domain.Behavior;
import com.ediary.domain.Event;
import com.ediary.domain.Grade;
import com.ediary.repositories.AttendanceRepository;
import com.ediary.repositories.BehaviorRepository;
import com.ediary.repositories.GradeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentServiceImpl implements StudentService {

    private final GradeRepository gradeRepository;
    private final AttendanceRepository attendanceRepository;
    private final BehaviorRepository behaviorRepository;

    public StudentServiceImpl(GradeRepository gradeRepository, AttendanceRepository attendanceRepository, BehaviorRepository behaviorRepository) {
        this.gradeRepository = gradeRepository;
        this.attendanceRepository = attendanceRepository;
        this.behaviorRepository = behaviorRepository;
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
        return null;
    }
}
