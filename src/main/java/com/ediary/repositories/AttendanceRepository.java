package com.ediary.repositories;

import com.ediary.domain.Attendance;
import com.ediary.domain.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AttendanceRepository extends JpaRepository<Attendance, Long> {

    List<Attendance> findAllByStudentId(Long studentId);
    Attendance findByStudent(Student student);
}
