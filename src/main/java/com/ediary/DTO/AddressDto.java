package com.ediary.DTO;

import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddressDto {

    private Long id;

    private String street;
    private String city;
    private String state;
    private String zip;
    private String phoneNumber;

    //User
    private Long userId;
}
