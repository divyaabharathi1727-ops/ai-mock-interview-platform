package com.mockinterview.backend.controller;

import com.mockinterview.backend.dto.InterviewAnalyticsResponse;
import com.mockinterview.backend.service.InterviewQuestionService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/interviews")
public class InterviewAnalyticsController {

    private final InterviewQuestionService questionService;

    public InterviewAnalyticsController(InterviewQuestionService questionService) {
        this.questionService = questionService;
    }

    @GetMapping("/analytics")
    public ResponseEntity<InterviewAnalyticsResponse> analytics(Authentication authentication) {
        return ResponseEntity.ok(questionService.getAnalytics(authentication.getName()));
    }
}
