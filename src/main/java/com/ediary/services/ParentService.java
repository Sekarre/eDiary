package com.ediary.services;

import com.ediary.domain.*;

import java.util.List;

public interface ParentService {

    List<Student> listStudents(Long parentId);
    Attendance saveAttendance(Attendance attendance);
}
