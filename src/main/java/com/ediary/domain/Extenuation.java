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

    @Enumerated(value = EnumType.STRING)
    private Status status;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "parent_id")
    private Parent parent;

    @ManyToMany(mappedBy = "extenuations", fetch = FetchType.EAGER)
    private Set<Attendance> attendances;
}
