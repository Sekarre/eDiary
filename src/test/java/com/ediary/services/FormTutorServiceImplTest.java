package com.ediary.services;

import com.ediary.DTO.*;
import com.ediary.converters.*;
import com.ediary.domain.*;
import com.ediary.domain.Class;
import com.ediary.repositories.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;

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
    ParentRepository parentRepository;
    @Mock
    ParentCouncilRepository parentCouncilRepository;
    @Mock
    GradeRepository gradeRepository;

    @Mock
    StudentCouncilDtoToStudentCouncil studentCouncilDtoToStudentCouncil;
    @Mock
    StudentCouncilToStudentCouncilDto studentCouncilToStudentCouncilDto;
    @Mock
    ParentCouncilToParentCouncilDto parentCouncilToParentCouncilDto;
    @Mock
    ParentCouncilDtoToParentCouncil parentCouncilDtoToParentCouncil;
    @Mock
    StudentToStudentDto studentToStudentDto;
    @Mock
    ParentToParentDto parentToParentDto;
    @Mock
    GradeToGradeDto gradeToGradeDto;
    @Mock
    GradeDtoToGrade gradeDtoToGrade;


    FormTutorService formTutorService;

    @BeforeEach
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        formTutorService = new FormTutorServiceImpl(teacherRepository, studentCouncilRepository, studentRepository, parentRepository,
                parentCouncilRepository,gradeRepository, studentCouncilDtoToStudentCouncil, studentCouncilToStudentCouncilDto,
                parentCouncilDtoToParentCouncil, parentCouncilToParentCouncilDto, studentToStudentDto, parentToParentDto,
                gradeToGradeDto, gradeDtoToGrade);
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
    void deleteStudentCouncil() {
        Long teacherId = 1L;

        Teacher teacher = Teacher.builder()
                .id(teacherId)
                .schoolClass(Class.builder()
                        .studentCouncil(StudentCouncil.builder().build())
                        .build())
                .build();

        when(teacherRepository.findById(any())).thenReturn(java.util.Optional.ofNullable(teacher));

        Boolean result = formTutorService.deleteStudentCouncil(teacherId);

        assertTrue(result);
        verify(teacherRepository, times(1)).findById(teacherId);
    }

    @Test
    void removeStudentFromCouncil() {
        Long teacherId = 1L;
        Long studentId = 1L;

        StudentCouncil studentCouncil = StudentCouncil.builder()
                .id(1L)
                .students(new HashSet<>(){{
                    add(Student.builder().id(studentId).build());
                    add(Student.builder().id(2L).build());
                }})
                .build();

        StudentCouncilDto studentCouncilDto = StudentCouncilDto.builder()
                .id(1L)
                .studentsId(new ArrayList<>(){{
                    add(studentId);
                    add(2L);
                }})
                .build();

        when(studentRepository.findById(any())).thenReturn(java.util.Optional.ofNullable(Student.builder().build()));
        when(studentCouncilDtoToStudentCouncil.convert(any())).thenReturn(studentCouncil);
        when(studentCouncilToStudentCouncilDto.convert(any())).thenReturn(studentCouncilDto);
        when(studentCouncilRepository.save(any())).thenReturn(studentCouncil);


        StudentCouncilDto removedStudentCouncilDto = formTutorService
                .removeStudentFromCouncil(studentCouncilDto, studentId);


        assertNotNull(removedStudentCouncilDto);
        verify(studentRepository, times(1)).findById(teacherId);
        verify(studentCouncilRepository, times(1)).save(studentCouncil);
    }

    @Test
    void initNewParentCouncil() {
        Long teacherId = 1L;

        Teacher teacher = Teacher.builder()
                .id(1L)
                .schoolClass(Class.builder().id(1L).build())
                .build();

        when(teacherRepository.findById(any())).thenReturn(java.util.Optional.ofNullable(teacher));

        ParentCouncilDto parentCouncilDto = formTutorService.initNewParentCouncil(teacherId);

        assertNotNull(parentCouncilDto);
        verify(teacherRepository, times(1)).findById(teacherId);

    }


    @Test
    void saveParentCouncil() {
        Long teacherId = 1L;

        Teacher teacher = Teacher.builder().id(1L).build();
        ParentCouncil parentCouncil = ParentCouncil.builder().id(1L).build();
        ParentCouncilDto parentCouncilDto = ParentCouncilDto.builder().id(1L).build();;

        when(teacherRepository.findById(any())).thenReturn(java.util.Optional.ofNullable(teacher));
        when(parentCouncilDtoToParentCouncil.convert(any())).thenReturn(parentCouncil);
        when(parentRepository.findAllById(any())).thenReturn(Collections.singletonList(Parent.builder().build()));
        when(parentCouncilRepository.save(any())).thenReturn(parentCouncil);

        ParentCouncil savedParentCouncil = formTutorService
                .saveParentCouncil(teacherId, parentCouncilDto, new ArrayList<>(){{
                    add(1L);
                    add(2L);
                }});

        assertNotNull(savedParentCouncil);
        verify(teacherRepository, times(1)).findById(teacherId);
        verify(parentCouncilDtoToParentCouncil, times(1)).convert(parentCouncilDto);
    }

    @Test
    void findParentCouncil() {
        Long teacherId = 1L;

        Teacher teacher = Teacher.builder()
                .id(1L)
                .schoolClass(Class.builder()
                        .parentCouncil(ParentCouncil.builder().build())
                        .build())
                .build();
        ParentCouncilDto parentCouncilDto = ParentCouncilDto.builder().id(1L).build();;

        when(teacherRepository.findById(any())).thenReturn(java.util.Optional.ofNullable(teacher));
        when(parentCouncilToParentCouncilDto.convert(any())).thenReturn(parentCouncilDto);

        ParentCouncilDto parentCouncilDto1 = formTutorService.findParentCouncil(teacherId);

        assertNotNull(parentCouncilDto1);
        verify(teacherRepository, times(1)).findById(teacherId);
    }

    @Test
    void deleteParentCouncil() {
        Long teacherId = 1L;

        Teacher teacher = Teacher.builder()
                .id(teacherId)
                .schoolClass(Class.builder()
                        .parentCouncil(ParentCouncil.builder().build())
                        .build())
                .build();

        when(teacherRepository.findById(any())).thenReturn(java.util.Optional.ofNullable(teacher));

        Boolean result = formTutorService.deleteParentCouncil(teacherId);

        assertTrue(result);
        verify(teacherRepository, times(1)).findById(teacherId);
    }


    @Test
    void removeParentFromCouncil() {
        Long teacherId = 1L;
        Long parentId = 1L;

        ParentCouncil parentCouncil = ParentCouncil.builder()
                .id(1L)
                .parents(new HashSet<>(){{
                    add(Parent.builder().id(parentId).build());
                    add(Parent.builder().id(2L).build());
                }})
                .build();

        ParentCouncilDto parentCouncilDto = ParentCouncilDto.builder()
                .id(1L)
                .parentsId(new ArrayList<>(){{
                    add(parentId);
                    add(2L);
                }})
                .build();

        when(parentRepository.findById(any())).thenReturn(java.util.Optional.ofNullable(Parent.builder().build()));
        when(parentCouncilDtoToParentCouncil.convert(any())).thenReturn(parentCouncil);
        when(parentCouncilToParentCouncilDto.convert(any())).thenReturn(parentCouncilDto);
        when(parentCouncilRepository.save(any())).thenReturn(parentCouncil);


        ParentCouncilDto removedParentCouncilDto = formTutorService
                .removeParentFromCouncil(parentCouncilDto, parentId);


        assertNotNull(removedParentCouncilDto);
        verify(parentRepository, times(1)).findById(teacherId);
        verify(parentCouncilRepository, times(1)).save(parentCouncil);
    }


    @Test
    void listBehaviorGrades() {
        Long teacherId = 1L;
        Long schoolClassId = 1L;

        Teacher teacher = Teacher.builder()
                .id(teacherId)
                .schoolClass(Class.builder().id(schoolClassId).build())
                .build();

        Grade grade = Grade.builder().build();

        when(teacherRepository.findById(any())).thenReturn(java.util.Optional.ofNullable(teacher));
        when(gradeToGradeDto.convert(any())).thenReturn(GradeDto.builder().build());
        when(gradeRepository.findAllByTeacherIdAndWeight(any(), any())).thenReturn(Collections.singletonList(grade));


        List<GradeDto> gradeDtoList = formTutorService.listBehaviorGrades(teacherId);

        assertNotNull(gradeDtoList);
        verify(gradeRepository, times(1)).findAllByTeacherIdAndWeight(teacherId, 10);
        verify(teacherRepository, times(1)).findById(teacherId);
        verify(gradeToGradeDto, times(1)).convert(any());
    }

    @Test
    void saveBehaviorGrade() {
        Long teacherId = 1L;

        Teacher teacher = Teacher.builder().id(1L).build();
        Grade grade = Grade.builder().id(1L).build();
        GradeDto gradeDto = GradeDto.builder().id(1L).build();;

        when(teacherRepository.findById(any())).thenReturn(java.util.Optional.ofNullable(teacher));
        when(gradeToGradeDto.convert(any())).thenReturn(gradeDto);
        when(gradeDtoToGrade.convert(any())).thenReturn(grade);
        when(gradeRepository.save(any())).thenReturn(grade);

        Grade savedGrade = formTutorService.saveBehaviorGrade(teacherId, gradeDto);

        assertNotNull(savedGrade);
        verify(teacherRepository, times(1)).findById(teacherId);
        verify(gradeDtoToGrade, times(1)).convert(gradeDto);
        verify(gradeToGradeDto, times(0)).convert(any());
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

    @Test
    void listClassParents() {
        Long teacherId = 1L;
        Long schoolClassId = 1L;

        Teacher teacher = Teacher.builder()
                .id(teacherId)
                .schoolClass(Class.builder().id(schoolClassId).build())
                .build();


        when(teacherRepository.findById(any())).thenReturn(java.util.Optional.ofNullable(teacher));
        when(parentToParentDto.convert(any())).thenReturn(ParentDto.builder().build());
        when(studentRepository.findAllBySchoolClassId(any())).thenReturn(Collections.singletonList(Student.builder().build()));


        List<ParentDto> parentDtoList = formTutorService.listClassParents(teacherId);

        assertNotNull(parentDtoList);
        verify(studentRepository, times(1)).findAllBySchoolClassId(teacherId);
        verify(teacherRepository, times(1)).findById(teacherId);
        verify(parentToParentDto, times(1)).convert(any());
    }

}
