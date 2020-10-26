package com.ediary.services;

import com.ediary.DTO.*;
import com.ediary.converters.*;
import com.ediary.domain.Attendance;
import com.ediary.domain.Parent;
import com.ediary.domain.Student;
import com.ediary.domain.security.User;
import com.ediary.exceptions.NoAccessException;
import com.ediary.exceptions.NotFoundException;
import com.ediary.repositories.AttendanceRepository;
import com.ediary.repositories.GradeRepository;
import com.ediary.repositories.ParentRepository;
import com.ediary.repositories.StudentRepository;
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

    private final StudentToStudentDto studentToStudentDto;
    private final AttendanceDtoToAttendance attendanceDtoToAttendance;
    private final ParentToParentDto parentToParentDto;

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
    public List<String> getAllStudentSubjectNames(Long parentId, Long studentId) {

        checkIfParentHasStudent(parentId, studentId);

        Set<String> subjectDtoSet = new HashSet<>();
        gradeRepository.findAllByStudentId(studentId).forEach(s -> subjectDtoSet.add(s.getSubject().getName()));

        return new ArrayList<>(subjectDtoSet);
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

    private void checkIfParentHasStudent(Long parentId, Long studentId) {
        Parent parent = parentRepository
                .findById(parentId).orElseThrow(() -> new NotFoundException("Parent not found"));

        parent.getStudents().stream()
                .filter(s -> s.getId().equals(studentId))
                .findFirst().orElseThrow(() -> new NoAccessException("Parent -> Student"));
    }


}
