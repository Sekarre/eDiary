package com.ediary.domain.timetable;

import com.ediary.domain.SchoolPeriod;
import lombok.*;

import java.util.HashMap;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Timetable {

    private HashMap<Day, List<SchoolPeriod>> schedule;
}
