package com.mockinterview.backend.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.mockinterview.backend.dto.AuthResponse;
import com.mockinterview.backend.dto.LoginRequest;
import com.mockinterview.backend.dto.RegisterRequest;
import com.mockinterview.backend.entity.User;
import com.mockinterview.backend.entity.UserRole;
import com.mockinterview.backend.exception.DuplicateEmailException;
import com.mockinterview.backend.exception.InvalidCredentialsException;
import com.mockinterview.backend.repository.UserRepository;
import com.mockinterview.backend.security.JwtService;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtService jwtService;

    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthService authService;

    @BeforeEach
    void setUp() {
        passwordEncoder = new BCryptPasswordEncoder();
        authService = new AuthService(userRepository, passwordEncoder, jwtService);
    }

    @Test
    void register_shouldCreateUserWithStudentRoleAndEncodedPassword() {
        RegisterRequest request = new RegisterRequest();
        request.setName("Test Student");
        request.setEmail("test@example.com");
        request.setPassword("password123");

        when(userRepository.existsByEmail("test@example.com")).thenReturn(false);
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User user = invocation.getArgument(0);
            user.setId(1L);
            return user;
        });
        when(jwtService.generateToken("test@example.com")).thenReturn("jwt-token");

        AuthResponse response = authService.register(request);

        assertNotNull(response);
        assertEquals("jwt-token", response.getToken());
        assertEquals("Test Student", response.getUser().getName());
        assertEquals("test@example.com", response.getUser().getEmail());
        assertEquals(UserRole.STUDENT, response.getUser().getRole());
        assertNotEquals("password123", response.getUser().getRole().name());
        verify(userRepository).save(any(User.class));
    }

    @Test
    void register_shouldRejectDuplicateEmail() {
        RegisterRequest request = new RegisterRequest();
        request.setName("Test Student");
        request.setEmail("test@example.com");
        request.setPassword("password123");

        when(userRepository.existsByEmail("test@example.com")).thenReturn(true);

        assertThrows(DuplicateEmailException.class, () -> authService.register(request));
    }

    @Test
    void login_shouldAuthenticateValidUser() {
        User user = new User();
        user.setId(1L);
        user.setName("Test Student");
        user.setEmail("test@example.com");
        user.setPassword(passwordEncoder.encode("password123"));
        user.setRole(UserRole.STUDENT);

        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));
        when(jwtService.generateToken("test@example.com")).thenReturn("jwt-token");

        AuthResponse response = authService.login(new LoginRequest("test@example.com", "password123"));

        assertNotNull(response);
        assertEquals("jwt-token", response.getToken());
        assertEquals("test@example.com", response.getUser().getEmail());
    }

    @Test
    void login_shouldRejectInvalidCredentials() {
        User user = new User();
        user.setId(1L);
        user.setEmail("test@example.com");
        user.setPassword(passwordEncoder.encode("correct-password"));
        user.setRole(UserRole.STUDENT);

        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));

        assertThrows(InvalidCredentialsException.class,
                () -> authService.login(new LoginRequest("test@example.com", "wrong-password")));
    }
}
