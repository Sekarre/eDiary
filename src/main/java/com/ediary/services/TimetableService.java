package com.ediary.services;

import com.ediary.domain.timetable.Timetable;

public interface TimetableService {

    Timetable getTimetableByClassId(Long classtId);
}
