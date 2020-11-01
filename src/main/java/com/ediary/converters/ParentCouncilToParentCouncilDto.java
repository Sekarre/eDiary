package com.ediary.converters;

import com.ediary.DTO.ParentCouncilDto;
import com.ediary.domain.Parent;
import com.ediary.domain.ParentCouncil;
import com.ediary.domain.Student;
import lombok.Synchronized;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

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

        //Parents
        parentCouncilDto.setParentsId(source.getParents().stream()
                .map(Parent::getId)
                .collect(Collectors.toList()));


        parentCouncilDto.setParentsName(source.getParents().stream()
                .map(Parent::getUser)
                .map(user -> user.getFirstName() + " " + user.getLastName())
                .collect(Collectors.toList()));


        return parentCouncilDto;
    }
}
