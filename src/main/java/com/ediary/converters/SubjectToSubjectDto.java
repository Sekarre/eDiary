package com.ediary.converters;

import com.ediary.DTO.SubjectDto;
import com.ediary.domain.Lesson;
import com.ediary.domain.Subject;
import com.ediary.domain.Teacher;
import lombok.Synchronized;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class SubjectToSubjectDto implements Converter<Subject, SubjectDto> {


    @Nullable
    @Synchronized
    @Override
    public SubjectDto convert(Subject source) {

        SubjectDto subjectDto = new SubjectDto();

        subjectDto.setId(source.getId());
        subjectDto.setName(source.getName());

        //Teachers
        subjectDto.setTeachersName(source.getTeachers().stream()
                .map(Teacher::getUser)
                .map(user -> user.getFirstName() + " " + user.getLastName())
                .collect(Collectors.toList()));

        subjectDto.setTeachersId(source.getTeachers().stream()
                .map(Teacher::getId)
                .collect(Collectors.toList()));


        //Lessons
        subjectDto.setLessonsName(source.getLessons().stream()
                .map(Lesson::getName)
                .collect(Collectors.toList()));

        subjectDto.setLessonsId(source.getLessons().stream()
                .map(Lesson::getId)
                .collect(Collectors.toList()));

        return subjectDto;
    }
}
