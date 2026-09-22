package com.mockinterview.backend.controller;

import static org.mockito.Mockito.when;
import com.mockinterview.backend.dto.UserResponse;
import com.mockinterview.backend.entity.UserRole;
import com.mockinterview.backend.service.AuthService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private AuthService authService;

    @Test
    void currentUser_shouldReturnAuthenticatedUser() {
        UserResponse response = new UserResponse(1L, "Test Student", "test@example.com", UserRole.STUDENT);
        when(authService.currentUser("test@example.com")).thenReturn(response);
        ResponseEntity<UserResponse> result = new UserController(authService).currentUser(
                new UsernamePasswordAuthenticationToken("test@example.com", null));

        org.junit.jupiter.api.Assertions.assertEquals(200, result.getStatusCode().value());
        org.junit.jupiter.api.Assertions.assertEquals("test@example.com", result.getBody().getEmail());
        org.junit.jupiter.api.Assertions.assertEquals(UserRole.STUDENT, result.getBody().getRole());
    }
}
