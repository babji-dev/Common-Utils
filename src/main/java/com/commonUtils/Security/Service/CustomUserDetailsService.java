package com.commonUtils.Security.Service;

import com.commonUtils.Security.Repository.UserRespository;
import com.commonUtils.dto.CustomUserDetails;
import com.commonUtils.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRespository userRespository;

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {

        Optional<User> opUser =  userRespository.findByUserName(userName);

        if(opUser.isPresent()){
            User user = opUser.get();
            String[] roles = user.getRoles().split(",");
            Set<GrantedAuthority> authorities = Arrays.stream(roles).map(role ->new SimpleGrantedAuthority(role))
                            .collect(Collectors.toSet());
            return new CustomUserDetails(opUser.get(),authorities);
        }

        throw new UsernameNotFoundException("User not found with userName "+userName);
    }



}
