package com.ediary.services;

import com.ediary.DTO.ClassDto;
import com.ediary.DTO.StudentCouncilDto;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
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
    void listAllClasses() {
        Class schoolClass = Class.builder().build();
        ClassDto schoolClassDto = ClassDto.builder().build();

        when(classRepository.findAll()).thenReturn(new ArrayList<>(){{
            add(schoolClass);
        }});
        when(classToClassDto.convert(any())).thenReturn(schoolClassDto);

        List<ClassDto> classes = deputyHeadService.listAllClasses();

        assertNotNull(classes);
        assertEquals(1, classes.size());
        verify(classRepository, times(1)).findAll();
    }


    @Test
    void removeFormTutorFromClass() {
        Long teacherId = 1L;
        Long classId = 1L;

        Class schoolClass = Class.builder()
                .id(classId)
                .teacher(Teacher.builder().id(teacherId).build())
                .build();


        when(classRepository.findById(any())).thenReturn(java.util.Optional.ofNullable(schoolClass));
        when(classToClassDto.convert(any())).thenReturn(ClassDto.builder().build());

        ClassDto newSchoolClass = deputyHeadService.removeFormTutorFromClass(classId, teacherId);

        assertNotNull(newSchoolClass);
        assertNull(newSchoolClass.getTeacherId());
        verify(classRepository, times(1)).findById(classId);
    }

    @Test
    void removeStudentFromClass() {
        Long studentId = 1L;
        Long classId = 1L;

        Class schoolClass = Class.builder()
                .id(classId)
                .students(Collections.singleton(Student.builder().id(studentId).build()))
                .build();


        when(classRepository.findById(any())).thenReturn(java.util.Optional.ofNullable(schoolClass));
        when(classToClassDto.convert(any())).thenReturn(ClassDto.builder().build());
        when(studentRepository.findById(any())).thenReturn(java.util.Optional.ofNullable(Student.builder().build()));

        ClassDto newSchoolClass = deputyHeadService.removeStudentFromClass(classId, studentId);

        assertNotNull(newSchoolClass);
        assertNull(newSchoolClass.getStudents());
        verify(classRepository, times(1)).findById(classId);
    }

    @Test
    void addFormTutorToClass() {
        Long teacherId = 1L;
        Long classId = 1L;

        Class schoolClass = Class.builder()
                .id(classId)
                .build();


        when(teacherRepository.findById(any())).thenReturn(java.util.Optional.ofNullable(Teacher.builder().build()));
        when(classRepository.findById(any())).thenReturn(java.util.Optional.ofNullable(schoolClass));
        when(classToClassDto.convert(any())).thenReturn(ClassDto.builder().build());

        ClassDto newSchoolClass = deputyHeadService.addFormTutorToClass(classId, teacherId);

        assertNotNull(newSchoolClass);
        verify(classRepository, times(1)).findById(classId);
        verify(teacherRepository, times(1)).findById(teacherId);
    }


    @Test
    void addStudentToClass() {
        Long studentId = 1L;
        Long classId = 1L;

        Class schoolClass = Class.builder()
                .id(classId)
                .students(Collections.singleton(Student.builder().id(2L).build()))
                .build();


        when(studentRepository.findById(any())).thenReturn(java.util.Optional.ofNullable(Student.builder().build()));
        when(classRepository.findById(any())).thenReturn(java.util.Optional.ofNullable(schoolClass));
        when(classToClassDto.convert(any())).thenReturn(ClassDto.builder().build());

        ClassDto newSchoolClass = deputyHeadService.addStudentToClass(classId, studentId);

        assertNotNull(newSchoolClass);
        verify(classRepository, times(1)).findById(classId);
        verify(studentRepository, times(1)).findById(studentId);
    }


    @Test
    void listAllStudentsWithoutClass() {

        StudentDto studentDto = StudentDto.builder().build();

        List<Student> students = new ArrayList<>() {{
            add(Student.builder().build());
        }};

        Page<Student> page = new PageImpl<>(students);


        when(studentRepository.findAllBySchoolClassIsNull(any())).thenReturn(page);
        when(studentToStudentDto.convert(any())).thenReturn(studentDto);

        List<StudentDto> returnedStudents = deputyHeadService.listAllStudentsWithoutClass(0, 1);

        assertNotNull(returnedStudents);
        verify(studentRepository, times(1)).findAllBySchoolClassIsNull(any());
    }



    @Test
    void listAllTeachersWithoutClass() {
        TeacherDto teacherDto = TeacherDto.builder().build();

        List<Teacher> teachersPage = new ArrayList<>() {{
            add(Teacher.builder().build());
        }};

        Page<Teacher> page = new PageImpl<>(teachersPage);

        when(teacherRepository.findAllBySchoolClassIsNull(any())).thenReturn(page);
        when(teacherToTeacherDto.convert(any())).thenReturn(teacherDto);

        List<TeacherDto> teachers = deputyHeadService.listAllTeachersWithoutClass(0, 1);

        assertNotNull(teachers);
        verify(teacherRepository, times(1)).findAllBySchoolClassIsNull(any());
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
