package com.ediary.services;

import com.ediary.DTO.AttendanceDto;
import com.ediary.DTO.BehaviorDto;
import com.ediary.DTO.EventDto;
import com.ediary.DTO.GradeDto;
import com.ediary.domain.timetable.Timetable;

import java.util.List;

public interface StudentService {

    List<GradeDto> listGrades(Long studentId);                 //Wszystkie oceny
    List<GradeDto> listGrades(Long studentId, Long subjectId); //Oceny wedlug przedmiotu
    List<AttendanceDto> listAttendances(Long studentId);
    List<BehaviorDto> listBehaviors(Long studentId);
    List<EventDto> listEvents(Long studentId);
    Timetable getTimetableByStudentId(Long studentId);
}
