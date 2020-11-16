package com.ediary.services;

import com.ediary.DTO.*;
import com.ediary.domain.*;
import com.ediary.domain.security.User;
import com.ediary.domain.timetable.Timetable;

import java.util.List;

public interface TeacherService {

    TeacherDto findByUser(User user);

    List<StudentDto> listLessonStudents(Long teacherId, Long subjectId, Long classId, Long lessonId);

    LessonDto initNewLesson(Long subjectId);
    Lesson saveLesson(LessonDto lesson);
    Boolean deleteLesson(Long lessonId);
    List<LessonDto> listLessons(Integer page, Integer size, Long teacherId, Long subjectId);
    List<LessonDto> listLessons(Long teacherId);
    List<LessonDto> listLessons(Long teacherId, Long subjectId, Long classId);
    LessonDto getLesson(Long lessonId);

    TopicDto initNewTopic(Long teacherId, Long subjectId);
    Topic updateTopic(TopicDto topicDto);
    Topic saveTopic(TopicDto topicDto);
    Boolean deleteTopic(Long teacherId, Long subjectId, Long topicId);
    List<TopicDto> listTopics(Long teacherId, Long subjectId);
    TopicDto updatePatchTopic(TopicDto topicUpdated);

    SubjectDto getSubjectById(Long teacherId, Long subjectId);
    Subject saveOrUpdateSubject(SubjectDto subject);
    Boolean deleteSubject(Long teacherId, Long subjectId);
    List<SubjectDto> listSubjects(Long teacherId);
    SubjectDto initNewSubject(Long teacherId);
    SubjectDto updatePatchSubject(SubjectDto subjectUpdated);

    Attendance saveAttendance(AttendanceDto attendance);
    List<AttendanceDto> listAttendances(Long teacherId, Long subjectId, Long classId, Long lessonId);
    List<AttendanceDto> listAttendancesByStudent(Long studentId);


    Grade saveClassGrade(Grade grade);
    Boolean deleteClassGrade(Long gradeId);
    List<GradeDto> listClassGrades(Long teacherId, Long schoolClassId);
    List<GradeDto> listStudentGrades(Long teacherId, Long studentId);

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
    List<ClassDto> listClassByTeacher(Long teacherId);


    List<ExtenuationDto> listExtenuations(Long teacherId);
    Boolean acceptExtenuation(Long extenuationId);
    Boolean rejectExtenuation(Long extenuationId);

    List<StudentDto> listClassStudents(Long teacherId, Long classId);

    Boolean isFormTutor(Long teacherId, Long classId);

    Boolean excuseAttendances(List<Long> ids, Long teacherId, Long studentId, Long classId);

    Timetable getTimetableByTeacherId(Long teacherId);
}
