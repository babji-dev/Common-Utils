package com.commonUtils.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "user_sequence")
public class UserSequence {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // Default constructor
    public UserSequence() {}

    public Integer getId() {
        return id;
    }
}