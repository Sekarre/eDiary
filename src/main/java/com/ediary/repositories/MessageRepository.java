package com.ediary.repositories;

import com.ediary.domain.Message;
import com.ediary.domain.security.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {

    List<Message> findAllByReaders(User user);
    List<Message> findAllBySenders(User user);

}
