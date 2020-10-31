package com.ediary.services;

import com.ediary.DTO.*;
import com.ediary.converters.*;
import com.ediary.domain.*;
import com.ediary.domain.security.User;
import com.ediary.domain.timetable.Day;
import com.ediary.exceptions.NoAccessException;
import com.ediary.exceptions.NotFoundException;
import com.ediary.repositories.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;


@RequiredArgsConstructor
@Service
public class ParentServiceImpl implements ParentService {


    private final StudentRepository studentRepository;
    private final AttendanceRepository attendanceRepository;
    private final ParentRepository parentRepository;
    private final GradeRepository gradeRepository;
    private final SubjectRepository subjectRepository;
    private final ExtenuationRepository extenuationRepository;

    private final StudentToStudentDto studentToStudentDto;
    private final AttendanceDtoToAttendance attendanceDtoToAttendance;
    private final ParentToParentDto parentToParentDto;
    private final SubjectToSubjectDto subjectToSubjectDto;
    private final ExtenuationToExtenuationDto extenuationToExtenuationDto;
    private final ExtenuationDtoToExtenuation extenuationDtoToExtenuation;

    @Override
    public List<StudentDto> listStudents(Long parentId) {

        Optional<Parent> parentOptional = parentRepository.findById(parentId);

        if (parentOptional.isEmpty()) {
            throw new NotFoundException("Parent Not Found. ");
        }

        Parent parent = parentOptional.get();


        return studentRepository.findAllByParentId(parent.getId())
                .stream()
                .map(studentToStudentDto::convertForParent)
                .collect(Collectors.toList());
    }

    @Override
    public StudentDto findStudent(Long parentId, Long studentId) {

        checkIfParentHasStudent(parentId, studentId);

        Student student = studentRepository
                .findById(studentId).orElseThrow(() -> new NotFoundException("Student not found"));

        return studentToStudentDto.convertForParent(student);
    }

    @Override
    public List<SubjectDto> getAllStudentSubjectNames(Long parentId, Long studentId) {

        checkIfParentHasStudent(parentId, studentId);

        Student student = studentRepository.findById(studentId).orElseThrow(() -> new NotFoundException("Student not found"));

        List<Subject> subjects = subjectRepository.findAllBySchoolClassIdOrderByName(student.getSchoolClass().getId());


        return subjects.stream()
                .map(subjectToSubjectDto::convert)
                .collect(Collectors.toList());
    }


    @Override
    public Attendance saveAttendance(AttendanceDto attendance) {

        Attendance attendanceToSave = attendanceDtoToAttendance.convert(attendance);
        return attendanceRepository.save(attendanceToSave);
    }

    @Override
    public ParentDto findByUser(User user) {
        Optional<Parent> parentOptional = parentRepository.findByUser(user);
        if (!parentOptional.isPresent()) {
            throw new NotFoundException("Parent Not Found.");
        }

        return parentToParentDto.convert(parentOptional.get());
    }

    //todo: test
    @Override
    public ExtenuationDto initNewExtenuation(Long studentId, Long parentId) {
        Parent parent = parentRepository
                .findById(parentId).orElseThrow(() -> new NotFoundException("Parent not found"));

        Extenuation extenuation = Extenuation.builder().parent(parent).build();

        return extenuationToExtenuationDto.convert(extenuation);
    }

    //todo: test
    @Override
    public ExtenuationDto addAttendancesToExtenuation(List<Long> attendancesId, ExtenuationDto extenuationDto, Long parentId) {
        Extenuation extenuation = extenuationDtoToExtenuation.convert(extenuationDto);

        List<Attendance> attendances = attendanceRepository.findAllById(attendancesId);

        if (attendances.size() != 0) {
            attendances.forEach(attendance -> {
                attendance.setExtenuations(Collections.singleton(extenuation));
            });

            extenuation.setAttendances(new HashSet<>(attendances));

        }


        return extenuationToExtenuationDto.convert(extenuation);
    }

    //todo: tests
    @Override
    public Extenuation saveExtenuation(ExtenuationDto extenuationDto, Long parentId, List<Long> attId) {
        Extenuation extenuation = extenuationDtoToExtenuation.convert(extenuationDto);

        extenuation.setParent(parentRepository
                .findById(parentId).orElseThrow(() -> new NotFoundException("Parent not found")));
        extenuation.setStatus(Extenuation.Status.SENT);

        List<Attendance> attendances = attendanceRepository.findAllById(attId);

        extenuation.setAttendances(new HashSet<>(attendances));

        return extenuationRepository.save(extenuation);
    }

    private void checkIfParentHasStudent(Long parentId, Long studentId) {
        Parent parent = parentRepository
                .findById(parentId).orElseThrow(() -> new NotFoundException("Parent not found"));

        parent.getStudents().stream()
                .filter(s -> s.getId().equals(studentId))
                .findFirst().orElseThrow(() -> new NoAccessException("Parent -> Student"));
    }


}
