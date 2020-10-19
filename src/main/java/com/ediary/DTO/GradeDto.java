package com.ediary.DTO;

import lombok.*;

import java.util.Date;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GradeDto {

    private Long id;
    private String value;
    private Integer weight;
    private String description;
    private Date date;

    //Teacher
    private Long teacherId;
    private String teacherName;

    //Subject
    private Long subjectId;
    private String subjectName;

    //Student
    private Long studentId;
    private String studentName;
}
