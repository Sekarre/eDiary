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
    private String address;
    private Boolean isEnabled;

    //Roles
    private List<String> rolesNames;
    private List<Long> rolesId;

}
