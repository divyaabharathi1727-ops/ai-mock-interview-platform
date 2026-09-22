package com.mockinterview.backend.dto;

import com.mockinterview.backend.entity.InterviewStatus;
import java.time.LocalDateTime;

public class InterviewCompletionResponse {

    private InterviewStatus status;
    private int totalQuestions;
    private int answeredQuestions;
    private LocalDateTime completedAt;

    public InterviewCompletionResponse() { }

    public InterviewCompletionResponse(InterviewStatus status, int totalQuestions,
                                       int answeredQuestions, LocalDateTime completedAt) {
        this.status = status;
        this.totalQuestions = totalQuestions;
        this.answeredQuestions = answeredQuestions;
        this.completedAt = completedAt;
    }

    public InterviewStatus getStatus() { return status; }
    public int getTotalQuestions() { return totalQuestions; }
    public int getAnsweredQuestions() { return answeredQuestions; }
    public LocalDateTime getCompletedAt() { return completedAt; }
}
