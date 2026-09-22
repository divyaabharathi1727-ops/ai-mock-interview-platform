package com.mockinterview.backend.controller;

import com.mockinterview.backend.dto.AnswerRequest;
import com.mockinterview.backend.dto.AnswerResponse;
import com.mockinterview.backend.dto.InterviewCompletionResponse;
import com.mockinterview.backend.dto.InterviewSessionResponse;
import com.mockinterview.backend.dto.InterviewSessionResultsResponse;
import com.mockinterview.backend.dto.QuestionResponse;
import com.mockinterview.backend.service.InterviewQuestionService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/interviews/{interviewId}")
public class InterviewQuestionController {

    private final InterviewQuestionService questionService;

    public InterviewQuestionController(InterviewQuestionService questionService) {
        this.questionService = questionService;
    }

    @PostMapping("/start")
    public ResponseEntity<InterviewSessionResponse> start(
            Authentication authentication, @PathVariable Long interviewId) {
        return ResponseEntity.ok(questionService.startInterview(authentication.getName(), interviewId));
    }

    @GetMapping("/questions")
    public ResponseEntity<List<QuestionResponse>> getQuestions(
            Authentication authentication, @PathVariable Long interviewId) {
        return ResponseEntity.ok(questionService.getQuestions(authentication.getName(), interviewId));
    }

    @GetMapping("/questions/current")
    public ResponseEntity<InterviewSessionResponse> getCurrentQuestion(
            Authentication authentication, @PathVariable Long interviewId) {
        return ResponseEntity.ok(questionService.getCurrentQuestion(authentication.getName(), interviewId));
    }

    @PostMapping("/questions/{questionId}/answer")
    public ResponseEntity<AnswerResponse> submitAnswer(
            Authentication authentication,
            @PathVariable Long interviewId,
            @PathVariable Long questionId,
            @Valid @RequestBody AnswerRequest request) {
        return ResponseEntity.ok(questionService.submitAnswer(
                authentication.getName(), interviewId, questionId, request));
    }

    @PostMapping("/finish")
    public ResponseEntity<InterviewCompletionResponse> finish(
            Authentication authentication, @PathVariable Long interviewId) {
        return ResponseEntity.ok(questionService.finishInterview(authentication.getName(), interviewId));
    }

    @GetMapping("/results")
    public ResponseEntity<InterviewSessionResultsResponse> results(
            Authentication authentication, @PathVariable Long interviewId) {
        return ResponseEntity.ok(questionService.getResults(authentication.getName(), interviewId));
    }
}
