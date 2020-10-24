package com.ediary.repositories;

import com.ediary.domain.Class;
import com.ediary.domain.Lesson;
import com.ediary.domain.Subject;
import com.ediary.domain.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Set;

public interface ClassRepository extends JpaRepository<Class, Long> {

    Class findByName(String name);

    //Not sure if works
    List<Class> findAllBySubjects(Subject subject);
}
