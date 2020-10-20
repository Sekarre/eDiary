package com.ediary.converters;


import com.ediary.DTO.StudentCouncilDto;
import com.ediary.domain.Student;
import com.ediary.domain.StudentCouncil;
import lombok.Synchronized;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class StudentCouncilToStudentCouncilDto implements Converter<StudentCouncil, StudentCouncilDto> {


    @Nullable
    @Synchronized
    @Override
    public StudentCouncilDto convert(StudentCouncil source) {

        if (source == null) {
            return null;
        }

        StudentCouncilDto studentCouncilDto = new StudentCouncilDto();

        studentCouncilDto.setId(source.getId());

        //Students
        studentCouncilDto.setStudentsName(source.getStudents().stream()
                .map(Student::getUser)
                .map(user -> user.getFirstName() + " " + user.getLastName())
                .collect(Collectors.toList()));

        studentCouncilDto.setStudentsId(source.getStudents().stream()
                .map(Student::getId)
                .collect(Collectors.toList()));


        //Class
        studentCouncilDto.setSchoolClassName(source.getSchoolClass().getName());
        studentCouncilDto.setSchoolClassId(source.getSchoolClass().getId());

        return studentCouncilDto;
    }
}
