package com.ediary.DTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GradeDto {

    private Long id;

    @NotNull
    @Min(1)
    @Max(6)
    private String value;

    @NotNull
    @Min(0)
    @Max(99)
    private Integer weight;

    @Size(max = 255)
    private String description;

    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone="CET")
    @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    private Date date;

    //Teacher
    @NotNull
    private Long teacherId;
    private String teacherName;

    //Subject
    @NotNull
    private Long subjectId;
    private String subjectName;

    //Student
    @NotNull
    private Long studentId;
    private String studentName;
}
