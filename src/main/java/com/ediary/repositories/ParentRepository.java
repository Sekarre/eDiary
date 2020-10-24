package com.ediary.repositories;

import com.ediary.domain.Parent;
import com.ediary.domain.security.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ParentRepository extends JpaRepository<Parent, Long> {

    Parent findByUserId(Long userId);
    Optional<Parent> findByUser(User user);
}
