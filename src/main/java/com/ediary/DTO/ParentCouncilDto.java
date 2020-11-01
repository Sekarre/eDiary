package com.ediary.DTO;

import lombok.*;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ParentCouncilDto {

    private Long id;

    //Class
    private String schoolClassName;
    private Long schoolClassId;

    //Parents
    private List<String> parentsName;
    private List<Long> parentsId;
}
