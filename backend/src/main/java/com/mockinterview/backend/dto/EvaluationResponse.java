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
    private List<String> weaknesses;
    private String improvementSuggestion;
    private String idealAnswerGuidance;

    public EvaluationResponse() { }
    public EvaluationResponse(boolean available, Boolean correct, Double overallScore, Double technicalScore,
                              Double relevanceScore, Double clarityScore, Double completenessScore, String feedback,
                              List<String> strengths, List<String> improvements) {
        this.available = available; this.correct = correct; this.overallScore = overallScore;
        this.technicalScore = technicalScore; this.relevanceScore = relevanceScore; this.clarityScore = clarityScore;
        this.completenessScore = completenessScore; this.feedback = feedback; this.strengths = strengths;
        this.improvements = improvements;
    }
    public void setStructuredFeedback(List<String> weaknesses, String improvementSuggestion,
                                      String idealAnswerGuidance) {
        this.weaknesses = weaknesses;
        this.improvementSuggestion = improvementSuggestion;
        this.idealAnswerGuidance = idealAnswerGuidance;
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
    public List<String> getWeaknesses() { return weaknesses; }
    public String getImprovementSuggestion() { return improvementSuggestion; }
    public String getIdealAnswerGuidance() { return idealAnswerGuidance; }
}
