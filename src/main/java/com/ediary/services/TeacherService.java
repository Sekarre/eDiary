package com.ediary.services;

import com.ediary.DTO.BehaviorDto;
import com.ediary.DTO.ClassDto;
import com.ediary.DTO.EventDto;
import com.ediary.DTO.LessonDto;
import com.ediary.domain.*;

import java.util.List;

public interface TeacherService {

    Lesson saveLesson(Lesson lesson);
    Boolean deleteLesson(Long lessonId);
    List<LessonDto> listLessons(Long teacherId, Long subjectId);
    List<LessonDto> listLessons(Long teacherId);

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

    Event saveEvent(EventDto eventDto);
    EventDto initNewEvent(Long teacherId);
    Boolean deleteEvent(Long teacherId, Long eventId);
    List<EventDto> listEvents(Long teacherId);
    EventDto updatePutEvent(EventDto eventDto);
    EventDto updatePatchEvent(EventDto eventDto);
    EventDto getEvent(Long eventId);

    Behavior saveBehavior(BehaviorDto behaviorDto);
    List<BehaviorDto> listBehaviors(Long teacherId);
    List<BehaviorDto> listBehaviors(Long teacherId, Long studentId);
    BehaviorDto initNewBehavior(Long teacherId);
    Boolean deleteBehavior(Long teacherId, Long behaviorId);
    BehaviorDto updatePutBehavior(BehaviorDto behaviorDto);
    BehaviorDto updatePatchBehavior(BehaviorDto behaviorDto);


    List<ClassDto> listAllClasses();
}
