package com.ediary.repositories;

import com.ediary.domain.Grade;
import com.ediary.domain.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GradeRepository extends JpaRepository<Grade, Long> {

    List<Grade> findAllByStudentId(Long studentId);
    List<Grade> findAllByStudentIdAndSubjectId(Long studentId, Long subjectId);
}
