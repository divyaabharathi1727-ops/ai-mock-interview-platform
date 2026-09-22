package com.mockinterview.backend.dto;

import com.mockinterview.backend.entity.InterviewDifficulty;
import com.mockinterview.backend.entity.InterviewStatus;
import java.time.LocalDateTime;

public class InterviewResponse {

    private Long id;
    private String jobRole;
    private String interviewType;
    private InterviewDifficulty difficulty;
    private InterviewStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public InterviewResponse() { }

    public InterviewResponse(Long id, String jobRole, String interviewType,
                             InterviewDifficulty difficulty, InterviewStatus status,
                             LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.jobRole = jobRole;
        this.interviewType = interviewType;
        this.difficulty = difficulty;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Long getId() { return id; }
    public String getJobRole() { return jobRole; }
    public String getInterviewType() { return interviewType; }
    public InterviewDifficulty getDifficulty() { return difficulty; }
    public InterviewStatus getStatus() { return status; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
}
