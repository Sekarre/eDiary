package com.ediary.services;

import com.ediary.DTO.ClassDto;
import com.ediary.DTO.ParentCouncilDto;
import com.ediary.DTO.StudentDto;
import com.ediary.DTO.TeacherDto;
import com.ediary.converters.ClassDtoToClass;
import com.ediary.converters.ClassToClassDto;
import com.ediary.converters.StudentToStudentDto;
import com.ediary.converters.TeacherToTeacherDto;
import com.ediary.domain.*;
import com.ediary.domain.Class;
import com.ediary.repositories.ClassRepository;
import com.ediary.repositories.StudentRepository;
import com.ediary.repositories.TeacherRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

public class DeputyHeadServiceImplTest {

    @Mock
    ClassRepository classRepository;
    @Mock
    StudentRepository studentRepository;
    @Mock
    TeacherRepository teacherRepository;

    @Mock
    StudentToStudentDto studentToStudentDto;
    @Mock
    TeacherToTeacherDto teacherToTeacherDto;
    @Mock
    ClassToClassDto classToClassDto;
    @Mock
    ClassDtoToClass classDtoToClass;

    DeputyHeadService deputyHeadService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        deputyHeadService = new DeputyHeadServiceImpl(classRepository, studentRepository, teacherRepository, studentToStudentDto,
                teacherToTeacherDto, classToClassDto, classDtoToClass);
    }


    @Test
    void initNewClass() {
        ClassDto newClassDto = deputyHeadService.initNewClass();

        assertNotNull(newClassDto);
    }


    @Test
    void listAllStudentsWithoutClass() {

        StudentDto studentDto = StudentDto.builder().build();

        when(studentRepository.findAllBySchoolClassIsNull()).thenReturn(new ArrayList<>(){{
            add(Student.builder().id(1L).build());
            add(Student.builder().id(2L).build());
        }});
        when(studentToStudentDto.convert(any())).thenReturn(studentDto);

        List<StudentDto> students = deputyHeadService.listAllStudentsWithoutClass();

        assertNotNull(students);
        verify(studentRepository, times(1)).findAllBySchoolClassIsNull();
    }

    @Test
    void listAllTeachersWithoutClass() {
        TeacherDto teacherDto = TeacherDto.builder().build();

        when(teacherRepository.findAllBySchoolClassIsNull()).thenReturn(new ArrayList<>(){{
            add(Teacher.builder().id(1L).build());
            add(Teacher.builder().id(2L).build());
        }});
        when(teacherToTeacherDto.convert(any())).thenReturn(teacherDto);

        List<TeacherDto> teachers = deputyHeadService.listAllTeachersWithoutClass();

        assertNotNull(teachers);
        verify(teacherRepository, times(1)).findAllBySchoolClassIsNull();
    }


    @Test
    void saveClass() {
        Student student = Student.builder().id(1L).build();

        when(studentRepository.findAllById(any())).thenReturn(Collections.singletonList(student));
        when(classDtoToClass.convert(any())).thenReturn(Class.builder().build());
        when(classRepository.save(any())).thenReturn(Class.builder().build());

        Class savedClass = deputyHeadService
                .saveClass(ClassDto.builder().build(),  new ArrayList<>(){{
                    add(1L);
                    add(2L);
                }});

        assertNotNull(savedClass);
        verify(studentRepository, times(1)).findAllById(any());
        verify(classDtoToClass, times(1)).convert(any());
    }
}
