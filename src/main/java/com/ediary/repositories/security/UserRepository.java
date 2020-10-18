package com.ediary.repositories.security;

import com.ediary.domain.security.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByFirstNameAndLastName(String name, String lastName);
}
