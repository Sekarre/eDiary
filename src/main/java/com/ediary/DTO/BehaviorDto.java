package com.ediary.DTO;

import lombok.*;

import java.util.Date;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BehaviorDto {

    private Long id;
    private Date date;
    private String content;
    private boolean positive;

    //Student
    private Long studentId;
    private String studentName;

    //Teacher
    private Long teacherId;
    private String teacherName;
}
