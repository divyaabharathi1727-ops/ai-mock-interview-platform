package com.mockinterview.backend.dto;

import jakarta.validation.constraints.Size;
import java.util.List;

public class AnswerRequest {

    @Size(max = 10000, message = "Answer must be 10000 characters or less")
    private String answerText;

    private String selectedOption;
    private List<String> selectedOptions;

    public String getAnswerText() { return answerText; }
    public void setAnswerText(String answerText) { this.answerText = answerText; }
    public String getSelectedOption() { return selectedOption; }
    public void setSelectedOption(String selectedOption) { this.selectedOption = selectedOption; }
    public List<String> getSelectedOptions() { return selectedOptions; }
    public void setSelectedOptions(List<String> selectedOptions) { this.selectedOptions = selectedOptions; }
}
