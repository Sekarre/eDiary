package com.ediary.domain.timetable;

import com.ediary.domain.SchoolPeriod;
import lombok.*;

import javax.persistence.*;
import java.sql.Time;
import java.util.Set;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Duration {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private int number;
    private Time startTime;
    private Time endTime;

    @OneToMany(mappedBy = "duration", fetch = FetchType.EAGER)
    private Set<SchoolPeriod> schoolPeriods;
}
