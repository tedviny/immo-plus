package com.ted.immo.service;

import com.ted.immo.model.User;
import com.ted.immo.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private User user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        user = new User();
        ReflectionTestUtils.setField(user, "id", 1L);
        user.setEmail("gilles@example.com");
        user.setPassword("plainPassword");
    }

    @Test
    void should_create_user_and_encode_password() {
        // Arrange
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        User savedUser = userService.createUser(user);

        // Assert
        assertThat(savedUser.getId()).isEqualTo(1L);
        assertThat(savedUser.getEmail()).isEqualTo("gilles@example.com");

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        assertTrue(encoder.matches("plainPassword", savedUser.getPassword()));

        verify(userRepository).save(any(User.class));
    }

    @Test
    void should_get_user_by_id() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        Optional<User> found = userService.getUserById(1L);

        assertThat(found).isPresent();
        assertThat(found.get().getId()).isEqualTo(1L);

        verify(userRepository).findById(1L);
    }

    @Test
    void should_get_user_by_email() {
        when(userRepository.findByEmail("gilles@example.com")).thenReturn(Optional.of(user));

        Optional<User> foundedUser = userService.getUserByEmail("gilles@example.com");

        assertThat(foundedUser).isPresent();
        assertThat(foundedUser.get().getEmail()).isEqualTo("gilles@example.com");

        verify(userRepository).findByEmail("gilles@example.com");
    }

    @Test
    void should_delete_user_by_id() {
        doNothing().when(userRepository).deleteById(1L);

        userService.deleteUser(1L);

        verify(userRepository).deleteById(1L);
    }
}