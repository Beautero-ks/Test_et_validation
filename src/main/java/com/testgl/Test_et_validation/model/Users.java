package com.testgl.Test_et_validation.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "users")
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;
    private String password;

    public Users() {}

    public Users(Long id, String email, String password) {
        this.id = id;
        this.email = email;
        this.password = password;
    }
}
