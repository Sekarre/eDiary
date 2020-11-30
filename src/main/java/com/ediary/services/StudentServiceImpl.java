package com.ediary.services;

import com.ediary.DTO.*;
import com.ediary.converters.*;
import com.ediary.domain.*;
import com.ediary.domain.Class;
import com.ediary.domain.helpers.GradeWeight;
import com.ediary.domain.security.User;
import com.ediary.domain.timetable.Timetable;
import com.ediary.exceptions.NotFoundException;
import com.ediary.repositories.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class StudentServiceImpl implements StudentService {

    private final TimetableService timetableService;
    private final EventService eventService;

    private final GradeRepository gradeRepository;
    private final AttendanceRepository attendanceRepository;
    private final BehaviorRepository behaviorRepository;
    private final StudentRepository studentRepository;
    private final EventRepository eventRepository;
    private final SubjectRepository subjectRepository;

    private final GradeToGradeDto gradeToGradeDto;
    private final AttendanceToAttendanceDto attendanceToAttendanceDto;
    private final BehaviorToBehaviorDto behaviorToBehaviorDto;
    private final EventToEventDto eventToEventDto;
    private final StudentToStudentDto studentToStudentDto;
    private final SubjectToSubjectDto subjectToSubjectDto;


    @Override
    public List<GradeDto> listGrades(Long studentId) {
        return gradeRepository.findAllByStudentId(studentId)
                .stream()
                .map(gradeToGradeDto::convert)
                .collect(Collectors.toList());
    }

    @Override
    public List<GradeDto> listGrades(Long studentId, Long subjectId) {
        return gradeRepository.findAllByStudentIdAndSubjectId(studentId, subjectId)
                .stream()
                .map(gradeToGradeDto::convert)
                .collect(Collectors.toList());
    }

    @Override
    public List<SubjectDto> listSubjects(Long studentId) {
        Student student = getStudentById(studentId);
        return subjectRepository.findAllBySchoolClass_Students(student)
                .stream()
                .map(subjectToSubjectDto::convert)
                .collect(Collectors.toList());
    }

    @Override
    public List<AttendanceDto> listAttendances(Long studentId) {
        return attendanceRepository.findAllByStudentId(studentId)
                .stream()
                .map(attendanceToAttendanceDto::convert)
                .collect(Collectors.toList());
    }

    @Override
    public List<BehaviorDto> listBehaviors(Long studentId) {
        return behaviorRepository.findAllByStudentId(studentId)
                .stream()
                .map(behaviorToBehaviorDto::convert)
                .collect(Collectors.toList());
    }

    @Override
    public List<EventDto> listEvents(Long studentId, Integer page, Integer size) {

        Student student = getStudentById(studentId);

        if (student.getSchoolClass() == null) {
            return null;
        }

        Pageable pageable = PageRequest.of(page, size);

        return eventRepository.findAllBySchoolClassIdOrderByDate(student.getSchoolClass().getId(), pageable)
                .stream()
                .map(eventToEventDto::convert)
                .collect(Collectors.toList());
    }

    @Override
    public Timetable getTimetableByStudentId(Long studentId) {

        Student student = getStudentById(studentId);

        if (student.getSchoolClass() == null) {
            return null;
        }

        return timetableService.getTimetableByClassId(student.getSchoolClass().getId());

    }

    @Override
    public StudentDto findByUser(User user) {
        Optional<Student> studentOptional = studentRepository.findByUser(user);
        if (!studentOptional.isPresent()) {
            throw new NotFoundException("Student Not Found");
        }
        return studentToStudentDto.convert(studentOptional.get());
    }


    @Override
    public Map<SubjectDto, List<GradeDto>> listSubjectsGrades(Long studentId) {
        Student student = getStudentById(studentId);

        Map<SubjectDto, List<GradeDto>> studentGradesListMap = new TreeMap<>(
                ((o1, o2) -> o1.getName().compareToIgnoreCase(o2.getName())));

        student.getSchoolClass().getSubjects().forEach(subject -> {
            studentGradesListMap.put(subjectToSubjectDto.convert(subject),
                    gradeRepository.findAllByStudentIdAndSubjectId(student.getId(), subject.getId())
                            .stream()
                            .map(gradeToGradeDto::convert)
                            .collect(Collectors.toList()));
        });


        if (studentGradesListMap.keySet().isEmpty()) {
            return null;
        }

        return studentGradesListMap;
    }

    @Override
    public GradeDto getBehaviorGrade(Long studentId) {
        Student student = getStudentById(studentId);

        Grade grade = gradeRepository.findByStudentIdAndWeight(student.getId(), GradeWeight.BEHAVIOR_GRADE.getWeight());

        if (grade != null) {
            return gradeToGradeDto.convert(grade);
        }

        return null;
    }

    private Student getStudentById(Long studentId) {
        Optional<Student> studentOptional = studentRepository.findById(studentId);

        if(!studentOptional.isPresent()) {
            throw new NotFoundException("Student Not Found.");
        }

        return studentOptional.get();
    }


}
