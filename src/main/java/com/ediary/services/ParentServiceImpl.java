package com.ediary.services;

import com.ediary.DTO.AttendanceDto;
import com.ediary.DTO.StudentDto;
import com.ediary.converters.AttendanceDtoToAttendance;
import com.ediary.converters.StudentToStudentDto;
import com.ediary.domain.Attendance;
import com.ediary.domain.Parent;
import com.ediary.domain.Student;
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
    public Attendance saveAttendance(AttendanceDto attendance) {

        Attendance attendanceToSave = attendanceDtoToAttendance.convert(attendance);
        return attendanceRepository.save(attendanceToSave);
    }
}
