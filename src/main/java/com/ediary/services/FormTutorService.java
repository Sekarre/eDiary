package com.ediary.services;

import com.ediary.DTO.*;
import com.ediary.domain.Grade;
import com.ediary.domain.ParentCouncil;
import com.ediary.domain.StudentCard;
import com.ediary.domain.StudentCouncil;

import java.util.List;


public interface FormTutorService {

    StudentCouncilDto initNewStudentCouncil(Long teacherId);
    StudentCouncil saveStudentCouncil(Long teacherId, StudentCouncilDto studentCouncilDto, List<Long> studentsId);
    StudentCouncilDto findStudentCouncil(Long teacherId);
    Boolean deleteStudentCouncil(Long teacherId);
    StudentCouncilDto removeStudentFromCouncil(StudentCouncilDto studentCouncilDto, Long studentId);

    ParentCouncilDto initNewParentCouncil(Long teacherId);
    ParentCouncil saveParentCouncil(Long teacherId, ParentCouncilDto parentCouncilDto, List<Long> parentsId);
    ParentCouncilDto findParentCouncil(Long teacherId);
    Boolean deleteParentCouncil(Long teacherId);
    ParentCouncilDto removeParentFromCouncil(ParentCouncilDto parentCouncilDto, Long parentId);


    StudentCard findStudentCard(Long teacherId, Long studentId);

    List<GradeDto> listBehaviorGrades(Long teacherId);
    Grade saveBehaviorGrade(Long teacher, GradeDto gradeDto);
    Grade findGrade(Long studentId); //wyswietlenie oceny z zachowania

    List<StudentDto> listClassStudents(Long teacherId);

    List<ParentDto> listClassParents(Long teacherId);
}
