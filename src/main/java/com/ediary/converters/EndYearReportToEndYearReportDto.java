package com.ediary.converters;

import com.ediary.DTO.EndYearReportDto;
import com.ediary.domain.EndYearReport;
import lombok.RequiredArgsConstructor;
import lombok.Synchronized;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class EndYearReportToEndYearReportDto implements Converter<EndYearReport, EndYearReportDto> {



    @Nullable
    @Synchronized
    @Override
    public EndYearReportDto convert(EndYearReport source) {

        if (source == null) {
            return null;
        }

        final EndYearReportDto dto = new EndYearReportDto();

        dto.setId(source.getId());
        dto.setYear(source.getYear());

        //Teacher
        if (source.getUserType() == EndYearReport.Type.TEACHER && source.getTeacher() != null) {
            dto.setTeacherId(source.getTeacher().getId());
            dto.setTeacherName(source.getTeacher().getUser().getFirstName() + " " + source.getTeacher().getUser().getLastName());
        }

        if (source.getUserType() == EndYearReport.Type.STUDENT && source.getStudent() != null) {
            dto.setStudentId(source.getStudent().getId());
            dto.setStudentName(source.getStudent().getUser().getFirstName() + " " + source.getStudent().getUser().getLastName());
        }

        return dto;
    }
}
