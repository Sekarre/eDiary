package com.ediary.DTO;

import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ParentCouncilDto {

    private Long id;

    private String schoolClassName;
    private Long schoolClassId;
}
