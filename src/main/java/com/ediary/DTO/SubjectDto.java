package com.ediary.DTO;

import lombok.*;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SubjectDto {

    private Long id;
    private String name;

    private List<String> teachersName;
    private List<Long> teachersId;

    private List<String> lessonsName;
    private List<Long> lessonsId;

}
