package com.futurecollege.backend.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data // This generates getters/setters automatically
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(unique = true)
    private String email;

    private String password;
}
