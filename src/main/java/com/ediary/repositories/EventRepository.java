package com.ediary.repositories;

import com.ediary.domain.Class;
import com.ediary.domain.Event;
import com.ediary.domain.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {

    List<Event> findAllBySchoolClass(Class schoolClass);
    List<Event> findAllByTeacher(Teacher teacher);
}
