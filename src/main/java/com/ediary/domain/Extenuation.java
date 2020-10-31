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
public class Extenuation {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    public enum Status {
        SENT, ACCEPT, REJECT
    }

    private String description;

    @Enumerated(value = EnumType.STRING)
    private Status status;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "parent_id")
    private Parent parent;

    @Singular
    @ManyToMany(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    @JoinTable(name = "Extenuation_has_Attendance",
            joinColumns = {@JoinColumn(name = "Extenuation_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "Attendance_id", referencedColumnName = "id")})
    private Set<Attendance> attendances;
}
