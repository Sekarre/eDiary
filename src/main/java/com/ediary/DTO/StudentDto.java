package com.ediary.DTO;

import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentDto {

    private Long id;

    //User
    private String userName;

    //Class
    private String className;
}
