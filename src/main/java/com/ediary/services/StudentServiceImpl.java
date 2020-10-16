package com.ediary.services;

import com.ediary.domain.Attendance;
import com.ediary.domain.Behavior;
import com.ediary.domain.Event;
import com.ediary.domain.Grade;
import com.ediary.repositories.GradeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentServiceImpl implements StudentService {

    private final GradeRepository gradeRepository;

    public StudentServiceImpl(GradeRepository gradeRepository) {
        this.gradeRepository = gradeRepository;
    }

    @Override
    public List<Grade> listGrades(Long studentId) {
        return gradeRepository.findAllByStudentId(studentId);
    }

    @Override
    public List<Grade> listGrades(Long studentId, Long subjectId) {
        return null;
    }

    @Override
    public List<Attendance> listAttendances(Long studentId) {
        return null;
    }

    @Override
    public List<Behavior> listBehaviors(Long studentId) {
        return null;
    }

    @Override
    public List<Event> listEvents(Long studentId) {
        return null;
    }
}
