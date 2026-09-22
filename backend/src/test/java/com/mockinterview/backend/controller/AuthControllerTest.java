package com.mockinterview.backend.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mockinterview.backend.dto.AuthResponse;
import com.mockinterview.backend.dto.LoginRequest;
import com.mockinterview.backend.dto.RegisterRequest;
import com.mockinterview.backend.dto.UserResponse;
import com.mockinterview.backend.entity.UserRole;
import com.mockinterview.backend.service.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    private AuthService authService;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(new AuthController(authService)).build();
    }

    @Test
    void register_shouldReturnCreatedStatus() throws Exception {
        RegisterRequest request = new RegisterRequest();
        request.setName("Test Student");
        request.setEmail("test@example.com");
        request.setPassword("password123");

        AuthResponse response = new AuthResponse();
        response.setToken("jwt-token");
        UserResponse user = new UserResponse();
        user.setId(1L);
        user.setName("Test Student");
        user.setEmail("test@example.com");
        user.setRole(UserRole.STUDENT);
        response.setUser(user);

        when(authService.register(any(RegisterRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.token").value("jwt-token"))
                .andExpect(jsonPath("$.user.email").value("test@example.com"));
    }

    @Test
    void login_shouldReturnOkStatus() throws Exception {
        LoginRequest request = new LoginRequest("test@example.com", "password123");

        AuthResponse response = new AuthResponse();
        response.setToken("jwt-token");
        UserResponse user = new UserResponse();
        user.setId(1L);
        user.setName("Test Student");
        user.setEmail("test@example.com");
        user.setRole(UserRole.STUDENT);
        response.setUser(user);

        when(authService.login(any(LoginRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("jwt-token"))
                .andExpect(jsonPath("$.user.role").value("STUDENT"));
    }

    @Test
    void register_shouldRejectInvalidRequest() throws Exception {
        RegisterRequest request = new RegisterRequest();
        request.setName("");
        request.setEmail("wrong-email");
        request.setPassword("short");

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }
}
