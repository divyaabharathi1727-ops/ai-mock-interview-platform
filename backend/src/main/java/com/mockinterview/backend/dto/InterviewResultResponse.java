package com.mockinterview.backend.dto;

import com.mockinterview.backend.entity.InterviewDifficulty;
import com.mockinterview.backend.entity.InterviewStatus;
import java.time.LocalDateTime;
import java.util.List;

public class InterviewResultResponse {
    private Long id; private String jobRole; private String interviewType; private InterviewDifficulty difficulty;
    private InterviewStatus status; private int totalQuestions; private int answeredQuestions; private LocalDateTime completedAt;
    private Double overallScore; private Double technicalScore; private Double relevanceScore;
    private Double clarityScore; private Double completenessScore; private List<QuestionResultResponse> questions;
    public InterviewResultResponse(Long id, String jobRole, String interviewType, InterviewDifficulty difficulty,
            InterviewStatus status, int totalQuestions, int answeredQuestions, LocalDateTime completedAt,
            Double overallScore, Double technicalScore, Double relevanceScore, Double clarityScore,
            Double completenessScore, List<QuestionResultResponse> questions) {
        this.id = id; this.jobRole = jobRole; this.interviewType = interviewType; this.difficulty = difficulty;
        this.status = status; this.totalQuestions = totalQuestions; this.answeredQuestions = answeredQuestions;
        this.completedAt = completedAt; this.overallScore = overallScore; this.technicalScore = technicalScore;
        this.relevanceScore = relevanceScore; this.clarityScore = clarityScore; this.completenessScore = completenessScore;
        this.questions = questions;
    }
    public Long getId() { return id; } public String getJobRole() { return jobRole; }
    public String getInterviewType() { return interviewType; } public InterviewDifficulty getDifficulty() { return difficulty; }
    public InterviewStatus getStatus() { return status; } public int getTotalQuestions() { return totalQuestions; }
    public int getAnsweredQuestions() { return answeredQuestions; } public LocalDateTime getCompletedAt() { return completedAt; }
    public Double getOverallScore() { return overallScore; } public Double getTechnicalScore() { return technicalScore; }
    public Double getRelevanceScore() { return relevanceScore; } public Double getClarityScore() { return clarityScore; }
    public Double getCompletenessScore() { return completenessScore; } public List<QuestionResultResponse> getQuestions() { return questions; }
}
