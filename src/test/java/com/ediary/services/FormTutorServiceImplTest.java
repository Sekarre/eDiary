package com.ediary.services;

import com.ediary.DTO.StudentCouncilDto;
import com.ediary.DTO.StudentDto;
import com.ediary.converters.StudentCouncilDtoToStudentCouncil;
import com.ediary.converters.StudentCouncilToStudentCouncilDto;
import com.ediary.converters.StudentToStudentDto;
import com.ediary.domain.Class;
import com.ediary.domain.Student;
import com.ediary.domain.StudentCouncil;
import com.ediary.domain.Teacher;
import com.ediary.repositories.StudentCouncilRepository;
import com.ediary.repositories.StudentRepository;
import com.ediary.repositories.TeacherRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


public class FormTutorServiceImplTest {

    @Mock
    TeacherRepository teacherRepository;
    @Mock
    StudentCouncilRepository studentCouncilRepository;
    @Mock
    StudentRepository studentRepository;

    @Mock
    StudentCouncilDtoToStudentCouncil studentCouncilDtoToStudentCouncil;
    @Mock
    StudentCouncilToStudentCouncilDto studentCouncilToStudentCouncilDto;
    @Mock
    StudentToStudentDto studentToStudentDto;

    FormTutorService formTutorService;

    @BeforeEach
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        formTutorService = new FormTutorServiceImpl(teacherRepository, studentCouncilRepository, studentRepository,
                studentCouncilDtoToStudentCouncil, studentCouncilToStudentCouncilDto, studentToStudentDto);
    }

    @Test
    void initNewStudentCouncil() {
        Long teacherId = 1L;

        Teacher teacher = Teacher.builder()
                .id(1L)
                .schoolClass(Class.builder().id(1L).build())
                .build();

        when(teacherRepository.findById(any())).thenReturn(java.util.Optional.ofNullable(teacher));

        StudentCouncilDto studentCouncilDto = formTutorService.initNewStudentCouncil(teacherId);

        assertNotNull(studentCouncilDto);
        verify(teacherRepository, times(1)).findById(teacherId);

    }

    @Test
    void saveStudentCouncil() {
        Long teacherId = 1L;

        Teacher teacher = Teacher.builder().id(1L).build();
        StudentCouncil studentCouncil = StudentCouncil.builder().id(1L).build();
        StudentCouncilDto studentCouncilDto = StudentCouncilDto.builder().id(1L).build();;

        when(teacherRepository.findById(any())).thenReturn(java.util.Optional.ofNullable(teacher));
        when(studentCouncilDtoToStudentCouncil.convert(any())).thenReturn(studentCouncil);
        when(studentRepository.findAllById(any())).thenReturn(Collections.singletonList(Student.builder().build()));
        when(studentCouncilRepository.save(any())).thenReturn(studentCouncil);

        StudentCouncil savedStudentCouncil = formTutorService
                .saveStudentCouncil(teacherId, studentCouncilDto, new ArrayList<>(){{
                    add(1L);
                    add(2L);
                }});

        assertNotNull(savedStudentCouncil);
        verify(teacherRepository, times(1)).findById(teacherId);
        verify(studentCouncilDtoToStudentCouncil, times(1)).convert(studentCouncilDto);

    }

    @Test
    void findStudentCouncil() {
        Long teacherId = 1L;

        Teacher teacher = Teacher.builder()
                .id(1L)
                .schoolClass(Class.builder()
                        .studentCouncil(StudentCouncil.builder().build())
                        .build())
                .build();
        StudentCouncilDto studentCouncilDto = StudentCouncilDto.builder().id(1L).build();;

        when(teacherRepository.findById(any())).thenReturn(java.util.Optional.ofNullable(teacher));
        when(studentCouncilToStudentCouncilDto.convert(any())).thenReturn(studentCouncilDto);

        StudentCouncilDto studentCouncilDto1 = formTutorService.findStudentCouncil(teacherId);

        assertNotNull(studentCouncilDto1);
        verify(teacherRepository, times(1)).findById(teacherId);

    }


    @Test
    void listClassStudents() {
        Long teacherId = 1L;
        Long schoolClassId = 1L;

        Teacher teacher = Teacher.builder()
                .id(teacherId)
                .schoolClass(Class.builder().id(schoolClassId).build())
                .build();

        Student student = Student.builder().build();

        when(teacherRepository.findById(any())).thenReturn(java.util.Optional.ofNullable(teacher));
        when(studentToStudentDto.convert(any())).thenReturn(StudentDto.builder().build());
        when(studentRepository.findAllBySchoolClassId(any())).thenReturn(Collections.singletonList(student));


        List<StudentDto> studentDtoList = formTutorService.listClassStudents(teacherId);

        assertNotNull(studentDtoList);
        verify(studentRepository, times(1)).findAllBySchoolClassId(teacherId);
        verify(teacherRepository, times(1)).findById(teacherId);
        verify(studentToStudentDto, times(1)).convert(student);
    }

}
