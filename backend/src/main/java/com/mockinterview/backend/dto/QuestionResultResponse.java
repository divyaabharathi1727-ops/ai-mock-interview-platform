package com.mockinterview.backend.dto;

import com.mockinterview.backend.entity.InterviewQuestionType;
import java.util.List;

public class QuestionResultResponse {
    private Long questionId;
    private String questionText;
    private InterviewQuestionType questionType;
    private List<String> options;
    private String candidateAnswer;
    private EvaluationResponse evaluation;
    private String status;

    public QuestionResultResponse(Long questionId, String questionText, InterviewQuestionType questionType,
                                  List<String> options, String candidateAnswer, EvaluationResponse evaluation) {
        this.questionId = questionId; this.questionText = questionText; this.questionType = questionType;
        this.options = options; this.candidateAnswer = candidateAnswer; this.evaluation = evaluation;
        this.status = evaluation == null ? "UNANSWERED" : evaluation.getCorrect() == null
            ? (evaluation.isAvailable() ? "EVALUATED" : "EVALUATION_UNAVAILABLE")
            : (evaluation.getCorrect() ? "CORRECT" : "INCORRECT");
    }
    public Long getQuestionId() { return questionId; }
    public String getQuestionText() { return questionText; }
    public InterviewQuestionType getQuestionType() { return questionType; }
    public List<String> getOptions() { return options; }
    public String getCandidateAnswer() { return candidateAnswer; }
    public EvaluationResponse getEvaluation() { return evaluation; }
    public String getStatus() { return status; }
}
