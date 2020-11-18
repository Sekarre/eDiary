package com.ediary.DTO;

import com.fasterxml.jackson.annotation.JsonFormat;
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

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone="CET")
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
