package com.mockinterview.backend.controller;

import com.mockinterview.backend.dto.InterviewCreateRequest;
import com.mockinterview.backend.dto.InterviewResponse;
import com.mockinterview.backend.dto.InterviewUpdateRequest;
import com.mockinterview.backend.service.InterviewService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/interviews")
public class InterviewController {

    private final InterviewService interviewService;

    public InterviewController(InterviewService interviewService) {
        this.interviewService = interviewService;
    }

    @PostMapping
    public ResponseEntity<InterviewResponse> createInterview(
            Authentication authentication, @Valid @RequestBody InterviewCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(interviewService.createInterview(authentication.getName(), request));
    }

    @GetMapping
    public ResponseEntity<List<InterviewResponse>> getMyInterviews(Authentication authentication) {
        return ResponseEntity.ok(interviewService.getMyInterviews(authentication.getName()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<InterviewResponse> getInterview(
            Authentication authentication, @PathVariable Long id) {
        return ResponseEntity.ok(interviewService.getInterviewById(authentication.getName(), id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<InterviewResponse> updateInterview(
            Authentication authentication,
            @PathVariable Long id,
            @Valid @RequestBody InterviewUpdateRequest request) {
        return ResponseEntity.ok(interviewService.updateInterview(authentication.getName(), id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInterview(Authentication authentication, @PathVariable Long id) {
        interviewService.deleteInterview(authentication.getName(), id);
        return ResponseEntity.noContent().build();
    }
}
