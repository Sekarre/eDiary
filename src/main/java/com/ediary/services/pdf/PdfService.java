package com.ediary.services.pdf;

import com.ediary.domain.Grade;
import com.ediary.domain.Student;
import com.ediary.domain.Teacher;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

public interface PdfService {

    Boolean createStudentCardPdf(HttpServletResponse response,
                                 Map<String, List<Grade>> gradesWithSubjects, Student student,
                                 Map<String, Long> attendanceNumber, String timeInterval) throws Exception;

    Boolean createReportPdf(HttpServletResponse response, Teacher teacher, String timeInterval, Integer lessonsNumber,
                            String subjectsNames, Long gradesNumber, Long eventsNumber) throws Exception;
}
