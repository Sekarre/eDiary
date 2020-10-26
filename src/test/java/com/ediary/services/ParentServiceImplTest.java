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

    @Mock
    StudentToStudentDto studentToStudentDto;

    @Mock
    AttendanceDtoToAttendance attendanceDtoToAttendance;

    @Mock
    ParentToParentDto parentToParentDto;

    ParentService parentService;

    @BeforeEach
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        parentService = new ParentServiceImpl(studentRepository, attendanceRepository, parentRepository,
                studentToStudentDto, attendanceDtoToAttendance, parentToParentDto);
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
        when(studentToStudentDto.convertForParent(student)).thenReturn(StudentDto.builder().id(student.getId()).build());

        List<StudentDto> returnedStudents = parentService.listStudents(parentId);

        assertNotNull(returnedStudents);
        assertEquals(student.getId(), returnedStudents.get(0).getId());
        verify(studentRepository, times(1)).findAllByParentId(parentId);
        verify(studentToStudentDto, times(1)).convertForParent(student);
    }

    @Test
    void findByStudent() {
        Student student = Student.builder()
                .id(1L)
                .build();

        Parent parent = Parent.builder()
                .id(1L)
                .students(new HashSet<>(Collections.singleton(student)))
                .build();


        when(parentRepository.findById(any())).thenReturn(Optional.of(parent));
        when(studentToStudentDto.convertForParent(any())).thenReturn(StudentDto.builder().id(student.getId()).build());
        when(studentRepository.findById(any())).thenReturn(Optional.of(student));

        StudentDto returnedStudent = parentService.findStudent(parent.getId(), student.getId());

        assertNotNull(returnedStudent);
        verify(parentRepository, times(1)).findById(any());
        verify(studentToStudentDto, times(1)).convertForParent(student);
    }

    @Test
    void saveAttendance() {
        AttendanceDto attendanceToSave = AttendanceDto.builder()
                .id(1L)
                .build();

        Attendance attendanceSaved = Attendance.builder().id(1L).build();

        when(attendanceDtoToAttendance.convert(attendanceToSave)).thenReturn(attendanceSaved);
        when(attendanceRepository.save(attendanceSaved)).thenReturn(attendanceSaved);

        Attendance returnedAttendance = parentService.saveAttendance(attendanceToSave);

        assertNotNull(returnedAttendance);
        assertEquals(attendanceToSave.getId(), returnedAttendance.getId());
        verify(attendanceRepository, times(1)).save(any());
        verify(attendanceDtoToAttendance, times(1)).convert(attendanceToSave);
    }

    @Test
    void findByUser() {
        Long userId = 24L;
        User user = User.builder().id(userId).build();

        Parent parentReturned = Parent.builder().id(parentId).build();

        when(parentRepository.findByUser(user)).thenReturn(Optional.of(parentReturned));
        when(parentToParentDto.convert(parentReturned)).thenReturn(ParentDto.builder().id(parentReturned.getId()).build());

        ParentDto parent = parentService.findByUser(user);

        assertEquals(parent.getId(), parentReturned.getId());
        verify(parentRepository, times(1)).findByUser(user);
        verify(parentToParentDto, times(1)).convert(parentReturned);
    }

}