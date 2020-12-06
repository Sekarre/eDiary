package com.ediary.services;

import com.ediary.DTO.ClassDto;
import com.ediary.DTO.StudentDto;
import com.ediary.DTO.TeacherDto;
import com.ediary.domain.Class;

import java.util.List;

public interface DeputyHeadService {

    ClassDto initNewClass();
    Class saveClass(ClassDto schoolClassDto, List<Long> studentsId);
    Class saveClass(ClassDto schoolClassDto);
    Boolean deleteClass(Long schoolClassId);
    List<ClassDto> listAllClasses();
    ClassDto getSchoolClass(Long classId);

    Boolean schoolClassNameIsUnique(String schoolClassName);


    ClassDto removeFormTutorFromClass(Long classId, Long teacherId);
    ClassDto removeStudentFromClass(Long classId, Long studentId);
    ClassDto addFormTutorToClass(Long classId, Long teacherId);
    ClassDto addStudentToClass(Long classId, Long studentId);

    List<StudentDto> listAllStudentsWithoutClass(Integer page, Integer size);

    List<TeacherDto> listAllTeachersWithoutClass(Integer page, Integer size);
    TeacherDto findTeacher(Long teacherId, Long classId);
    String findTeacher(Long teacherId);

    Integer countStudentsWithoutClass();
}
