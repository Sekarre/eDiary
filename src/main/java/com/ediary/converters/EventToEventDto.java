package com.ediary.converters;

import com.ediary.DTO.EventDto;
import com.ediary.domain.Event;
import lombok.Synchronized;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

@Component
public class EventToEventDto implements Converter<Event, EventDto> {

    @Synchronized
    @Nullable
    @Override
    public EventDto convert(Event source) {

        if(source == null){
            return null;
        }

        final EventDto eventdTO = new EventDto();
        eventdTO.setId(source.getId());
        eventdTO.setDescription(source.getDescription());
        eventdTO.setCreateDate(source.getCreateDate());
        eventdTO.setDate(source.getDate());
        eventdTO.setType(source.getType());

        //Class
        eventdTO.setClassId(source.getSchoolClass().getId());
        eventdTO.setClassName(source.getSchoolClass().getName());

        //Teacher
        eventdTO.setTeacherId(source.getTeacher().getId());
        eventdTO.setTeacherName(
                source.getTeacher().getUser().getFirstName() + " " + source.getTeacher().getUser().getLastName()
        );

        return eventdTO;
    }
}
