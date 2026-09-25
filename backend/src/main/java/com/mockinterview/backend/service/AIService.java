package com.mockinterview.backend.service;

import com.mockinterview.backend.entity.InterviewQuestionType;
import java.util.List;

public interface AIService {

    List<GeneratedQuestion> generateQuestions(String jobRole, String interviewType,
                                              String difficulty, int questionCount);

    TextEvaluation evaluateTextAnswer(String jobRole, String interviewType, String difficulty,
                                      String question, String answer);

    record GeneratedQuestion(String questionText, InterviewQuestionType questionType,
                             List<String> options, String correctAnswer,
                             List<String> correctAnswers) { }

    record TextEvaluation(double overallScore, double technicalScore, double relevanceScore,
                          double clarityScore, double completenessScore, String feedback,
                          List<String> strengths, List<String> improvements) { }
}
