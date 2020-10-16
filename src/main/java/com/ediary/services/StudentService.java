package com.ediary.services;

import com.ediary.domain.Attendance;
import com.ediary.domain.Behavior;
import com.ediary.domain.Event;
import com.ediary.domain.Grade;
import com.ediary.domain.timetable.Timetable;

import java.util.List;

public interface StudentService {

    List<Grade> listGrades(Long studentId);                 //Wszystkie oceny
    List<Grade> listGrades(Long studentId, Long subjectId); //Oceny wedlug przedmiotu
    List<Attendance> listAttendances(Long studentId);
    List<Behavior> listBehaviors(Long studentId);
    List<Event> listEvents(Long studentId);
    Timetable getTimetableByStudentId(Long studentId);
}
