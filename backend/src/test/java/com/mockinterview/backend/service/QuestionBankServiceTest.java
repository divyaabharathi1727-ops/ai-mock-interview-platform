package com.mockinterview.backend.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.mockinterview.backend.entity.InterviewQuestionType;
import java.util.Set;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;

class QuestionBankServiceTest {

    private final QuestionBankService questionBankService = new QuestionBankService();

    @Test
    void fallback_shouldProvideConfiguredDefaultCountAndMixedTypes() {
        var questions = questionBankService.getFallbackQuestions(
                "Java Developer", "Technical", "MEDIUM", 20);

        assertEquals(20, questions.size());
        Set<InterviewQuestionType> types = questions.stream()
                .map(AIService.GeneratedQuestion::questionType)
                .collect(Collectors.toSet());
        assertTrue(types.contains(InterviewQuestionType.TEXT));
        assertTrue(types.contains(InterviewQuestionType.MCQ));
        assertTrue(types.contains(InterviewQuestionType.MULTIPLE_SELECT));
        assertTrue(types.contains(InterviewQuestionType.TRUE_FALSE));
    }
}
