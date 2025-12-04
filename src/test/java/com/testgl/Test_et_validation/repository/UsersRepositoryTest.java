package com.testgl.Test_et_validation.repository;

import com.testgl.Test_et_validation.model.Users;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Sql(scripts = "/data.sql")
class UsersRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    @Test
    void shouldGetAllUser(){
        List<Users> users = userRepository.findAll();

        assertNotNull(users);
        assertEquals(3, users.size());
        assertEquals("test@gmail.com", users.getFirst().getEmail());
    }

    @Test
    void shouldGetUserByEmail() {
        // Assert: Is from where I prepare my data (This is already done in data.sql)
        // Act: Call for the method I want to test
        // Assert: All assertions that should respect the Arrange

        Optional<Users> user = userRepository.findByEmail("test@gmail.com");
        assertTrue(user.isPresent());
        assertEquals("test@gmail.com", user.get().getEmail());
    }

    @Test
    void shouldGetUserById(){
        Users users = userRepository.findById(1L).get();
        assertEquals("test@gmail.com", users.getEmail());
    }

    @Test
    void shouldSaveUser(){
        Users users = new Users();
        users.setEmail("divan@yahoo.fr");
        users.setPassword("password123");

        Users savedUsers = userRepository.save(users);

        assertNotNull(savedUsers.getId());
        assertEquals("divan@yahoo.fr", savedUsers.getEmail());
        assertEquals("password123", savedUsers.getPassword());
    }

    @Test
    void shoudUpdateUserPasswordAndEmail() throws NoSuchElementException {
        try {
            Users users = userRepository.findById(3L).get();
            users.setEmail("ken25@yahoo.de");
            users.setPassword("password123");

            Users savedUsers = userRepository.save(users);

            assertEquals("password123", savedUsers.getPassword());
            assertEquals("ken25@yahoo.de",  savedUsers.getEmail());
        } catch (NoSuchElementException e) {
            throw new NoSuchElementException(e.getMessage());
        }
    }

    @Test
    void shouldDeleteUser(){
        userRepository.deleteById(1L);

        Optional<Users> user = userRepository.findById(1L);

        assertFalse(user.isPresent());
    }

}