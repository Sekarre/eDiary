package com.ediary.DTO;

import lombok.*;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {

    private Long id;

    private String Name;
    private Long messageNumber;

    //Data for admin
    private String username;
    private String password;
    private Boolean isEnabled;

    //Address
    private AddressDto address;

    //Roles
    private List<String> rolesNames;
    private List<Long> rolesId;

}
