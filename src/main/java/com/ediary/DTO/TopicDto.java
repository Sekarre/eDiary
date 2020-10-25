package com.ediary.DTO;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TopicDto {

    private Long id;
    private Long number;
    private String name;
    private String description;

    //Subject
    private String subjectName;
    private Long subjectId;
}
