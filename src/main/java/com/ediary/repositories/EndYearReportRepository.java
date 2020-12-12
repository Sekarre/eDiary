package com.ediary.repositories;

import com.ediary.domain.EndYearReport;
import com.ediary.domain.Student;
import com.ediary.domain.Teacher;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EndYearReportRepository  extends JpaRepository<EndYearReport, Long> {

    List<EndYearReport> findAllByUserTypeOrderByYearDesc(EndYearReport.Type type, Pageable pageable);
    Optional<EndYearReport> findByIdAndStudent(Long reportId, Student student);
    Optional<EndYearReport> findByIdAndTeacher(Long reportId, Teacher teacher);

}
