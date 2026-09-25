package com.mockinterview.backend.dto;

import com.mockinterview.backend.entity.InterviewQuestionType;
import java.util.List;

public class QuestionResponse {

    private Long id;
    private String questionText;
    private Integer questionOrder;
    private InterviewQuestionType questionType;
    private List<String> options;

    public QuestionResponse() { }

    public QuestionResponse(Long id, String questionText, Integer questionOrder,
                            InterviewQuestionType questionType, List<String> options) {
        this.id = id;
        this.questionText = questionText;
        this.questionOrder = questionOrder;
        this.questionType = questionType;
        this.options = options;
    }

    public Long getId() { return id; }
    public String getQuestionText() { return questionText; }
    public Integer getQuestionOrder() { return questionOrder; }
    public InterviewQuestionType getQuestionType() { return questionType; }
    public List<String> getOptions() { return options; }
}
