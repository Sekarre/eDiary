package com.ediary.DTO;

import com.ediary.domain.Attendance;
import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AttendanceDto {

    private Long id;

    private Attendance.Status status;

    //Lesson
    private LessonDto lessonDto;

    //Student
    private Long studentId;
    private String studentName;
    private String studentClass;
}
