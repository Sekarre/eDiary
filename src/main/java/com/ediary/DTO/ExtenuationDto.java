package com.ediary.DTO;


import com.ediary.domain.Extenuation;
import lombok.*;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExtenuationDto {

    private Long id;
    private String description;
    private Extenuation.Status status;

    private String parentName;
    private Long parentId;

    private List<Long> attendancesId;
}
