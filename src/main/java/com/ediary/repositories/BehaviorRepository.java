package com.ediary.repositories;

import com.ediary.domain.Behavior;
import com.ediary.domain.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BehaviorRepository extends JpaRepository<Behavior, Long> {

    List<Behavior> findAllByStudentId(Long studentId);
    List<Behavior> findAllByTeacher(Teacher teacher);
}
