package com.ediary.repositories;

import com.ediary.domain.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentCard extends JpaRepository<Student, Long> {
}
