package com.mockinterview.backend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "answer_evaluations")
public class AnswerEvaluation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "answer_id", nullable = false, unique = true)
    private InterviewAnswer answer;

    private Double overallScore;
    private Double technicalScore;
    private Double relevanceScore;
    private Double clarityScore;
    private Double completenessScore;
    private Boolean correct;

    @Column(length = 4000)
    private String feedback;

    @Column(length = 4000)
    private String strengths;

    @Column(length = 4000)
    private String improvements;

    @Column(length = 4000)
    private String weaknesses;

    @Column(name = "improvement_suggestion", length = 4000)
    private String improvementSuggestion;

    @Column(name = "ideal_answer_guidance", length = 4000)
    private String idealAnswerGuidance;

    @Column(nullable = false)
    private boolean available;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public InterviewAnswer getAnswer() { return answer; }
    public void setAnswer(InterviewAnswer answer) { this.answer = answer; }
    public Double getOverallScore() { return overallScore; }
    public void setOverallScore(Double overallScore) { this.overallScore = overallScore; }
    public Double getTechnicalScore() { return technicalScore; }
    public void setTechnicalScore(Double technicalScore) { this.technicalScore = technicalScore; }
    public Double getRelevanceScore() { return relevanceScore; }
    public void setRelevanceScore(Double relevanceScore) { this.relevanceScore = relevanceScore; }
    public Double getClarityScore() { return clarityScore; }
    public void setClarityScore(Double clarityScore) { this.clarityScore = clarityScore; }
    public Double getCompletenessScore() { return completenessScore; }
    public void setCompletenessScore(Double completenessScore) { this.completenessScore = completenessScore; }
    public Boolean getCorrect() { return correct; }
    public void setCorrect(Boolean correct) { this.correct = correct; }
    public String getFeedback() { return feedback; }
    public void setFeedback(String feedback) { this.feedback = feedback; }
    public String getStrengths() { return strengths; }
    public void setStrengths(String strengths) { this.strengths = strengths; }
    public String getImprovements() { return improvements; }
    public void setImprovements(String improvements) { this.improvements = improvements; }
    public String getWeaknesses() { return weaknesses; }
    public void setWeaknesses(String weaknesses) { this.weaknesses = weaknesses; }
    public String getImprovementSuggestion() { return improvementSuggestion; }
    public void setImprovementSuggestion(String improvementSuggestion) { this.improvementSuggestion = improvementSuggestion; }
    public String getIdealAnswerGuidance() { return idealAnswerGuidance; }
    public void setIdealAnswerGuidance(String idealAnswerGuidance) { this.idealAnswerGuidance = idealAnswerGuidance; }
    public boolean isAvailable() { return available; }
    public void setAvailable(boolean available) { this.available = available; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
