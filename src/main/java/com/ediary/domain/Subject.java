package com.ediary.domain;

import lombok.*;

import javax.persistence.*;
import java.util.Set;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Subject {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    @ManyToMany(mappedBy = "subjects", fetch = FetchType.EAGER)
    private Set<Teacher> teachers;

    @OneToMany(mappedBy = "subject")
    private Set<Lesson> lessons;
}
