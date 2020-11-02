package com.ediary.services;

import com.ediary.DTO.ClassDto;
import com.ediary.DTO.StudentDto;
import com.ediary.DTO.TeacherDto;
import com.ediary.converters.ClassDtoToClass;
import com.ediary.converters.ClassToClassDto;
import com.ediary.converters.StudentToStudentDto;
import com.ediary.converters.TeacherToTeacherDto;
import com.ediary.domain.Class;
import com.ediary.domain.Student;
import com.ediary.repositories.ClassRepository;
import com.ediary.repositories.StudentRepository;
import com.ediary.repositories.TeacherRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class DeputyHeadServiceImpl implements DeputyHeadService {

    private final ClassRepository classRepository;
    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;

    private final StudentToStudentDto studentToStudentDto;
    private final TeacherToTeacherDto teacherToTeacherDto;
    private final ClassToClassDto classToClassDto;
    private final ClassDtoToClass classDtoToClass;


    @Override
    public ClassDto initNewClass() {
        return ClassDto.builder().build();
    }

    @Override
    public Class saveClass(ClassDto schoolClassDto, List<Long> studentsId) {

        Class schoolClass = classDtoToClass.convert(schoolClassDto);

        if (schoolClass != null) {
            schoolClass.setStudents(new HashSet<>(studentRepository.findAllById(studentsId)));
            return classRepository.save(schoolClass);
        }

        return null;
    }

    @Override
    public Boolean deleteClass(Long schoolClassId) {
        return null;
    }

    @Override
    public List<ClassDto> listAllClasses() {
        return null;
    }

    @Override
    public List<StudentDto> listAllStudentsWithoutClass() {

        return studentRepository.findAllBySchoolClassIsNull()
                .stream()
                .map(studentToStudentDto::convert)
                .collect(Collectors.toList());
    }

    @Override
    public List<TeacherDto> listAllTeachersWithoutClass() {
        return teacherRepository.findAllBySchoolClassIsNull()
                .stream()
                .map(teacherToTeacherDto::convert)
                .collect(Collectors.toList());
    }
}