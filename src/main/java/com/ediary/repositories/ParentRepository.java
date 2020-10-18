package com.ediary.repositories;

import com.ediary.domain.Parent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ParentRepository extends JpaRepository<Parent, Long> {

    Parent findByUserId(Long userId);
}
