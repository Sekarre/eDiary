package com.ediary.services;

import com.ediary.domain.Grade;
import com.ediary.repositories.GradeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;

import static org.junit.jupiter.api.Assertions.*;

class StudentServiceImplTest {

    private static Long studentId = 1L;
    private static Long gradeId = 2L;
    private static String value = "3";

    @Mock
    GradeRepository gradeRepository;

    StudentService studentService;

    Grade returnGrade;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        studentService = new StudentServiceImpl(gradeRepository);

        returnGrade = Grade.builder().id(gradeId).value(value).build();
    }

    @Test
    void listGrades() {
        when(gradeRepository.findAllByStudentId(studentId)).thenReturn(Arrays.asList(returnGrade));

        List<Grade> grades = studentService.listGrades(studentId);

        assertEquals(1, grades.size());
        assertEquals(gradeId, grades.get(0).getId());
        verify(gradeRepository, times(1)).findAllByStudentId(anyLong());
    }
}