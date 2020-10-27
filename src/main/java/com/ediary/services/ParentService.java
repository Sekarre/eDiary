package com.ediary.services;

import com.ediary.DTO.AttendanceDto;
import com.ediary.DTO.ParentDto;
import com.ediary.DTO.StudentDto;
import com.ediary.DTO.SubjectDto;
import com.ediary.domain.*;
import com.ediary.domain.security.User;

import java.util.List;

public interface ParentService {

    List<StudentDto> listStudents(Long parentId);
    StudentDto findStudent(Long parentId, Long studentId);
    List<SubjectDto> getAllStudentSubjectNames(Long parentId, Long studentId);
    Attendance saveAttendance(AttendanceDto attendance);
    ParentDto findByUser(User user);
}
