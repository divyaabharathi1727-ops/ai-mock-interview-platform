package com.mockinterview.backend.dto;

import java.util.List;

public class InterviewAnalyticsResponse {
    private int totalInterviews;
    private int completedInterviews;
    private int inProgressInterviews;
    private Double averageScore;
    private Double bestScore;
    private Long averageDurationSeconds;
    private int totalQuestions;
    private int totalAnsweredQuestions;
    private int totalCorrectObjectiveAnswers;
    private int totalIncorrectObjectiveAnswers;
    private List<AnalyticsPoint> trend;
    private List<String> strongAreas;
    private List<String> practiceRecommendations;

    public InterviewAnalyticsResponse(int totalInterviews, int completedInterviews, int inProgressInterviews,
                                      Double averageScore, Double bestScore, Long averageDurationSeconds,
                                      int totalQuestions, int totalAnsweredQuestions,
                                      int totalCorrectObjectiveAnswers, int totalIncorrectObjectiveAnswers,
                                      List<AnalyticsPoint> trend, List<String> strongAreas,
                                      List<String> practiceRecommendations) {
        this.totalInterviews = totalInterviews; this.completedInterviews = completedInterviews;
        this.inProgressInterviews = inProgressInterviews; this.averageScore = averageScore;
        this.bestScore = bestScore; this.averageDurationSeconds = averageDurationSeconds;
        this.totalQuestions = totalQuestions; this.totalAnsweredQuestions = totalAnsweredQuestions;
        this.totalCorrectObjectiveAnswers = totalCorrectObjectiveAnswers;
        this.totalIncorrectObjectiveAnswers = totalIncorrectObjectiveAnswers; this.trend = trend;
        this.strongAreas = strongAreas; this.practiceRecommendations = practiceRecommendations;
    }
    public int getTotalInterviews() { return totalInterviews; }
    public int getCompletedInterviews() { return completedInterviews; }
    public int getInProgressInterviews() { return inProgressInterviews; }
    public Double getAverageScore() { return averageScore; }
    public Double getBestScore() { return bestScore; }
    public Long getAverageDurationSeconds() { return averageDurationSeconds; }
    public int getTotalQuestions() { return totalQuestions; }
    public int getTotalAnsweredQuestions() { return totalAnsweredQuestions; }
    public int getTotalCorrectObjectiveAnswers() { return totalCorrectObjectiveAnswers; }
    public int getTotalIncorrectObjectiveAnswers() { return totalIncorrectObjectiveAnswers; }
    public List<AnalyticsPoint> getTrend() { return trend; }
    public List<String> getStrongAreas() { return strongAreas; }
    public List<String> getPracticeRecommendations() { return practiceRecommendations; }

    public record AnalyticsPoint(String label, Double score, Long durationSeconds) { }
}
