package com.ediary.services;

import com.ediary.DTO.EventDto;
import com.ediary.domain.*;

import java.util.List;

public interface TeacherService {

    Lesson saveLesson(Lesson lesson);
    Boolean deleteLesson(Long lessonId);
    List<Lesson> listLessons(Long teacherId, Long subjectId);
    List<Lesson> listLessons(Long teacherId);

    Topic saveTopic(Topic topic);
    Boolean deleteTopic(Long topicId);
    List<Topic> listTopics(Long teacherId, Long subjectId);

    Subject saveSubject(Subject subject);
    Boolean deleteSubject(Long subjectId);
    List<Subject> listSubjects(Long teacherId);

    Attendance saveAttendance(Attendance attendance);
    List<Attendance> listAttendances(Long teacherId, Long lessonId);

    Grade saveGrade(Grade grade);
    Boolean deleteGrade(Long gradeId);
    List<Grade> listClassGrades(Long teacherId, Long schoolClassId);
    List<Grade> listStudentGrades(Long teacherId, Long studentId);

    Event saveEvent(Event event);
    Boolean deleteEvent(Long eventId);
    List<EventDto> listEvents(Long teacherId);

    Behavior saveBehavior(Behavior behavior);
    List<Behavior> listBehaviors(Long teacherId, Long studentId);
}
