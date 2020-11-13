package com.ediary.services;

import com.ediary.DTO.ClassDto;
import com.ediary.DTO.StudentDto;
import com.ediary.DTO.TeacherDto;
import com.ediary.converters.ClassDtoToClass;
import com.ediary.converters.ClassToClassDto;
import com.ediary.converters.StudentToStudentDto;
import com.ediary.converters.TeacherToTeacherDto;
import com.ediary.domain.Class;
import com.ediary.domain.Student;
import com.ediary.domain.Teacher;
import com.ediary.exceptions.NotFoundException;
import com.ediary.repositories.ClassRepository;
import com.ediary.repositories.StudentRepository;
import com.ediary.repositories.TeacherRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class DeputyHeadServiceImpl implements DeputyHeadService {

    private final ClassRepository classRepository;
    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;

    private final StudentToStudentDto studentToStudentDto;
    private final TeacherToTeacherDto teacherToTeacherDto;
    private final ClassToClassDto classToClassDto;
    private final ClassDtoToClass classDtoToClass;


    @Override
    public ClassDto initNewClass() {
        return ClassDto.builder().build();
    }

    @Override
    public Class saveClass(ClassDto schoolClassDto, List<Long> studentsId) {

        Class schoolClass = classDtoToClass.convert(schoolClassDto);

        if (schoolClass != null) {
            schoolClass.setStudents(new HashSet<>(studentRepository.findAllById(studentsId)));
            return classRepository.save(schoolClass);
        }

        return null;
    }

    @Override
    public Boolean deleteClass(Long schoolClassId) {
        Class schoolClass = getSchoolCLass(schoolClassId);

        classRepository.delete(schoolClass);

        return true;
    }

    @Override
    public List<ClassDto> listAllClasses() {
        return classRepository.findAll()
                .stream()
                .map(classToClassDto::convert)
                .collect(Collectors.toList());
    }

    @Override
    public ClassDto getSchoolClass(Long classId) {
        return classToClassDto.convert(getSchoolCLass(classId));
    }

    @Override
    public ClassDto removeFormTutorFromClass(Long classId, Long teacherId) {
        Class schoolClass = getSchoolCLass(classId);

        if (schoolClass.getTeacher().getId().equals(teacherId)) {
            schoolClass.setTeacher(null);
        }

        return classToClassDto.convert(schoolClass);
    }

    @Override
    public ClassDto removeStudentFromClass(Long classId, Long studentId) {
        Class schoolClass = getSchoolCLass(classId);
        Student studentToRemove = studentRepository.findById(studentId)
                .orElseThrow(() -> new NotFoundException("Student not found"));

        if (schoolClass.getStudents().stream().anyMatch(student -> student.getId().equals(studentId))) {
            schoolClass.setStudents(schoolClass.getStudents()
                    .stream()
                    .filter((s) -> !(s.getId().equals(studentId)))
                    .collect(Collectors.toSet()));

            studentToRemove.setSchoolClass(null);
            studentRepository.save(studentToRemove);

            classRepository.save(schoolClass);
        }

        return classToClassDto.convert(schoolClass);
    }

    @Override
    public ClassDto addFormTutorToClass(Long classId, Long teacherId) {
        Class schoolClass = getSchoolCLass(classId);

        Teacher teacher = teacherRepository.
                findById(teacherId).orElseThrow(() -> new NotFoundException("Teacher not found"));

        if (teacher.getSchoolClass() != null) {
            throw new NotFoundException("Teacher is already form tutor");
        } else {
            schoolClass.setTeacher(teacher);
        }


        return classToClassDto.convert(classRepository.save(schoolClass));
    }

    @Override
    public ClassDto addStudentToClass(Long classId, Long studentId) {
        Class schoolClass = getSchoolCLass(classId);


        if (schoolClass.getStudents().stream().noneMatch(s -> s.getId().equals(studentId))) {

            Set<Student> newStudentSet = new HashSet<>(){{
                addAll(schoolClass.getStudents());
                add(studentRepository.
                        findById(studentId).orElseThrow(() -> new NotFoundException("Student not found")));
            }};

            schoolClass.setStudents(newStudentSet);

            return classToClassDto.convert(schoolClass);
        }
        return null;
    }

    @Override
    public List<StudentDto> listAllStudentsWithoutClass() {

        return studentRepository.findAllBySchoolClassIsNull()
                .stream()
                .map(studentToStudentDto::convert)
                .collect(Collectors.toList());
    }

    @Override
    public List<TeacherDto> listAllTeachersWithoutClass() {
        return teacherRepository.findAllBySchoolClassIsNull()
                .stream()
                .map(teacherToTeacherDto::convert)
                .collect(Collectors.toList());
    }

    @Override
    public TeacherDto findTeacher(Long teacherId, Long classId) {
        return teacherToTeacherDto.convert(teacherRepository.findByIdAndSchoolClassId(teacherId, classId)
                .orElseThrow(() -> new NotFoundException("Form tutor not found")));
    }

    private Class getSchoolCLass(Long classId) {
        return classRepository
                .findById(classId).orElseThrow(() -> new NotFoundException("School class not found"));
    }
}
