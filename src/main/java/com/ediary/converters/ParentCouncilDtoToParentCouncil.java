package com.ediary.converters;

import com.ediary.DTO.ParentCouncilDto;
import com.ediary.domain.ParentCouncil;
import com.ediary.repositories.ClassRepository;
import lombok.RequiredArgsConstructor;
import lombok.Synchronized;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class ParentCouncilDtoToParentCouncil implements Converter<ParentCouncilDto, ParentCouncil> {

    private final ClassRepository classRepository;

    @Nullable
    @Synchronized
    @Override
    public ParentCouncil convert(ParentCouncilDto source) {

        if (source == null) {
            return null;
        }

        ParentCouncil parentCouncil = new ParentCouncil();

        parentCouncil.setId(source.getId());

        //Class
        parentCouncil.setSchoolClass(classRepository.findById(source.getId()).orElse(null));

        return parentCouncil;
    }
}
