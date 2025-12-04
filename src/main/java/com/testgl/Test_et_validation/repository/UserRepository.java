package com.testgl.Test_et_validation.repository;

import com.testgl.Test_et_validation.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<Users, Long> {
    boolean existsByEmail(String email);

    Optional<Users> findByEmail(String mail);
}
