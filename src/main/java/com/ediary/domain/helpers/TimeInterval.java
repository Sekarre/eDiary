package com.ediary.domain.helpers;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.sql.Date;


@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TimeInterval {

    @DateTimeFormat(pattern = "MM/yyyy")
    private Date startTime;

    @DateTimeFormat(pattern = "MM/yyyy")
    private Date endTime;
}
