package com.ediary.repositories;

import com.ediary.domain.Class;
import com.ediary.domain.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeacherRepository extends JpaRepository<Teacher, Long> {
    Teacher findByUserId(Long userId);
    Teacher findBySchoolClasses(Class schoolClass);
}
