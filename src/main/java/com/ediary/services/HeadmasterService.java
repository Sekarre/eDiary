package com.ediary.services;

import com.ediary.DTO.TeacherDto;
import com.ediary.domain.Report;
import com.ediary.domain.helpers.TimeInterval;

import javax.servlet.http.HttpServletResponse;
import java.io.FileNotFoundException;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

public interface HeadmasterService {

    Report saveReport(Report report);

    List<TeacherDto> listAllTeachers(Integer page, Integer size);
    TimeInterval initNewTimeInterval();
    TimeInterval setTimeInterval(LocalDate startTime, LocalDate endTime);

    Boolean createTeacherReport(HttpServletResponse response, Long teacherId, Date startTime, Date endTime) throws Exception;

    //Better dont touch now
//    Boolean performYearClosing();

    //Test
    Boolean savePdfToDatabaseTest();
    void getPdf(HttpServletResponse response) throws Exception ;
}
