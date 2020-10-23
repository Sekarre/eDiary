package com.ediary.DTO;

import lombok.*;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SubjectDto {

    private Long id;
    private String name;

    private String teacherName;
    private Long teacherId;

    private String className;
    private Long classId;

    private List<String> lessonsName;
    private List<Long> lessonsId;

    private List<String> topicsName;
    private List<Long> topicsId;

}
