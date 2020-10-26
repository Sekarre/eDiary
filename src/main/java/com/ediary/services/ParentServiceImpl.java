package com.ediary.services;

import com.ediary.DTO.AttendanceDto;
import com.ediary.DTO.ParentDto;
import com.ediary.DTO.StudentDto;
import com.ediary.converters.AttendanceDtoToAttendance;
import com.ediary.converters.ParentToParentDto;
import com.ediary.converters.StudentToStudentDto;
import com.ediary.domain.Attendance;
import com.ediary.domain.Parent;
import com.ediary.domain.Student;
import com.ediary.domain.security.User;
import com.ediary.exceptions.NoAccessException;
import com.ediary.exceptions.NotFoundException;
import com.ediary.repositories.AttendanceRepository;
import com.ediary.repositories.ParentRepository;
import com.ediary.repositories.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@RequiredArgsConstructor
@Service
public class ParentServiceImpl implements ParentService {


    private final StudentRepository studentRepository;
    private final AttendanceRepository attendanceRepository;
    private final ParentRepository parentRepository;

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

        Parent parent = parentRepository
                .findById(parentId).orElseThrow(() -> new NotFoundException("Parent not found"));

        parent.getStudents().stream()
                .filter(s -> s.getId().equals(studentId))
                .findFirst().orElseThrow(() -> new NoAccessException("Parent -> Student"));

        Student student = studentRepository
                .findById(studentId).orElseThrow(() -> new NotFoundException("Student not found"));

        return studentToStudentDto.convertForParent(student);
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
}
