package com.testgl.Test_et_validation.controller;

import com.testgl.Test_et_validation.exception.EmailAlreadyExistException;
import com.testgl.Test_et_validation.model.Users;
import com.testgl.Test_et_validation.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register (@RequestBody Users request) throws Exception {
        try {
            Users registerRequest = authService.register(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(registerRequest);
        } catch (EmailAlreadyExistException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login (@RequestParam String email, @RequestParam String password) throws Exception {
        try {
            Users loggedUser = authService.login(email, password);
            return ResponseEntity.ok(loggedUser);

        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", e.getMessage()));
        }
    }
}
