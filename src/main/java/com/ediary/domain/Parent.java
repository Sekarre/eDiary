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
public class Parent {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "parent", fetch = FetchType.EAGER)
    private Set<Student> students;

    @OneToMany(mappedBy = "parent", fetch = FetchType.EAGER)
    private Set<Extenuation> extenuations;
}
