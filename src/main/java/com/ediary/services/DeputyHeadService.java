package com.ediary.services;

import com.ediary.DTO.ClassDto;
import com.ediary.DTO.StudentDto;
import com.ediary.DTO.TeacherDto;
import com.ediary.domain.Class;

import java.util.List;

public interface DeputyHeadService {

    ClassDto initNewClass();
    Class saveClass(ClassDto schoolClassDto, List<Long> studentsId);
    Boolean deleteClass(Long schoolClassId);
    List<ClassDto> listAllClasses();
    ClassDto getSchoolClass(Long classId);

    List<StudentDto> listAllStudentsWithoutClass();

    List<TeacherDto> listAllTeachersWithoutClass();
}
