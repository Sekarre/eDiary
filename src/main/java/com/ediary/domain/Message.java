package com.ediary.domain;

import com.ediary.domain.security.User;
import lombok.*;

import javax.persistence.*;
import java.util.Set;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    public enum Status {
        SENT, READ
    }

    private String title;
    private String content;

    @Enumerated(value = EnumType.STRING)
    private Status status;


    @Singular
    @ManyToMany(cascade = {CascadeType.MERGE}, fetch = FetchType.EAGER)
    @JoinTable(name = "Message_has_User",
            joinColumns = {@JoinColumn(name = "Message_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "User_id", referencedColumnName = "id")})
    private Set<User> readers;


    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private User senders;

}
