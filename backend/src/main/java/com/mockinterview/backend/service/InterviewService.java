package com.mockinterview.backend.service;

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
import java.util.Locale;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class InterviewService {

    private final InterviewRepository interviewRepository;
    private final UserRepository userRepository;

    public InterviewService(InterviewRepository interviewRepository, UserRepository userRepository) {
        this.interviewRepository = interviewRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public InterviewResponse createInterview(String email, InterviewCreateRequest request) {
        User user = userRepository.findByEmail(normalizeEmail(email))
                .orElseThrow(() -> new InterviewNotFoundException("Authenticated user was not found"));
        Interview interview = new Interview();
        interview.setUser(user);
        interview.setJobRole(request.getJobRole().trim());
        interview.setInterviewType(request.getInterviewType().trim());
        interview.setDifficulty(parseDifficulty(request.getDifficulty()));
        interview.setStatus(InterviewStatus.CREATED);
        return toResponse(interviewRepository.save(interview));
    }

    @Transactional(readOnly = true)
    public List<InterviewResponse> getMyInterviews(String email) {
        return interviewRepository.findByUserEmailOrderByCreatedAtDesc(normalizeEmail(email))
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public InterviewResponse getInterviewById(String email, Long id) {
        return toResponse(findOwnedInterview(email, id));
    }

    @Transactional
    public InterviewResponse updateInterview(String email, Long id, InterviewUpdateRequest request) {
        Interview interview = findOwnedInterview(email, id);
        if (interview.getStatus() == InterviewStatus.COMPLETED) {
            throw new IllegalArgumentException("Completed interviews cannot be modified");
        }
        interview.setJobRole(request.getJobRole().trim());
        interview.setInterviewType(request.getInterviewType().trim());
        interview.setDifficulty(parseDifficulty(request.getDifficulty()));
        interview.setStatus(parseStatus(request.getStatus()));
        return toResponse(interviewRepository.save(interview));
    }

    @Transactional
    public void deleteInterview(String email, Long id) {
        Interview interview = findOwnedInterview(email, id);
        interviewRepository.delete(interview);
    }

    private Interview findOwnedInterview(String email, Long id) {
        return interviewRepository.findByIdAndUserEmail(id, normalizeEmail(email))
                .orElseThrow(() -> new InterviewNotFoundException("Interview not found"));
    }

    private InterviewDifficulty parseDifficulty(String value) {
        try {
            return InterviewDifficulty.valueOf(value.trim().toUpperCase(Locale.ROOT));
        } catch (Exception exception) {
            throw new IllegalArgumentException("Difficulty must be EASY, MEDIUM, or HARD");
        }
    }

    private InterviewStatus parseStatus(String value) {
        try {
            return InterviewStatus.valueOf(value.trim().toUpperCase(Locale.ROOT));
        } catch (Exception exception) {
            throw new IllegalArgumentException("Status must be CREATED, IN_PROGRESS, COMPLETED, or CANCELLED");
        }
    }

    private InterviewResponse toResponse(Interview interview) {
        return new InterviewResponse(
                interview.getId(),
                interview.getJobRole(),
                interview.getInterviewType(),
                interview.getDifficulty(),
                interview.getStatus(),
                interview.getCreatedAt(),
                interview.getUpdatedAt()
        );
    }

    private String normalizeEmail(String email) {
        return email == null ? null : email.trim().toLowerCase(Locale.ROOT);
    }
}
