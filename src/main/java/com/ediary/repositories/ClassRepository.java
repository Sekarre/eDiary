package com.ediary.repositories;

import com.ediary.domain.Class;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClassRepository extends JpaRepository<Class, Long> {

    Class findByName(String name);
}
