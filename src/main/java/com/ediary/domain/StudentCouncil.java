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
public class StudentCouncil {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Singular
    @ManyToMany(mappedBy = "studentCouncils" , fetch = FetchType.EAGER)
    private Set<Student> students;

    @OneToOne(mappedBy = "studentCouncil")
    private Class schoolClass;

}
