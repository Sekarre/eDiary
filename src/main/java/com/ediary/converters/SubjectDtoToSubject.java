package com.ediary.converters;

import com.ediary.DTO.SubjectDto;
import com.ediary.domain.Subject;
import com.ediary.repositories.LessonRepository;
import com.ediary.repositories.TeacherRepository;
import lombok.RequiredArgsConstructor;
import lombok.Synchronized;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import java.util.HashSet;

@RequiredArgsConstructor
@Component
public class SubjectDtoToSubject implements Converter<SubjectDto, Subject> {


    private final TeacherRepository teacherRepository;
    private final LessonRepository lessonRepository;

    @Nullable
    @Synchronized
    @Override
    public Subject convert(SubjectDto source) {

        Subject subject = new Subject();

        subject.setId(source.getId());
        subject.setName(source.getName());

        //Teachers
        subject.setTeachers(new HashSet<>(teacherRepository.findAllById(source.getTeachersId())));

        //Lessons
        subject.setLessons(new HashSet<>(lessonRepository.findAllById(source.getLessonsId())));

        return subject;
    }
}
