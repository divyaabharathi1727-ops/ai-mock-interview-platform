package com.mockinterview.backend.dto;

import com.mockinterview.backend.entity.InterviewDifficulty;
import com.mockinterview.backend.entity.InterviewStatus;
import java.time.LocalDateTime;

public class InterviewSessionResultsResponse {

    private Long id;
    private String jobRole;
    private String interviewType;
    private InterviewDifficulty difficulty;
    private InterviewStatus status;
    private int totalQuestions;
    private int answeredQuestions;
    private LocalDateTime completedAt;

    public InterviewSessionResultsResponse() { }

    public InterviewSessionResultsResponse(Long id, String jobRole, String interviewType,
                                           InterviewDifficulty difficulty, InterviewStatus status,
                                           int totalQuestions, int answeredQuestions,
                                           LocalDateTime completedAt) {
        this.id = id;
        this.jobRole = jobRole;
        this.interviewType = interviewType;
        this.difficulty = difficulty;
        this.status = status;
        this.totalQuestions = totalQuestions;
        this.answeredQuestions = answeredQuestions;
        this.completedAt = completedAt;
    }

    public Long getId() { return id; }
    public String getJobRole() { return jobRole; }
    public String getInterviewType() { return interviewType; }
    public InterviewDifficulty getDifficulty() { return difficulty; }
    public InterviewStatus getStatus() { return status; }
    public int getTotalQuestions() { return totalQuestions; }
    public int getAnsweredQuestions() { return answeredQuestions; }
    public LocalDateTime getCompletedAt() { return completedAt; }
}
