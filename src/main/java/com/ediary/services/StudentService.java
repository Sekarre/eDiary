package com.ediary.services;

import com.ediary.DTO.*;
import com.ediary.domain.security.User;
import com.ediary.domain.timetable.Timetable;

import java.util.List;

public interface StudentService {

    List<GradeDto> listGrades(Long studentId);                 //Wszystkie oceny
    List<GradeDto> listGrades(Long studentId, Long subjectId); //Oceny wedlug przedmiotu
    List<AttendanceDto> listAttendances(Long studentId);
    List<BehaviorDto> listBehaviors(Long studentId);
    List<EventDto> listEvents(Long studentId);
    Timetable getTimetableByStudentId(Long studentId);
    StudentDto findByUser(User user);
}
