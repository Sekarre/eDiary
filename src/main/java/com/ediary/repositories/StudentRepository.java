package com.ediary.repositories;

import com.ediary.domain.Student;
import com.ediary.domain.security.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, Long> {
    List<Student> findAllByParentId(Long parentId);
    Student findByUserId(Long userId);
    Optional<Student> findByUser(User user);
}
