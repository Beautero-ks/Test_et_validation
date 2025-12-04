package com.testgl.Test_et_validation.service;

import com.testgl.Test_et_validation.exception.EmailAlreadyExistException;
import com.testgl.Test_et_validation.model.Users;
import com.testgl.Test_et_validation.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;

    public AuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Users register(Users registerRequest) throws Exception {
        if(userRepository.existsByEmail(registerRequest.getEmail())){
            throw new EmailAlreadyExistException("Email already exists");
        }

        Users users = new Users();
        users.setEmail(registerRequest.getEmail());
        users.setPassword(registerRequest.getPassword());

        return userRepository.save(users);
    }

    public Users login(String email, String password) throws Exception{
        Users user = userRepository.findByEmail(email)
                .orElseThrow(()-> new Exception("Invalid email or password"));

        if(!user.getPassword().equals(password)){
            throw new Exception("Invalid email or password");
        }

        return user;
    }
}
