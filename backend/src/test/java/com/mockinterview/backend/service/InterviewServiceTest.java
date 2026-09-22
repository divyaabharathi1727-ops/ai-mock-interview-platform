package com.mockinterview.backend.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.mockinterview.backend.dto.InterviewCreateRequest;
import com.mockinterview.backend.dto.InterviewResponse;
import com.mockinterview.backend.dto.InterviewUpdateRequest;
import com.mockinterview.backend.entity.Interview;
import com.mockinterview.backend.entity.InterviewDifficulty;
import com.mockinterview.backend.entity.InterviewStatus;
import com.mockinterview.backend.entity.User;
import com.mockinterview.backend.exception.InterviewNotFoundException;
import com.mockinterview.backend.repository.InterviewRepository;
import com.mockinterview.backend.repository.UserRepository;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class InterviewServiceTest {

    @Mock
    private InterviewRepository interviewRepository;

    @Mock
    private UserRepository userRepository;

    private InterviewService interviewService;

    @BeforeEach
    void setUp() {
        interviewService = new InterviewService(interviewRepository, userRepository);
    }

    @Test
    void createInterview_shouldAssignAuthenticatedUserAndCreatedStatus() {
        User user = user("student@example.com");
        InterviewCreateRequest request = createRequest();
        when(userRepository.findByEmail("student@example.com")).thenReturn(Optional.of(user));
        when(interviewRepository.save(any(Interview.class))).thenAnswer(invocation -> {
            Interview interview = invocation.getArgument(0);
            interview.setId(1L);
            return interview;
        });

        InterviewResponse response = interviewService.createInterview("student@example.com", request);

        assertEquals(1L, response.getId());
        assertEquals(InterviewDifficulty.MEDIUM, response.getDifficulty());
        assertEquals(InterviewStatus.CREATED, response.getStatus());
    }

    @Test
    void getMyInterviews_shouldOnlyUseAuthenticatedEmail() {
        when(interviewRepository.findByUserEmailOrderByCreatedAtDesc("student@example.com"))
                .thenReturn(List.of(interview(1L, user("student@example.com"))));

        List<InterviewResponse> result = interviewService.getMyInterviews("student@example.com");

        assertEquals(1, result.size());
        verify(interviewRepository).findByUserEmailOrderByCreatedAtDesc("student@example.com");
    }

    @Test
    void getInterviewById_shouldRejectAnotherUsersInterview() {
        when(interviewRepository.findByIdAndUserEmail(1L, "other@example.com"))
                .thenReturn(Optional.empty());

        assertThrows(InterviewNotFoundException.class,
                () -> interviewService.getInterviewById("other@example.com", 1L));
    }

    @Test
    void updateInterview_shouldUpdateOwnedInterview() {
        Interview interview = interview(1L, user("student@example.com"));
        when(interviewRepository.findByIdAndUserEmail(1L, "student@example.com"))
                .thenReturn(Optional.of(interview));
        when(interviewRepository.save(interview)).thenReturn(interview);
        InterviewUpdateRequest request = new InterviewUpdateRequest();
        request.setJobRole("Backend Developer");
        request.setInterviewType("Mixed");
        request.setDifficulty("HARD");
        request.setStatus("IN_PROGRESS");

        InterviewResponse response = interviewService.updateInterview("student@example.com", 1L, request);

        assertEquals("Backend Developer", response.getJobRole());
        assertEquals(InterviewStatus.IN_PROGRESS, response.getStatus());
    }

    @Test
    void deleteInterview_shouldRejectAnotherUsersInterview() {
        when(interviewRepository.findByIdAndUserEmail(1L, "other@example.com"))
                .thenReturn(Optional.empty());

        assertThrows(InterviewNotFoundException.class,
                () -> interviewService.deleteInterview("other@example.com", 1L));
    }

    private InterviewCreateRequest createRequest() {
        InterviewCreateRequest request = new InterviewCreateRequest();
        request.setJobRole("Full Stack Developer");
        request.setInterviewType("Technical");
        request.setDifficulty("MEDIUM");
        return request;
    }

    private User user(String email) {
        User user = new User();
        user.setId(10L);
        user.setEmail(email);
        user.setName("Test Student");
        return user;
    }

    private Interview interview(Long id, User user) {
        Interview interview = new Interview();
        interview.setId(id);
        interview.setUser(user);
        interview.setJobRole("Java Developer");
        interview.setInterviewType("Technical");
        interview.setDifficulty(InterviewDifficulty.EASY);
        interview.setStatus(InterviewStatus.CREATED);
        return interview;
    }
}
