package com.commonUtils.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
@NoArgsConstructor
public class UserRequest {

    private String userId;

    private String userName;

    private String password;

    private String email;

    private String phoneNumber ;

    private String roles;

}
