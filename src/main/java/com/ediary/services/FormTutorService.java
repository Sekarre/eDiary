package com.ediary.services;

import com.ediary.DTO.StudentCouncilDto;
import com.ediary.DTO.StudentDto;
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


    ParentCouncil saveParentCouncil(ParentCouncil parentCouncil);
    ParentCouncil findParentCouncil(Long schoolClassId);

    StudentCard findStudentCard(Long teacherId, Long studentId);

    Grade saveGrade(Long studentId); //wyznaczenie oceny z zachowania (?)
    Grade findGrade(Long studentId); //wyswietlenie oceny z zachowania

    List<StudentDto> listClassStudents(Long teacherId);

}
