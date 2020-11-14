package com.ediary.converters;

import com.ediary.DTO.ClassDto;
import com.ediary.DTO.StudentDto;
import com.ediary.domain.Class;
import com.ediary.domain.SchoolPeriod;
import com.ediary.repositories.*;
import lombok.Synchronized;
import org.springframework.core.convert.converter.Converter;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.HashSet;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
public class ClassDtoToClass implements Converter<ClassDto, Class> {

    private final EventRepository eventRepository;
    private final ParentCouncilRepository parentCouncilRepository;
    private final StudentCouncilRepository studentCouncilRepository;
    private final TeacherRepository teacherRepository;
    private final StudentRepository studentRepository;
    private final SchoolPeriodRepository schoolPeriodRepository;
    private final LessonRepository lessonRepository;

    private final StudentDtoToStudent studentDtoToStudent;


    @Override
    @Nullable
    @Synchronized
    public Class convert(ClassDto source) {

        if (source == null) {
            return null;
        }

        final Class schoolClass = new Class();
        schoolClass.setId(source.getId());
        schoolClass.setName(source.getName());

        //Student Council
        schoolClass.setStudentCouncil(studentCouncilRepository.findById(source.getParentCouncilId()).orElse(null));

        //Teacher
        schoolClass.setTeacher(teacherRepository.findById(source.getTeacherId()).orElse(null));

        //Parent Council
        schoolClass.setParentCouncil(parentCouncilRepository.findById(source.getParentCouncilId()).orElse(null));

        //Students
        schoolClass.setStudents(source.getStudents()
                .stream()
                .map(studentDtoToStudent::convert)
                .collect(Collectors.toSet()));

        //Events
        schoolClass.setEvents(new HashSet<>(eventRepository.findAllById(source.getEventsId())));

        //School Periods
        schoolClass.setSchoolPeriods(new HashSet<>(schoolPeriodRepository.findAllById(source.getSchoolPeriodsId())));

        //Lessons
        schoolClass.setLessons(new HashSet<>(lessonRepository.findAllById(source.getLessonsId())));

        return schoolClass;
    }

    @Nullable
    @Synchronized
    public Class convertForNewClass(ClassDto source) {
        if (source == null) {
            return null;
        }


        final Class schoolClass = new Class();

        schoolClass.setName(source.getName());
        schoolClass.setTeacher(teacherRepository.findById(source.getTeacherId()).orElse(null));

        return schoolClass;
    }

}
