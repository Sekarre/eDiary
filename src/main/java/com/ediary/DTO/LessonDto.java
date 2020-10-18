package com.ediary.DTO;

import lombok.*;

import java.util.Date;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LessonDto {

    private Long id;
    private String name;
    private Date date;

    //Class
    private Long classId;
    private String className;

    //Topic
    private Long topicId;
    private String topicName;

    //Subject
    private Long subjectId;
    private String subjectName;


}
