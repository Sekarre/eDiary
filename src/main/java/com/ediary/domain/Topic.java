package com.ediary.domain;

import lombok.*;

import javax.persistence.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Topic {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private int number;
    private String name;
    private String description;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "subject_id")
    private Subject subject;

}
