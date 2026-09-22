package com.mockinterview.backend.dto;

import jakarta.validation.constraints.NotBlank;

public class InterviewCreateRequest {

    @NotBlank(message = "Job role is required")
    private String jobRole;

    @NotBlank(message = "Interview type is required")
    private String interviewType;

    @NotBlank(message = "Difficulty is required")
    private String difficulty;

    public String getJobRole() { return jobRole; }
    public void setJobRole(String jobRole) { this.jobRole = jobRole; }
    public String getInterviewType() { return interviewType; }
    public void setInterviewType(String interviewType) { this.interviewType = interviewType; }
    public String getDifficulty() { return difficulty; }
    public void setDifficulty(String difficulty) { this.difficulty = difficulty; }
}
