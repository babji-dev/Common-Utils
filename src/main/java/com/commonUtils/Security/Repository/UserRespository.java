package com.commonUtils.Security.Repository;

import com.commonUtils.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public
interface UserRespository extends JpaRepository<User, String> {

    Optional<User> findByUserName(String userName);

}



