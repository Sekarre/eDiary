package com.ediary.converters;

import com.ediary.DTO.ClassDto;
import com.ediary.domain.*;
import com.ediary.domain.Class;
import lombok.RequiredArgsConstructor;
import lombok.Synchronized;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
public class ClassToClassDto implements Converter<Class, ClassDto> {

    private final StudentToStudentDto studentToStudentDto;

    @Nullable
    @Synchronized
    @Override
    public ClassDto convert(Class source) {

        if (source == null) {
            return null;
        }

        final ClassDto classDto = new ClassDto();

        classDto.setId(source.getId());
        classDto.setName(source.getName());

        //Student Council
        classDto.setStudentCouncilId(source.getStudentCouncil().getId());

        //Teacher
        classDto.setTeacherName(source.getTeacher().getUser().getFirstName() + " " + source.getTeacher().getUser().getLastName());
        classDto.setTeacherId(source.getTeacher().getId());

        //Parent Council
        classDto.setParentCouncilId(source.getParentCouncil().getId());

        //Students
        classDto.setStudents(source.getStudents()
                .stream()
                .map(studentToStudentDto::convert)
                .collect(Collectors.toList()));

        //Events
        classDto.setEventsId(source.getEvents().stream()
                .map(Event::getId)
                .collect(Collectors.toList()));

        //School Periods
        classDto.setSchoolPeriodsId(source.getSchoolPeriods().stream()
                .map(SchoolPeriod::getId)
                .collect(Collectors.toList()));

        //Lessons
        classDto.setLessonsId(source.getLessons().stream()
                .map(Lesson::getId)
                .collect(Collectors.toList()));


        return classDto;
    }
}
