package com.testgl.Test_et_validation.service;

import com.testgl.Test_et_validation.exception.EmailAlreadyExistException;
import com.testgl.Test_et_validation.model.Users;
import com.testgl.Test_et_validation.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AuthService authService;

    @Test
    void ShouldRegisterUser() throws Exception {
        Users request = new Users(1L, "ken26@yahoo.fr", "12345678");
        Users savedUser = new Users(1L,"ken26@yahoo.fr", "12345678");

        when(userRepository.existsByEmail(request.getEmail())).thenReturn(false);
        when(userRepository.save(any(Users.class))).thenReturn(request);

        Users result = authService.register(request);

        assertEquals("ken26@yahoo.fr", result.getEmail());
        assertEquals("12345678", result.getPassword());
    }

    @Test
    void registerShouldFailWhenEmailAlreadyExists() {
        Users request = new Users(null, "test@gmail.com", "1234");

        when(userRepository.existsByEmail(request.getEmail())).thenReturn(true);

        assertThrows(
                EmailAlreadyExistException.class,
                () -> authService.register(request)
        );

        verify(userRepository, never()).save(any());
    }


    @Test
    void shouldLoginSuccessfully() throws Exception {
        String email = "test@gmail.com";
        String password = "password123";

        Users mockUser = new Users(1L, email, password);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(mockUser));

        Users result = authService.login(email, password);

        assertNotNull(result);
        assertEquals(email, result.getEmail());
        assertEquals(password, result.getPassword());
    }

    @Test
    void loginShouldFailForInvalidEmail() {
        when(userRepository.findByEmail("wrong@mail.com")).thenReturn(Optional.empty());

        Exception exception = assertThrows(
                Exception.class,
                () -> authService.login("wrong@mail.com", "12345678")
        );

        assertEquals("Invalid email or password", exception.getMessage());
    }

    @Test
    void loginShouldFailForInvalidPassword() {
        String email = "test@gmail.com";
        Users mockUser = new Users(1L, email, "correct_password");

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(mockUser));

        Exception exception = assertThrows(
                Exception.class,
                () -> authService.login(email, "wrong_password")
        );

        assertEquals("Invalid email or password", exception.getMessage());
    }
}