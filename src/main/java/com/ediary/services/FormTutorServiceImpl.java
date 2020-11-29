package com.ediary.services;

import com.ediary.DTO.*;
import com.ediary.converters.*;
import com.ediary.domain.*;
import com.ediary.domain.Class;
import com.ediary.domain.helpers.GradeWeight;
import com.ediary.domain.helpers.TimeInterval;
import com.ediary.exceptions.NotFoundException;
import com.ediary.repositories.*;
import com.ediary.services.pdf.PdfService;
import com.sun.el.stream.Stream;
import lombok.RequiredArgsConstructor;
import org.springframework.data.util.StreamUtils;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.nio.file.AccessDeniedException;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

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
    private final SubjectRepository subjectRepository;
    private final ClassRepository classRepository;

    private final StudentCouncilDtoToStudentCouncil studentCouncilDtoToStudentCouncil;
    private final StudentCouncilToStudentCouncilDto studentCouncilToStudentCouncilDto;
    private final ParentCouncilDtoToParentCouncil parentCouncilDtoToParentCouncil;
    private final ParentCouncilToParentCouncilDto parentCouncilToParentCouncilDto;
    private final StudentToStudentDto studentToStudentDto;
    private final ParentToParentDto parentToParentDto;
    private final GradeToGradeDto gradeToGradeDto;
    private final GradeDtoToGrade gradeDtoToGrade;
    private final SubjectToSubjectDto subjectToSubjectDto;


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

        if (studentsId.size() > 3) {
            return null;
        }

        if (teacher.getSchoolClass() != null) {
            StudentCouncil studentCouncil = studentCouncilRepository.findBySchoolClassId(teacher.getSchoolClass().getId());

            if (studentCouncil != null) {
                int studentCouncilSize = studentCouncil.getStudents().size();
                for (int i = 0; i < 3 - studentCouncilSize; i++) {
                    Student student = studentRepository.findById(studentsId.get(i))
                            .orElseThrow(() -> new NotFoundException("Student not found"));

                    studentCouncil.getStudents().add(student);
                }
            } else {
                studentCouncil = StudentCouncil.builder()
                        .students(studentRepository.findAllById(studentsId))
                        .schoolClass(teacher.getSchoolClass())
                        .build();
            }

            Class classToSave = teacher.getSchoolClass();
            classToSave.setStudentCouncil(studentCouncil);

            Class savedClass = classRepository.save(classToSave);

            return savedClass.getStudentCouncil();
        }
        return null;
    }

    @Override
    public StudentCouncilDto findStudentCouncil(Long teacherId) {
        Teacher teacher = getTeacherById(teacherId);

        if (teacher.getSchoolClass() != null) {
            StudentCouncil studentCouncil = studentCouncilRepository.findBySchoolClassId(teacher.getSchoolClass().getId());

            if (studentCouncil == null) {
                return null;
            }

            return studentCouncilToStudentCouncilDto.convert(studentCouncil);
        }

        return null;
    }


    @Override
    public StudentCouncilDto removeStudentFromCouncil(StudentCouncilDto studentCouncilDto, Long teacherId, Long studentId) {
        Teacher teacher = getTeacherById(teacherId);

        StudentCouncil studentCouncil = studentCouncilRepository.findBySchoolClassId(teacher.getSchoolClass().getId());

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

        if (parentsId.size() > 3) {
            return null;
        }

        if (teacher.getSchoolClass() != null) {
            ParentCouncil parentCouncil = parentCouncilRepository.findBySchoolClassId(teacher.getSchoolClass().getId());

            if (parentCouncil != null) {
                int parentCouncilSize = parentCouncil.getParents().size();
                for (int i = 0; i < 3 - parentCouncilSize; i++) {
                    Parent parent = parentRepository.findById(parentsId.get(i))
                            .orElseThrow(() -> new NotFoundException("Parent not found"));

                    parentCouncil.getParents().add(parent);
                }
            } else {
                parentCouncil = ParentCouncil.builder()
                        .parents(parentRepository.findAllById(parentsId))
                        .schoolClass(teacher.getSchoolClass())
                        .build();
            }

            Class classToSave = teacher.getSchoolClass();
            classToSave.setParentCouncil(parentCouncil);

            Class savedClass = classRepository.save(classToSave);

            return savedClass.getParentCouncil();
        }

        return null;
    }

    @Override
    public ParentCouncilDto findParentCouncil(Long teacherId) {
        Teacher teacher = getTeacherById(teacherId);

        if (teacher.getSchoolClass() != null) {
            ParentCouncil parentCouncil = parentCouncilRepository.findBySchoolClassId(teacher.getSchoolClass().getId());

            if (parentCouncil == null) {
                return null;
            }

            return parentCouncilToParentCouncilDto.convert(parentCouncil);
        }

        return null;
    }

    @Override
    public ParentCouncilDto removeParentFromCouncil(ParentCouncilDto parentCouncilDto, Long teacherId, Long parentId) {
        Teacher teacher = getTeacherById(teacherId);

        ParentCouncil parentCouncil = parentCouncilRepository.findBySchoolClassId(teacher.getSchoolClass().getId());

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

    @Override
    public List<GradeDto> listBehaviorGrades(Long teacherId) {
        Teacher teacher = getTeacherById(teacherId);


        return gradeRepository.findAllByTeacherIdAndWeight(teacherId, GradeWeight.BEHAVIOR_GRADE.getWeight())
                .stream()
                .map(gradeToGradeDto::convert)
                .collect(Collectors.toList());
    }

    @Override
    public Grade saveBehaviorGrade(Long teacherId, GradeDto gradeDto) {
        getTeacherById(teacherId);

        Grade grade = gradeDtoToGrade.convert(gradeDto);

        if (grade != null) {
            grade.setWeight(GradeWeight.BEHAVIOR_GRADE.getWeight());
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

        if (teacher.getSchoolClass() != null) {
            StudentCouncil studentCouncil = studentCouncilRepository.findBySchoolClassId(teacher.getSchoolClass().getId());

            if (studentCouncil == null) {
                return studentRepository.findAllBySchoolClassIdOrderByUserLastName(teacher.getSchoolClass().getId())
                        .stream()
                        .map(studentToStudentDto::convert)
                        .collect(Collectors.toList());

            }

            return studentRepository.findAllBySchoolClassIdOrderByUserLastName(teacher.getSchoolClass().getId())
                    .stream()
                    .filter(student -> !studentCouncil.getStudents().contains(student))
                    .map(studentToStudentDto::convert)
                    .collect(Collectors.toList());
        }

        return null;
    }

    @Override
    public List<ParentDto> listClassParents(Long teacherId) {
        Teacher teacher = getTeacherById(teacherId);

        if (teacher.getSchoolClass() != null) {
            ParentCouncil parentCouncil = parentCouncilRepository.findBySchoolClassId(teacher.getSchoolClass().getId());

            if (parentCouncil == null) {


                return parentRepository.findAllByStudentsInOrderByUserLastName(teacher.getSchoolClass().getStudents())
                        .stream()
                        .map(parentToParentDto::convert)
                        .collect(Collectors.toList());

            }

            return parentRepository.findAllByStudentsInOrderByUserLastName(teacher.getSchoolClass().getStudents())
                    .stream()
                    .filter(parent -> !parentCouncil.getParents().contains(parent))
                    .map(parentToParentDto::convert)
                    .collect(Collectors.toList());
        }

        return null;

    }

    @Override
    public TimeInterval initNewTimeInterval() {
        return TimeInterval.builder()
                .startTime(Date.valueOf(LocalDate.now().minusYears(1)))
                .endTime(Date.valueOf(LocalDate.now()))
                .build();
    }

    @Override
    public TimeInterval setTimeInterval(LocalDate startTime, LocalDate endTime) {
        return TimeInterval.builder()
                .startTime(Date.valueOf(startTime))
                .endTime(Date.valueOf(endTime))
                .build();
    }

    @Override
    public List<SubjectDto> listAllSubjectsByClass(Long teacherId) {
        Teacher teacher = getTeacherById(teacherId);

        if (teacher.getSchoolClass() != null) {
            return subjectRepository.findAllBySchoolClassId(teacher.getSchoolClass().getId())
                    .stream()
                    .map(subjectToSubjectDto::convert)
                    .collect(Collectors.toList());
        }

        return null;
    }

    @Override
    public Map<StudentDto, List<GradeDto>> listStudentsGrades(Long teacherId, Long subjectId) {
        Teacher teacher = getTeacherById(teacherId);

        Subject subject = subjectRepository.findById(subjectId).orElseThrow(() -> new NotFoundException("Subject not found"));
        Optional<Class> optionalClass = Optional.ofNullable(teacher.getSchoolClass());

        Class schoolClass = optionalClass.orElseThrow(() -> new org.springframework.security.access.AccessDeniedException("Teacher -> school Class"));

        if (teacher.getSchoolClass() != null && schoolClass.equals(teacher.getSchoolClass())) {

            Map<StudentDto, List<GradeDto>> studentGradesListMap = new TreeMap<>(
                    ((o1, o2) -> Arrays.stream(o1.getUserName().split(" ")).skip(1).findFirst().get()
                            .compareToIgnoreCase(Arrays.stream(o2.getUserName().split(" ")).skip(1).findFirst().get())));

            schoolClass.getStudents().forEach(student -> {
                studentGradesListMap.put(studentToStudentDto.convert(student),
                        gradeRepository.findAllByTeacherIdAndSubjectIdAndStudentIdAndWeightNotIn(teacherId, subjectId,
                                student.getId(), Arrays.asList(GradeWeight.FINAL_GRADE.getWeight(), GradeWeight.BEHAVIOR_GRADE.getWeight()))
                                .stream()
                                .map(gradeToGradeDto::convert)
                                .collect(Collectors.toList()));
            });


            if (studentGradesListMap.keySet().isEmpty()) {
                return null;
            }

            return studentGradesListMap;
        }

        return null;
    }

    @Override
    public Map<StudentDto, GradeDto> listStudentsFinalGrades(Long teacherId, Long subjectId) {
        Teacher teacher = getTeacherById(teacherId);

        Subject subject = subjectRepository.findById(subjectId).orElseThrow(() -> new NotFoundException("Subject not found"));

        Optional<Class> optionalClass = Optional.ofNullable(teacher.getSchoolClass());

        Class schoolClass = optionalClass.orElseThrow(() -> new org.springframework.security.access.AccessDeniedException("Teacher -> school Class"));

        if (teacher.getSchoolClass() != null && schoolClass.equals(teacher.getSchoolClass())) {

            Map<StudentDto, GradeDto> studentFinalGradesListMap = new TreeMap<>(
                    ((o1, o2) -> Arrays.stream(o1.getUserName().split(" ")).skip(1).findFirst().get()
                            .compareToIgnoreCase(Arrays.stream(o2.getUserName().split(" ")).skip(1).findFirst().get())));

            schoolClass.getStudents().forEach(student -> {
                studentFinalGradesListMap.put(studentToStudentDto.convert(student),
                        gradeToGradeDto.convert(gradeRepository
                                .findByTeacherIdAndSubjectIdAndStudentIdAndWeight(teacherId, subjectId, student.getId(), GradeWeight.FINAL_GRADE.getWeight())));
            });

            if (studentFinalGradesListMap.keySet().isEmpty()) {
                return null;
            }

            return studentFinalGradesListMap;
        }

        return null;
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
