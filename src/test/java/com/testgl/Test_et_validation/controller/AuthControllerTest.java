package com.testgl.Test_et_validation.controller;

import com.testgl.Test_et_validation.Dto.RegisterRequest;
import com.testgl.Test_et_validation.exception.EmailAlreadyExistException;
import com.testgl.Test_et_validation.model.Users;
import com.testgl.Test_et_validation.repository.UserRepository;
import com.testgl.Test_et_validation.service.AuthService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
public class AuthControllerTest {
    @Autowired
    public MockMvc mockmvc;

    @MockitoBean
    private AuthService authService;

    @MockitoBean
    private UserRepository userRepository;

    // Successful registration with a valid email and password
    @Test
    public void testRegisterSuccess() throws Exception {
        RegisterRequest request = new RegisterRequest("test@gmail.com", "password");
        Users users = new Users(1L, "test@gmail.com", "password");

        // When register is called,
        when(authService.register(any(Users.class))).thenReturn(users);

        // Perform the post request and assert the response
        mockmvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.email").value("test@gmail.com"))
                .andExpect(jsonPath("$.password").value("password"));

    }

    // Register fail when Email already exists
    @Test
    public void testRegisterFail() throws Exception {
        // Given: A already exist user
        Users existingUsers = new Users();
        existingUsers.setId(1L);
        existingUsers.setPassword("old pass");
        existingUsers.setEmail("test@gmail.com");

        when(authService.register(any())).thenThrow(new EmailAlreadyExistException("Email already in use"));

        mockmvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                {
                    "email": "test@gmail.com",
                    "password": "newpassword"
                }
                """)
        )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Email already in use"));
    }

    @Test
    void loginShouldReturnUser() throws Exception {
        Users mockUser = new Users();
        mockUser.setId(1L);
        mockUser.setEmail("test@gmail.com");
        mockUser.setPassword("12345");

        Mockito.when(authService.login("test@gmail.com", "12345"))
                .thenReturn(mockUser);

        mockmvc.perform(post("/api/auth/login")
                        .param("email", "test@gmail.com")
                        .param("password", "12345"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("test@gmail.com"))
                .andExpect(jsonPath("$.id").value(1L));
    }

    // INVALID LOGIN (wrong password or email)
    @Test
    void loginShouldReturnErrorWhenCredentialsInvalid() throws Exception {
        Mockito.when(authService.login(anyString(), anyString()))
                .thenThrow(new Exception("Invalid email or password"));

        mockmvc.perform(post("/api/auth/login")
                        .param("email", "wrong@mail.com")
                        .param("password", "badpass"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.error").value("Invalid email or password"));

    }

}
