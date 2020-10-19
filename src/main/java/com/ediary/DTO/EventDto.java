package com.ediary.DTO;

import com.ediary.domain.Event;
import lombok.*;

import java.util.Date;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EventDto {

    private Long id;
    private String description;
    private Date createDate;
    private Date date;
    private Event.Type type;

    //Class
    private Long classId;
    private String className;

    //Teacher
    private Long teacherId;
    private String teacherName;
}
