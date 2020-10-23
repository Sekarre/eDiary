package com.ediary.repositories;

import com.ediary.domain.Class;
import com.ediary.domain.Subject;
import com.ediary.domain.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TeacherRepository extends JpaRepository<Teacher, Long> {
    Teacher findByUserId(Long userId);
    Teacher findBySchoolClass(Class schoolClass);
}
