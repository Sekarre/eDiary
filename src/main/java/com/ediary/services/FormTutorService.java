package com.ediary.services;

import com.ediary.domain.Grade;
import com.ediary.domain.ParentCouncil;
import com.ediary.domain.StudentCard;
import com.ediary.domain.StudentCouncil;


public interface FormTutorService {

    StudentCouncil saveStudentCouncil(StudentCouncil studentCouncil);
    StudentCouncil findStudentCouncil(Long schoolClassId);

    ParentCouncil saveParentCouncil(ParentCouncil parentCouncil);
    ParentCouncil findParentCouncil(Long schoolClassId);

    StudentCard findStudentCard(Long teacherId, Long studentId);

    Grade saveGrade(Long studentId); //wyznaczenie oceny z zachowania (?)
    Grade findGrade(Long studentId); //wyswietlenie oceny z zachowania

}
