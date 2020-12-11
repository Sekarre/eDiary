package com.ediary.repositories;

import com.ediary.domain.EndYearReport;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EndYearReportRepository  extends JpaRepository<EndYearReport, Long> {

    List<EndYearReport> findAllByUserTypeOrderByYearAsc(EndYearReport.Type type);

}
