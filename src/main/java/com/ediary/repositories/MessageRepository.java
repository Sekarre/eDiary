package com.ediary.repositories;

import com.ediary.domain.Message;
import com.ediary.domain.security.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {

    List<Message> findAllByReadersOrderByDateDesc(User user);
    List<Message> findAllBySendersOrderByDateDesc(User user);
    List<Message> findAllByStatusAndReaders(Message.Status status, User user);

}
