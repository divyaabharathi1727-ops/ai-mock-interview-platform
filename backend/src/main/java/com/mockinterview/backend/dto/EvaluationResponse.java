package com.mockinterview.backend.dto;

import java.util.List;

public class EvaluationResponse {
    private boolean available;
    private Boolean correct;
    private Double overallScore;
    private Double technicalScore;
    private Double relevanceScore;
    private Double clarityScore;
    private Double completenessScore;
    private String feedback;
    private List<String> strengths;
    private List<String> improvements;

    public EvaluationResponse() { }
    public EvaluationResponse(boolean available, Boolean correct, Double overallScore, Double technicalScore,
                              Double relevanceScore, Double clarityScore, Double completenessScore, String feedback,
                              List<String> strengths, List<String> improvements) {
        this.available = available; this.correct = correct; this.overallScore = overallScore;
        this.technicalScore = technicalScore; this.relevanceScore = relevanceScore; this.clarityScore = clarityScore;
        this.completenessScore = completenessScore; this.feedback = feedback; this.strengths = strengths;
        this.improvements = improvements;
    }
    public boolean isAvailable() { return available; }
    public Boolean getCorrect() { return correct; }
    public Double getOverallScore() { return overallScore; }
    public Double getTechnicalScore() { return technicalScore; }
    public Double getRelevanceScore() { return relevanceScore; }
    public Double getClarityScore() { return clarityScore; }
    public Double getCompletenessScore() { return completenessScore; }
    public String getFeedback() { return feedback; }
    public List<String> getStrengths() { return strengths; }
    public List<String> getImprovements() { return improvements; }
}
