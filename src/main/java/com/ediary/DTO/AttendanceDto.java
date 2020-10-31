package com.ediary.DTO;

import com.ediary.domain.Attendance;
import com.ediary.domain.Extenuation;
import lombok.*;

import java.util.List;

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

    //extenuation
    private List<Extenuation.Status> extenuationStatus;
    private Boolean isExcuseSent;
}
