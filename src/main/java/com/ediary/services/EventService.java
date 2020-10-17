package com.ediary.services;

import com.ediary.domain.Event;
import com.ediary.domain.Teacher;

import java.util.List;

public interface EventService {

    List<Event> listEventsByTeacher(Teacher teacher);
}
