package com.ediary.services;

import com.ediary.DTO.*;
import com.ediary.domain.*;
import com.ediary.domain.security.User;
import com.ediary.domain.timetable.Timetable;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

public interface TeacherService {

    TeacherDto findByUser(User user);

    List<StudentDto> listLessonStudents(Long teacherId, Long subjectId, Long classId, Long lessonId);

    LessonDto initNewLesson(Long subjectId);
    Lesson saveLesson(LessonDto lesson);
    Boolean deleteLesson(Long lessonId);
    List<LessonDto> listLessons(Integer page, Integer size, Long teacherId, Long subjectId);
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
    List<Attendance> saveAttendancesForClass(AttendanceDto attendanceDto, Long classId, Long teacherId);
    List<AttendanceDto> listAttendances(Long teacherId, Long subjectId, Long classId, Long lessonId);
    List<AttendanceDto> listAttendancesByStudent(Long studentId);
    AttendanceDto initNewLessonAttendance(Long teacherId, Long subjectId, Long lessonId);


    Grade saveClassGrade(Grade grade);
    Boolean deleteClassGrade(Long gradeId);
    List<GradeDto> listClassGrades(Long teacherId, Long schoolClassId);
    Boolean deleteLessonGrade(Long studentId, Long gradeId);


    List<GradeDto> listGradesBySubject(Long teacherId, Long subjectId);
    GradeDto updatePutGrade(GradeDto gradeDto);
    GradeDto updatePatchGrade(GradeDto gradeDto);
    Boolean deleteGrade(Long teacherId, Long subjectId, Long gradeId);
    Grade saveOrUpdateGrade(GradeDto grade);
    GradeDto initNewGrade(Long teacherId, Long subjectId);
    GradeDto initNewLessonGrade(Long teacherId, Long subjectId, Long lessonId);

    Event saveEvent(EventDto eventDto);
    EventDto initNewEvent(Long teacherId);
    EventDto initNewClassEvent(Long teacherId, Long subjectId);
    Boolean deleteEvent(Long teacherId, Long eventId);
    List<EventDto> listEvents(Long teacherId);
    List<EventDto> listClassEvents(Long teacherId, Long subjectId, Integer page, Integer size, Boolean includeHistory);
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

    Map<StudentDto, AttendanceDto> listStudentsLessonAttendances(Long teacherId, Long lessonId);
    Map<StudentDto, List<GradeDto>> listStudentsLessonGrades(Long teacherId, Long lessonId);
    Map<StudentDto, List<GradeDto>> listStudentsGrades(Long teacherId, Long subjectId);

    List<Long> maxGradesCount(Long teacherId, Long lessonId);
    List<Long> maxGradesCountBySubject(Long teacherId, Long subjectId);

}
