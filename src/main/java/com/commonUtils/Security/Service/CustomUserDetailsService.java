package com.commonUtils.Security.Service;

import com.commonUtils.Security.Repository.UserRespository;
import com.commonUtils.Security.Repository.UserSequenceRepository;
import com.commonUtils.dto.CustomUserDetails;
import com.commonUtils.dto.UserRequest;
import com.commonUtils.entity.User;
import com.commonUtils.entity.UserSequence;
import jakarta.persistence.PrePersist;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
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

    @Autowired
    private UserSequenceRepository userSequenceRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

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


    @Transactional
    public String generateUserId() {
        Integer nextId = userSequenceRepository.getNextSequence();

        // Insert into user_sequence to reserve the ID
        userSequenceRepository.save(new UserSequence());

        // Return user ID with "CU" prefix
        return "CU" + String.format("%04d", nextId); // CU0001, CU0002, etc.
    }
    @Transactional
    public UserRequest createUser(UserRequest userRequest){

        User newUser = new User();
        newUser.setUserId(generateUserId());
        newUser.setUserName(userRequest.getUserName());
        newUser.setRoles(userRequest.getRoles());
        newUser.setEmail(userRequest.getEmail());
        newUser.setPhoneNumber(userRequest.getPhoneNumber());
        newUser.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        newUser.setEnabled(false);
        User createdUser =  userRespository.save(newUser);
        userRequest.setUserId(createdUser.getUserId());

        return userRequest;
    }

}
