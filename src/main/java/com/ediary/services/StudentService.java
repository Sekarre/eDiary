package com.ediary.services;

import com.ediary.DTO.*;
import com.ediary.domain.security.User;
import com.ediary.domain.timetable.Timetable;

import java.util.List;
import java.util.Map;

public interface StudentService {

    List<GradeDto> listGrades(Long studentId);                 //Wszystkie oceny
    List<GradeDto> listGrades(Long studentId, Long subjectId); //Oceny wedlug przedmiotu
    List<SubjectDto> listSubjects(Long studentId);
    List<AttendanceDto> listAttendances(Long studentId);
    List<BehaviorDto> listBehaviors(Long studentId);
    List<EventDto> listEvents(Long studentId, Integer page, Integer size);
    Timetable getTimetableByStudentId(Long studentId);
    StudentDto findByUser(User user);

    Map<Integer, String> getDayNames();
    Map<Integer, String> getMonthsNames();
}
