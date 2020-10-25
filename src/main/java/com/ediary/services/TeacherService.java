package com.ediary.services;

import com.ediary.DTO.*;
import com.ediary.domain.*;
import com.ediary.domain.security.User;

import java.util.List;

public interface TeacherService {

    TeacherDto findByUser(User user);

    List<StudentDto> listLessonStudents(Long teacherId, Long subjectId, Long classId, Long lessonId);

    LessonDto initNewLesson(Long subjectId);
    Lesson saveLesson(LessonDto lesson);
    Boolean deleteLesson(Long lessonId);
    List<LessonDto> listLessons(Long teacherId, Long subjectId);
    List<LessonDto> listLessons(Long teacherId);
    List<LessonDto> listLessons(Long teacherId, Long subjectId, Long classId);
    LessonDto getLesson(Long lessonId);

    Topic saveTopic(Topic topic);
    Boolean deleteTopic(Long topicId);
    List<TopicDto> listTopics(Long teacherId, Long subjectId);

    SubjectDto getSubjectById(Long teacherId, Long subjectId);
    Subject saveOrUpdateSubject(SubjectDto subject);
    Boolean deleteSubject(Long teacherId, Long subjectId);
    List<SubjectDto> listSubjects(Long teacherId);
    SubjectDto initNewSubject(Long teacherId);
    SubjectDto updatePatchSubject(SubjectDto subjectUpdated);

    Attendance saveAttendance(AttendanceDto attendance);
    List<AttendanceDto> listAttendances(Long teacherId, Long subjectId, Long classId, Long lessonId);

    Grade saveClassGrade(Grade grade);
    Boolean deleteClassGrade(Long gradeId);
    List<Grade> listClassGrades(Long teacherId, Long schoolClassId);
    List<Grade> listStudentGrades(Long teacherId, Long studentId);

    List<GradeDto> listGradesBySubject(Long teacherId, Long subjectId);
    GradeDto updatePutGrade(GradeDto gradeDto);
    GradeDto updatePatchGrade(GradeDto gradeDto);
    Boolean deleteGrade(Long teacherId, Long subjectId, Long gradeId);
    Grade saveGrade(GradeDto grade);
    GradeDto initNewGrade(Long teacherId, Long subjectId);


    Event saveEvent(EventDto eventDto);
    EventDto initNewEvent(Long teacherId);
    EventDto initNewClassEvent(Long teacherId, Long classId);
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
    List<ClassDto> listClassesByTeacherAndSubject(Long teacherId, Long subjectId);
    ClassDto getSchoolClassByTeacherAndSubject(Long classId, Long subjectId, Long teacherId);
}
