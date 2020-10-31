package com.ediary.domain;

import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Attendance {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    public enum Status {
        PRESENT, ABSENT, LATE, EXCUSED, UNEXCUSED
    }

    @Enumerated(value = EnumType.STRING)
    private Status status;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "lesson_id")
    private Lesson lesson;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "student_id")
    private Student student;

    @Singular
    @ManyToMany(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    @JoinTable(name = "Extenuation_has_Attendance",
            joinColumns = {@JoinColumn(name = "Attendance_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "Extenuation_id", referencedColumnName = "id")})
    private Set<Extenuation> extenuations;
}
