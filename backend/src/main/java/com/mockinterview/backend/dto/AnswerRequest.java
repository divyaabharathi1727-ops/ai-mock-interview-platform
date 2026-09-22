package com.mockinterview.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class AnswerRequest {

    @NotBlank(message = "Answer is required")
    @Size(max = 10000, message = "Answer must be 10000 characters or less")
    private String answerText;

    public String getAnswerText() { return answerText; }
    public void setAnswerText(String answerText) { this.answerText = answerText; }
}
