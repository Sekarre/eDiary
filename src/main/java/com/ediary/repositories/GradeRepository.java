package com.ediary.repositories;

import com.ediary.domain.Grade;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GradeRepository extends JpaRepository<Grade, Long> {

    List<Grade> findAllByStudentId(Long studentId);
    List<Grade> findAllByStudentIdAndSubjectId(Long studentId, Long subjectId);
    List<Grade> findAllByStudentIdAndSubjectIdAndDateAfterAndDateBefore(Long studentId, Long subjectId,
                                                                        java.util.Date startDate, java.util.Date endDate);
    List<Grade> findAllBySubjectIdAndTeacherId(Long subjectId, Long teacherId);
    List<Grade> findAllByTeacherIdAndWeight(Long teacherId, Integer weight);
}
