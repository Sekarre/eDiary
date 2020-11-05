package com.ediary.services.pdf;

import com.ediary.domain.Grade;
import com.ediary.domain.Student;
import com.itextpdf.io.font.FontProgram;
import com.itextpdf.io.font.FontProgramFactory;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.draw.SolidLine;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.*;
import com.itextpdf.layout.property.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Component
public class PdfServiceImpl implements PdfService {

    @Override
    public Boolean createStudentCardPdf(HttpServletResponse response,
                                        Map<String, List<Grade>> gradesWithSubjects, Student student,
                                        Map<String, Long> attendanceNumber, String timeInterval) throws Exception {
        PdfWriter writer = new PdfWriter(response.getOutputStream());
        PdfDocument pdf = new PdfDocument(writer);
        Document doc = new Document(pdf);


        PdfFont normalTextFont;

        try {
            FontProgram fontProgram = FontProgramFactory.createFont();
            normalTextFont = PdfFontFactory.createFont(fontProgram, "Cp1257");
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        //Date
        Table table = getNewTable(new float[]{100});
        table.addCell(getCell(LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")), TextAlignment.RIGHT,
                10f, normalTextFont));
        doc.add(table);


        //Student Name and class + separator
        table = getNewTable(new float[]{100});
        table.addCell(getCell(student.getUser().getFirstName() + " " + student.getUser().getLastName() + "\t" +
                student.getSchoolClass().getName(), TextAlignment.LEFT, 15f, normalTextFont));

        doc.add(table);
        doc.add(new LineSeparator(new SolidLine(3)));


        //Main title
        table = getNewTable(new float[]{100});

        table.addCell(getCell("\n" + "Wykaz ocen",
                TextAlignment.CENTER, 22f, normalTextFont));

        table.addCell(getCell("Przedział: " + timeInterval + "\n\n\n\n", TextAlignment.CENTER, 13f, normalTextFont));

        doc.add(table);

        //Grades
        table = getNewTable(new float[]{40, 60});
        table.addCell(getHeaderCell("Przedmiot", TextAlignment.CENTER, 12f, normalTextFont));
        table.addCell(getHeaderCell("Oceny", TextAlignment.CENTER, 12f, normalTextFont));
        Paragraph paragraph;


        for (Map.Entry<String, List<Grade>> entry : gradesWithSubjects.entrySet()) {

            //subject name
            paragraph = new Paragraph(entry.getKey());
            paragraph.setFont(normalTextFont);
            table.addCell(paragraph);

            //all grades for subject
            paragraph = new Paragraph();
            for (Grade grade : entry.getValue()) {
                if (grade != null) {
                    paragraph.add(grade.getValue() + ", ");
                }
            }
            table.addCell(paragraph);
        }

        table.addCell(getCell("\n\n", TextAlignment.CENTER, 12f, normalTextFont));
        doc.add(table);


        //Behavior
        table = getNewTable(new float[]{40, 60});
        table.addCell(getCell("Zachowanie: ", TextAlignment.CENTER, 12f, normalTextFont));
        table.addCell(getCell("*Ocena z wartoscia 9000*", TextAlignment.RIGHT, 12f, normalTextFont));
        doc.add(table);

        //Attendances
        table = getNewTable(new float[]{40, 60});
        table.addCell(getCell("Nieobecności: ", TextAlignment.CENTER, 12f, normalTextFont));
        table.addCell(getCell("- ogółem: " + attendanceNumber.get("total"), TextAlignment.CENTER, 12f, normalTextFont));
        table.addCell(getCell("", TextAlignment.CENTER, 12f, normalTextFont));
        table.addCell(getCell("- w tym usprawiedliwione: " + attendanceNumber.get("excused"),
                TextAlignment.CENTER, 12f, normalTextFont));
        doc.add(table);

        doc.close();

        return true;
    }

    @Override
    public Boolean createReportPdf(HttpServletResponse response, Long teacherId) {
        return null;
    }


    private Cell getCell(String text, TextAlignment alignment, Float fontSize, PdfFont normalTextFont) {
        Cell cell = new Cell().add(new Paragraph(text));
        cell.setPadding(0);
        cell.setFontSize(fontSize);
        cell.setTextAlignment(alignment);
        cell.setBorder(Border.NO_BORDER);
        cell.setFont(normalTextFont);
        cell.setHorizontalAlignment(HorizontalAlignment.CENTER);
        cell.setVerticalAlignment(VerticalAlignment.MIDDLE);
        return cell;
    }


    private Cell getHeaderCell(String text, TextAlignment alignment, Float fontSize, PdfFont normalTextFont) {
        Cell cell = new Cell().add(new Paragraph(text));
        cell.setPadding(0);
        cell.setFontSize(fontSize);
        cell.setBold();
        cell.setTextAlignment(alignment);
        cell.setFont(normalTextFont);
        cell.setHorizontalAlignment(HorizontalAlignment.CENTER);
        cell.setVerticalAlignment(VerticalAlignment.MIDDLE);
        return cell;
    }

    private Table getNewTable(float[] columnWidth) {
        return new Table(UnitValue.createPercentArray(columnWidth)).useAllAvailableWidth();
    }


}
