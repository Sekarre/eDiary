package com.ediary.converters;

import com.ediary.DTO.ParentCouncilDto;
import com.ediary.domain.ParentCouncil;
import lombok.Synchronized;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

@Component
public class ParentCouncilToParentCouncilDto implements Converter<ParentCouncil, ParentCouncilDto> {

    @Nullable
    @Synchronized
    @Override
    public ParentCouncilDto convert(ParentCouncil source) {

        if (source == null) {
            return null;
        }

        ParentCouncilDto parentCouncilDto = new ParentCouncilDto();

        parentCouncilDto.setId(source.getId());

        //Class
        parentCouncilDto.setSchoolClassName(source.getSchoolClass().getName());
        parentCouncilDto.setSchoolClassId(source.getSchoolClass().getId());

        return parentCouncilDto;
    }
}
