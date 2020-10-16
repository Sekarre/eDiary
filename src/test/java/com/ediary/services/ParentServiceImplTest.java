package com.ediary.services;

import com.ediary.domain.Attendance;
import com.ediary.domain.Parent;
import com.ediary.domain.Student;
import com.ediary.repositories.AttendanceRepository;
import com.ediary.repositories.ParentRepository;
import com.ediary.repositories.StudentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ParentServiceImplTest {

    private final Long parentId = 1L;

    @Mock
    StudentRepository studentRepository;

    @Mock
    AttendanceRepository attendanceRepository;

    @Mock
    ParentRepository parentRepository;

    ParentService parentService;

    @BeforeEach
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        parentService = new ParentServiceImpl(studentRepository, attendanceRepository, parentRepository);
    }


    @Test
    void listStudents() {
        Parent parent = Parent.builder()
                .id(1L)
                .build();

        Student student = Student.builder()
                .id(1L)
                .parent(Parent.builder().id(parentId).build())
                .build();

        parent.setStudents(new HashSet<>(Collections.singleton(student)));

        when(studentRepository.findAllByParentId(anyLong())).thenReturn(Collections.singletonList(student));
        when(parentRepository.findById(parentId)).thenReturn(Optional.of(parent));

        List<Student> returnedStudents = parentService.listStudents(parentId);

        assertNotNull(returnedStudents);
        assertTrue(parent.getStudents().contains(returnedStudents.get(0)));
        verify(studentRepository, times(1)).findAllByParentId(parentId);
    }

    @Test
    void saveAttendance() {
        Attendance attendance = Attendance.builder()
                .id(1L)
                .build();

        when(attendanceRepository.save(any())).thenReturn(attendance);

        Attendance returnedAttendance = parentService.saveAttendance(attendance);

        assertNotNull(returnedAttendance);
        verify(attendanceRepository, times(1)).save(attendance);
    }

}