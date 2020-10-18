package com.ediary.repositories;

import com.ediary.domain.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StudentRepository extends JpaRepository<Student, Long> {
    List<Student> findAllByParentId(Long parentId);
    Student findByUserId(Long userId);
}
