package com.ediary.DTO;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SchoolDto {

    private Long id;

    private String name;
    private String headmasterName;
    private String headmasterLastName;
    private String email;
    private String schoolOffice;

    //Address
    private AddressDto addressDto;
}
