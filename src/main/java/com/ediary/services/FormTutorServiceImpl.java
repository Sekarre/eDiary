package com.ediary.services;

import com.ediary.DTO.*;
import com.ediary.converters.*;
import com.ediary.domain.*;
import com.ediary.domain.helpers.TimeInterval;
import com.ediary.exceptions.NotFoundException;
import com.ediary.repositories.*;
import com.ediary.services.pdf.PdfService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class FormTutorServiceImpl implements FormTutorService {

    private final PdfService pdfService;

    private final TeacherRepository teacherRepository;
    private final StudentCouncilRepository studentCouncilRepository;
    private final StudentRepository studentRepository;
    private final ParentRepository parentRepository;
    private final ParentCouncilRepository parentCouncilRepository;
    private final GradeRepository gradeRepository;

    private final StudentCouncilDtoToStudentCouncil studentCouncilDtoToStudentCouncil;
    private final StudentCouncilToStudentCouncilDto studentCouncilToStudentCouncilDto;
    private final ParentCouncilDtoToParentCouncil parentCouncilDtoToParentCouncil;
    private final ParentCouncilToParentCouncilDto parentCouncilToParentCouncilDto;
    private final StudentToStudentDto studentToStudentDto;
    private final ParentToParentDto parentToParentDto;
    private final GradeToGradeDto gradeToGradeDto;
    private final GradeDtoToGrade gradeDtoToGrade;


    @Override
    public StudentCouncilDto initNewStudentCouncil(Long teacherId) {
        Teacher teacher = getTeacherById(teacherId);

        return StudentCouncilDto.builder()
                .schoolClassId(teacher.getSchoolClass().getId())
                .schoolClassName(teacher.getSchoolClass().getName())
                .build();
    }

    @Override
    public StudentCouncil saveStudentCouncil(Long teacherId, StudentCouncilDto studentCouncilDto, List<Long> studentsId) {
        Teacher teacher = getTeacherById(teacherId);

        StudentCouncil studentCouncil = studentCouncilDtoToStudentCouncil.convert(studentCouncilDto);

        if (studentCouncil != null) {
            if (studentCouncil.getStudents().size() > 0) {
                studentCouncil.getStudents().addAll(studentRepository.findAllById(studentsId));
            } else {
                studentCouncil.setStudents(new HashSet<>(studentRepository.findAllById(studentsId)));
            }
            return studentCouncilRepository.save(studentCouncil);

        } else {
            throw new NotFoundException("Students not found");
        }
    }

    @Override
    public StudentCouncilDto findStudentCouncil(Long teacherId) {
        Teacher teacher = getTeacherById(teacherId);

        return studentCouncilToStudentCouncilDto.convert(teacher.getSchoolClass().getStudentCouncil());
    }

    @Override
    public Boolean deleteStudentCouncil(Long teacherId) {
        Teacher teacher = getTeacherById(teacherId);

        StudentCouncil studentCouncil = teacher.getSchoolClass().getStudentCouncil();

        if (studentCouncil != null) {
            studentCouncilRepository.delete(studentCouncil);
            return true;
        }

        return false;
    }

    @Override
    public StudentCouncilDto removeStudentFromCouncil(StudentCouncilDto studentCouncilDto, Long studentId) {
        StudentCouncil studentCouncil = studentCouncilDtoToStudentCouncil.convert(studentCouncilDto);

        if ((studentCouncil != null) && (studentCouncil.getStudents().stream().anyMatch(s -> s.getId().equals(studentId)))) {

            Student student = studentRepository
                    .findById(studentId).orElseThrow(() -> new NotFoundException("Student not found"));

            studentCouncil.setStudents(studentCouncil.getStudents().stream()
                    .filter(s -> !(s.getId().equals(studentId)))
                    .collect(Collectors.toSet()));


            return studentCouncilToStudentCouncilDto.convert(studentCouncilRepository.save(studentCouncil));
        }

        return studentCouncilDto;
    }

    @Override
    public ParentCouncilDto initNewParentCouncil(Long teacherId) {
        Teacher teacher = getTeacherById(teacherId);

        return ParentCouncilDto.builder()
                .schoolClassId(teacher.getSchoolClass().getId())
                .schoolClassName(teacher.getSchoolClass().getName())
                .build();
    }

    @Override
    public ParentCouncil saveParentCouncil(Long teacherId, ParentCouncilDto parentCouncilDto, List<Long> parentsId) {
        Teacher teacher = getTeacherById(teacherId);

        ParentCouncil parentCouncil = parentCouncilDtoToParentCouncil.convert(parentCouncilDto);

        if (parentCouncil != null) {
            if (parentCouncil.getParents().size() > 0) {
                parentCouncil.getParents().addAll(parentRepository.findAllById(parentsId));
            } else {
                parentCouncil.setParents(new HashSet<>(parentRepository.findAllById(parentsId)));
            }
            return parentCouncilRepository.save(parentCouncil);

        } else {
            throw new NotFoundException("Parents not found");
        }
    }

    @Override
    public ParentCouncilDto findParentCouncil(Long teacherId) {
        Teacher teacher = getTeacherById(teacherId);

        return parentCouncilToParentCouncilDto.convert(teacher.getSchoolClass().getParentCouncil());
    }

    @Override
    public Boolean deleteParentCouncil(Long teacherId) {
        Teacher teacher = getTeacherById(teacherId);

        ParentCouncil parentCouncil = teacher.getSchoolClass().getParentCouncil();

        if (parentCouncil != null) {
            parentCouncilRepository.delete(parentCouncil);
            return true;
        }
        return false;
    }

    @Override
    public ParentCouncilDto removeParentFromCouncil(ParentCouncilDto parentCouncilDto, Long parentId) {
        ParentCouncil parentCouncil = parentCouncilDtoToParentCouncil.convert(parentCouncilDto);

        if ((parentCouncil != null) && (parentCouncil.getParents().stream().anyMatch(s -> s.getId().equals(parentId)))) {

            Parent parent = parentRepository
                    .findById(parentId).orElseThrow(() -> new NotFoundException("Student not found"));

            parentCouncil.setParents(parentCouncil.getParents().stream()
                    .filter(s -> !(s.getId().equals(parentId)))
                    .collect(Collectors.toSet()));


            return parentCouncilToParentCouncilDto.convert(parentCouncilRepository.save(parentCouncil));
        }

        return parentCouncilDto;
    }

    @Override
    public StudentCard findStudentCard(Long teacherId, Long studentId) {
        return null;
    }

    @Override
    public Boolean createStudentCard(HttpServletResponse response, Long studentId, Date startTime, Date endTime) throws Exception {

        if (response == null) {
            return false;
        }

        if (startTime.toLocalDate().isAfter(endTime.toLocalDate())) {
            return false;
        }

        Student student = studentRepository
                .findById(studentId).orElseThrow(() -> new NotFoundException("Student not found"));

        response.setContentType("application/pdf");
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=student_card_" + studentId + ".pdf";
        response.setHeader(headerKey, headerValue);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");

        String timeInterval = simpleDateFormat.format(startTime) + " - " + simpleDateFormat.format(endTime);

        //+1 day, cuz dateBefore in repos doesnt count that day in select query
        //simpler was '<', now is '<='
        Date correctedEndTime = Date.valueOf(endTime.toLocalDate().plusDays(1));

        //same here
        Date correctedStartTime = Date.valueOf(startTime.toLocalDate().minusDays(1));


        return pdfService.createStudentCardPdf(response, getStudentsGradesWithSubjects(student, correctedStartTime, correctedEndTime),
                student, getAttendancesNumber(student, correctedStartTime, correctedEndTime), timeInterval);
    }

    /**
     * Assuming weight of behavior grade is 9000, will be refactored to enum
     **/
    @Override
    public List<GradeDto> listBehaviorGrades(Long teacherId) {
        Teacher teacher = getTeacherById(teacherId);


        return gradeRepository.findAllByTeacherIdAndWeight(teacherId, 9000)
                .stream()
                .map(gradeToGradeDto::convert)
                .collect(Collectors.toList());
    }

    @Override
    public Grade saveBehaviorGrade(Long teacherId, GradeDto gradeDto) {
        getTeacherById(teacherId);

        Grade grade = gradeDtoToGrade.convert(gradeDto);

        if (grade != null) {
            grade.setWeight(9000);
            return gradeRepository.save(grade);
        }

        return gradeDtoToGrade.convert(gradeDto);
    }

    @Override
    public Grade findGrade(Long studentId) {
        return null;
    }

    @Override
    public List<StudentDto> listClassStudents(Long teacherId) {
        Teacher teacher = getTeacherById(teacherId);

        return studentRepository.findAllBySchoolClassId(teacher.getSchoolClass().getId())
                .stream()
                .map(studentToStudentDto::convert)
                .collect(Collectors.toList());
    }

    @Override
    public List<ParentDto> listClassParents(Long teacherId) {
        Teacher teacher = getTeacherById(teacherId);

        List<Student> students =
                new ArrayList<>(studentRepository.findAllBySchoolClassId(teacher.getSchoolClass().getId()));

        Set<Parent> parents = students.stream()
                .map(Student::getParent)
                .collect(Collectors.toSet());

        return parents.stream()
                .map(parentToParentDto::convert)
                .collect(Collectors.toList());

    }

    @Override
    public TimeInterval initNewTimeInterval() {
        return TimeInterval.builder()
                .startTime(Date.valueOf(LocalDate.now().minusYears(1)))
                .endTime(Date.valueOf(LocalDate.now()))
                .build();
    }

    private Teacher getTeacherById(Long teacherId) {
        return teacherRepository
                .findById(teacherId).orElseThrow(() -> new NotFoundException("Teacher not found"));
    }

    private Map<String, List<Grade>> getStudentsGradesWithSubjects(Student student, Date startTime, Date endTime) {

        Map<String, List<Grade>> gradesWithSubjects = new TreeMap<>();

        student.getSchoolClass().getSubjects()
                .forEach(subject -> gradesWithSubjects.put(subject.getName(),
                        gradeRepository.findAllByStudentIdAndSubjectIdAndDateAfterAndDateBefore(
                                student.getId(), subject.getId(), startTime, endTime)));

        return gradesWithSubjects;
    }

    private Map<String, Long> getAttendancesNumber(Student student, Date startTime, Date endTime) {

        Map<String, Long> attendancesNumber = new HashMap<>();

//Old version, may be useful
//        List<Attendance> attendanceList  = student.getAttendance()
//                .stream()
//                .filter(attendance -> attendance.getStatus().equals(Attendance.Status.ABSENT)
//                        || attendance.getStatus().equals(Attendance.Status.UNEXCUSED)
//                        || attendance.getStatus().equals(Attendance.Status.EXCUSED))
//                .collect(Collectors.toList());
//
//        Long excusedAttendances = attendanceList
//                .stream()
//                .filter(attendance ->attendance.getStatus().equals(Attendance.Status.EXCUSED))
//                .count();

        List<Attendance> attendanceList = student.getAttendance()
                .stream()
                .filter(attendance -> attendance.getStatus().equals(Attendance.Status.ABSENT)
                        || attendance.getStatus().equals(Attendance.Status.UNEXCUSED)
                        || attendance.getStatus().equals(Attendance.Status.EXCUSED))
                .filter(attendance -> attendance.getLesson().getDate().before(endTime)
                        && attendance.getLesson().getDate().after(startTime))
                .collect(Collectors.toList());

        Long excusedAttendances = attendanceList
                .stream()
                .filter(attendance -> attendance.getStatus().equals(Attendance.Status.EXCUSED))
                .count();


        attendancesNumber.put("total", (long) attendanceList.size());
        attendancesNumber.put("excused", excusedAttendances);

        return attendancesNumber;
    }
}
