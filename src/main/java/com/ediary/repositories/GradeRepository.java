package com.ediary.repositories;

import com.ediary.domain.Grade;
import com.ediary.domain.Subject;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface GradeRepository extends JpaRepository<Grade, Long> {

    List<Grade> findAllByStudentId(Long studentId);
    List<Grade> findAllByStudentIdAndDate(Long studentId, Date date);
    List<Grade> findAllByStudentIdAndSubjectId(Long studentId, Long subjectId);
    List<Grade> findAllByTeacherIdAndSubjectIdAndStudentId(Long teacherId, Long subjectId, Long studentId);
    List<Grade> findAllByStudentIdAndSubjectIdAndDateAfterAndDateBefore(Long studentId, Long subjectId,
                                                                        java.util.Date startDate, java.util.Date endDate);
    List<Grade> findAllByTeacherIdAndDateAfterAndDateBefore(Long teacherId, Date startTime, Date endTime);
    List<Grade> findAllBySubjectIdAndTeacherId(Long subjectId, Long teacherId);
    List<Grade> findAllByTeacherIdAndWeight(Long teacherId, Integer weight);
    List<Grade> findAllBySubject(Subject subject);
    List<Grade> findAllByTeacherIdAndDate(Long teacherId, Date date);

    Long countAllByStudentIdAndDate(Long studentId, Date date);
    Long countAllByTeacherIdAndSubjectIdAndStudentId(Long teacherId, Long subjectId, Long studentId);
}
