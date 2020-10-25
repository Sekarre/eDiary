package com.ediary.repositories;

import com.ediary.domain.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LessonRepository extends JpaRepository<Lesson, Long> {
    List<Lesson> findAllBySubjectId(Long subjectId);
    List<Lesson> findAllBySubjectIdAndSchoolClassId(Long subjectId, Long classId);
    List<Lesson> findAllBySchoolClassId(Long classId);
}
