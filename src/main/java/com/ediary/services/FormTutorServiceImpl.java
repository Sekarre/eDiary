package com.ediary.services;

import com.ediary.DTO.StudentCouncilDto;
import com.ediary.DTO.StudentDto;
import com.ediary.converters.StudentCouncilDtoToStudentCouncil;
import com.ediary.converters.StudentCouncilToStudentCouncilDto;
import com.ediary.converters.StudentToStudentDto;
import com.ediary.domain.*;
import com.ediary.exceptions.NotFoundException;
import com.ediary.repositories.StudentCouncilRepository;
import com.ediary.repositories.StudentRepository;
import com.ediary.repositories.TeacherRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class FormTutorServiceImpl implements FormTutorService {

    private final TeacherRepository teacherRepository;
    private final StudentCouncilRepository studentCouncilRepository;
    private final StudentRepository studentRepository;

    private final StudentCouncilDtoToStudentCouncil studentCouncilDtoToStudentCouncil;
    private final StudentCouncilToStudentCouncilDto studentCouncilToStudentCouncilDto;
    private final StudentToStudentDto studentToStudentDto;


    @Override
    public StudentCouncilDto initNewStudentCouncil(Long teacherId) {
        Teacher teacher = getTeacherById(teacherId);

        return StudentCouncilDto.builder()
                .schoolClassId(teacher.getSchoolClass().getId())
                .build();
    }

    @Override
    public StudentCouncil saveStudentCouncil(Long teacherId, StudentCouncilDto studentCouncilDto, List<Long> studentsId) {
        Teacher teacher = getTeacherById(teacherId);

        StudentCouncil studentCouncil = studentCouncilDtoToStudentCouncil.convert(studentCouncilDto);

        if (studentCouncil != null) {
            if (studentCouncil.getStudents().size() > 0) {
                studentCouncil.getStudents().addAll(studentRepository.findAllById(studentsId));
            } else {
                studentCouncil.setStudents(new HashSet<>(studentRepository.findAllById(studentsId)));
            }
            return studentCouncilRepository.save(studentCouncil);

        } else {
            throw new NotFoundException("Students not found");
        }
    }

    @Override
    public StudentCouncilDto findStudentCouncil(Long teacherId) {
        Teacher teacher = getTeacherById(teacherId);

        return studentCouncilToStudentCouncilDto.convert(teacher.getSchoolClass().getStudentCouncil());
    }

    @Override
    public Boolean deleteStudentCouncil(Long teacherId) {
        Teacher teacher = getTeacherById(teacherId);

        StudentCouncil studentCouncil = teacher.getSchoolClass().getStudentCouncil();

        if (studentCouncil != null) {
            studentCouncilRepository.delete(studentCouncil);
            return true;
        }

        return false;
    }

    @Override
    public ParentCouncil saveParentCouncil(ParentCouncil parentCouncil) {
        return null;
    }

    @Override
    public ParentCouncil findParentCouncil(Long schoolClassId) {
        return null;
    }

    @Override
    public StudentCard findStudentCard(Long teacherId, Long studentId) {
        return null;
    }

    @Override
    public Grade saveGrade(Long studentId) {
        return null;
    }

    @Override
    public Grade findGrade(Long studentId) {
        return null;
    }

    @Override
    public List<StudentDto> listClassStudents(Long teacherId) {
        Teacher teacher = getTeacherById(teacherId);

        return studentRepository.findAllBySchoolClassId(teacher.getSchoolClass().getId())
                .stream()
                .map(studentToStudentDto::convert)
                .collect(Collectors.toList());
    }

    private Teacher getTeacherById(Long teacherId) {
        return teacherRepository
                .findById(teacherId).orElseThrow(() -> new NotFoundException("Teacher not found"));
    }
}
