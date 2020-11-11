package com.ediary.DTO;

import lombok.*;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClassDto {

    private Long id;
    private String name;

    private Long studentCouncilId;

    private String teacherName;
    private Long teacherId;

    private Long parentCouncilId;

    private List<StudentDto> students;

    private List<Long> eventsId;

    private List<Long> schoolPeriodsId;

    private List<Long> lessonsId;
}
