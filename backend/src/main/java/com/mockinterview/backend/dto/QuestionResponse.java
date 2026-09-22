package com.mockinterview.backend.dto;

import com.mockinterview.backend.entity.InterviewQuestionType;

public class QuestionResponse {

    private Long id;
    private String questionText;
    private Integer questionOrder;
    private InterviewQuestionType questionType;

    public QuestionResponse() { }

    public QuestionResponse(Long id, String questionText, Integer questionOrder,
                            InterviewQuestionType questionType) {
        this.id = id;
        this.questionText = questionText;
        this.questionOrder = questionOrder;
        this.questionType = questionType;
    }

    public Long getId() { return id; }
    public String getQuestionText() { return questionText; }
    public Integer getQuestionOrder() { return questionOrder; }
    public InterviewQuestionType getQuestionType() { return questionType; }
}
