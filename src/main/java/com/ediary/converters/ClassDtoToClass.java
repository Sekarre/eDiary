package com.ediary.converters;

import com.ediary.DTO.ClassDto;
import com.ediary.domain.Class;
import com.ediary.domain.SchoolPeriod;
import com.ediary.repositories.*;
import lombok.Synchronized;
import org.springframework.core.convert.converter.Converter;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import java.util.HashSet;

@RequiredArgsConstructor
@Component
public class ClassDtoToClass implements Converter<ClassDto, Class> {

    private final EventRepository eventRepository;
    private final ParentCouncilRepository parentCouncilRepository;
    private final StudentCouncilRepository studentCouncilRepository;
    private final TeacherRepository teacherRepository;
    private final StudentRepository studentRepository;
    private final SchoolPeriodRepository schoolPeriodRepository;

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
        schoolClass.setStudentCouncil(studentCouncilRepository.findById(source.getParentCouncilId()).orElse(null));
        schoolClass.setTeacher(teacherRepository.findByUserId(source.getTeacherId()));
        schoolClass.setParentCouncil(parentCouncilRepository.findById(source.getParentCouncilId()).orElse(null));
        schoolClass.setStudents(new HashSet<>(studentRepository.findAllById(source.getStudentsId())));
        schoolClass.setEvents(new HashSet<>(eventRepository.findAllById(source.getEventsId())));
        schoolClass.setSchoolPeriods(new HashSet<>(schoolPeriodRepository.findAllById(source.getSchoolPeriodsId())));

        return schoolClass;
    }
}
