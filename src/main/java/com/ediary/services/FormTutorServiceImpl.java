package com.ediary.services;

import com.ediary.DTO.*;
import com.ediary.converters.*;
import com.ediary.domain.*;
import com.ediary.exceptions.NotFoundException;
import com.ediary.repositories.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class FormTutorServiceImpl implements FormTutorService {

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
    public ParentCouncil saveParentCouncil(Long teacherId, ParentCouncilDto parentCouncilDto, List<Long> parentsId){
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

    /**Assuming weight of behavior grade is 10**/
    @Override
    public List<GradeDto> listBehaviorGrades(Long teacherId) {
        Teacher teacher = getTeacherById(teacherId);


        return gradeRepository.findAllByTeacherIdAndWeight(teacherId, 10)
                .stream()
                .map(gradeToGradeDto::convert)
                .collect(Collectors.toList());
    }

    @Override
    public Grade saveBehaviorGrade(Long teacherId, GradeDto gradeDto) {
        getTeacherById(teacherId);

        Grade grade = gradeDtoToGrade.convert(gradeDto);

        if (grade != null) {
            grade.setWeight(10);
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

    private Teacher getTeacherById(Long teacherId) {
        return teacherRepository
                .findById(teacherId).orElseThrow(() -> new NotFoundException("Teacher not found"));
    }
}
