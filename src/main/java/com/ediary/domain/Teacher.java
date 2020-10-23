package com.ediary.domain;

import com.ediary.domain.security.User;
import lombok.*;

import javax.persistence.*;
import java.util.Set;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Teacher {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "user_id")
    private User user;


    @OneToOne(mappedBy = "teacher", fetch = FetchType.EAGER)
    private Class schoolClass;

    @OneToMany(mappedBy = "teacher", fetch = FetchType.EAGER)
    private Set<Grade> grades;

    @OneToMany(mappedBy = "teacher", fetch = FetchType.EAGER)
    private Set<Subject> subjects;

    @OneToMany(mappedBy = "teacher", fetch = FetchType.EAGER)
    private Set<Event> events;

    @OneToMany(mappedBy = "teacher", fetch = FetchType.EAGER)
    private Set<SchoolPeriod> schoolPeriods;

    @OneToMany(mappedBy = "teacher", fetch = FetchType.EAGER)
    private Set<StudentCard> studentCards;

    @OneToMany(mappedBy = "teacher", fetch = FetchType.EAGER)
    private Set<Behavior> behaviors;

    @OneToMany(mappedBy = "teacher", fetch = FetchType.EAGER)
    private Set<Report> teacherReports;

    @OneToMany(mappedBy = "headmaster", fetch = FetchType.EAGER)
    private Set<Report> headmasterReports;

}
