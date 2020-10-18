package com.ediary.services;

import com.ediary.DTO.AttendanceDto;
import com.ediary.DTO.GradeDto;
import com.ediary.domain.Behavior;
import com.ediary.domain.Event;
import com.ediary.domain.timetable.Timetable;

import java.util.List;

public interface StudentService {

    List<GradeDto> listGrades(Long studentId);                 //Wszystkie oceny
    List<GradeDto> listGrades(Long studentId, Long subjectId); //Oceny wedlug przedmiotu
    List<AttendanceDto> listAttendances(Long studentId);
    List<Behavior> listBehaviors(Long studentId);
    List<Event> listEvents(Long studentId);
    Timetable getTimetableByStudentId(Long studentId);
}
