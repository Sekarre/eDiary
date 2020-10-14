package com.ediary.domain;

import lombok.*;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Lesson {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;
    private Date date;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "class_id")
    private Class schoolClass;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "topic_id")
    private Topic topic;

    @OneToMany(mappedBy = "lesson")
    private Set<Attendance> attendances;
}
