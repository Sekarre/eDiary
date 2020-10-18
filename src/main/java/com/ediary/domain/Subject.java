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

    @Singular
    @ManyToMany(cascade = {CascadeType.MERGE}, fetch = FetchType.EAGER)
    @JoinTable(name = "Teacher_has_Subject",
            joinColumns = {@JoinColumn(name = "Subject_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "Teacher_id", referencedColumnName = "id")})
    private Set<Teacher> teachers;

    @OneToMany(mappedBy = "subject")
    private Set<Lesson> lessons;
}
