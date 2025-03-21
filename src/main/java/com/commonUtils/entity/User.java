package com.commonUtils.entity;

import com.commonUtils.Security.Service.CustomUserDetailsService;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;

@Entity(name = "User")
@Data
@Getter
@Setter
@NoArgsConstructor
public class User {

    @Id
    @Column(name = "userId",columnDefinition = "userId")
    private String userId;

    @Column(name = "userName")
    private String userName;

    @Column(name = "password")
    private String password;

    @Column(name = "email")
    private String email;

    @Column(name = "phoneNumber")
    private String phoneNumber ;

    @Column(name = "roles")
    private String roles;

    @Column(name = "enabled")
    private boolean enabled;

}
