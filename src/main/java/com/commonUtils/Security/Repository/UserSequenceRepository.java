package com.commonUtils.Security.Repository;

import com.commonUtils.entity.UserSequence;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserSequenceRepository extends JpaRepository<UserSequence, Integer> {

    @Query("SELECT COALESCE(MAX(id), 0) + 1 FROM UserSequence")
    Integer getNextSequence();
}