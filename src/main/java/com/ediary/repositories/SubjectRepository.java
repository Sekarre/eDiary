package com.ediary.repositories;

import com.ediary.domain.Subject;
import com.ediary.domain.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SubjectRepository extends JpaRepository<Subject, Long> {
    Subject findByName(String name);
    List<Subject> findAllByTeacher(Teacher teacher);
}
