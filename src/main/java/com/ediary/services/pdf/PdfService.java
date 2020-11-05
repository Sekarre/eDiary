package com.ediary.services.pdf;

import com.ediary.domain.Grade;
import com.ediary.domain.Student;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

public interface PdfService {

    Boolean createStudentCardPdf(HttpServletResponse response,
                                 Map<String, List<Grade>> gradesWithSubjects, Student student,
                                 Map<String, Long> attendanceNumber, String timeInterval) throws Exception;

    Boolean createReportPdf(HttpServletResponse response, Long teacherId) throws Exception;
}
