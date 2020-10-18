package com.ediary.converters;

import com.ediary.DTO.AttendanceDto;
import com.ediary.domain.Attendance;
import lombok.RequiredArgsConstructor;
import lombok.Synchronized;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class AttendanceToAttendanceDto implements Converter<Attendance, AttendanceDto> {

    private final LessonToLessonDto lessonToLessonDto;

    @Synchronized
    @Nullable
    @Override
    public AttendanceDto convert(Attendance source) {

        if(source == null){
            return null;
        }

        final AttendanceDto attendanceDto = new AttendanceDto();
        attendanceDto.setId(source.getId());
        attendanceDto.setStatus(source.getStatus());

        //Lesson
        attendanceDto.setLessonDto(lessonToLessonDto.convert(source.getLesson()));

        //Student
        attendanceDto.setStudentId(source.getStudent().getId());
        attendanceDto.setStudentName(
                source.getStudent().getUser().getFirstName() + " " + source.getStudent().getUser().getLastName()
        );
        attendanceDto.setStudentClass(source.getStudent().getClass().getName());

        return attendanceDto;
    }
}
