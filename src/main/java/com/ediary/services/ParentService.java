package com.ediary.services;

import com.ediary.DTO.AttendanceDto;
import com.ediary.DTO.ParentDto;
import com.ediary.DTO.StudentDto;
import com.ediary.domain.*;
import com.ediary.domain.security.User;

import java.util.List;

public interface ParentService {

    List<StudentDto> listStudents(Long parentId);
    Attendance saveAttendance(AttendanceDto attendance);
    ParentDto findByUser(User user);
}
