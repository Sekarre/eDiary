package com.ediary.web.controllers.api.v1;

import com.ediary.domain.SchoolPeriod;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(TimetableController.BASE_URL)
public class TimetableController {

    public static final String BASE_URL = "/api/v1/timetable";

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public SchoolPeriod getSchoolPeriod(){
        //TODO impl
        return new SchoolPeriod();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    //TODO impl
    public SchoolPeriod createOrUpdateSchoolPeriod(@RequestBody SchoolPeriod schoolPeriod){
        return new SchoolPeriod();
    }

}
