package com.ediary.services;

import com.ediary.domain.Attendance;
import com.ediary.domain.Parent;
import com.ediary.domain.Student;
import com.ediary.exceptions.NotFoundException;
import com.ediary.repositories.AttendanceRepository;
import com.ediary.repositories.ParentRepository;
import com.ediary.repositories.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@RequiredArgsConstructor
@Service
public class ParentServiceImpl implements ParentService {


    private final StudentRepository studentRepository;
    private final AttendanceRepository attendanceRepository;
    private final ParentRepository parentRepository;

    @Override
    public List<Student> listStudents(Long parentId) {

        Optional<Parent> parentOptional = parentRepository.findById(parentId);

        if (parentOptional.isEmpty()) {
            throw new NotFoundException("Parent Not Found. ");
        }

        Parent parent = parentOptional.get();


        return studentRepository.findAllByParentId(parent.getId());
    }

    @Override
    public Attendance saveAttendance(Attendance attendance) {
        return attendanceRepository.save(attendance);
    }
}
