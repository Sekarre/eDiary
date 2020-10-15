package com.ediary.services;

import com.ediary.domain.Class;

import java.util.List;

public interface DeputyHeadService {

    Class saveClass(Class schoolClass);
    Boolean deleteClass(Long schoolClassId);
    List<Class> getAllClasses();

}
