package com.ediary.domain.timetable;

import com.ediary.domain.SchoolPeriod;
import lombok.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Timetable {

    private List<Duration> durations;
    private HashMap<Day, ArrayList<SchoolPeriod>> schedule;
}
