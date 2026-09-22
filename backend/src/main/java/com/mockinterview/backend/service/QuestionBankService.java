package com.mockinterview.backend.service;

import com.mockinterview.backend.entity.InterviewQuestionType;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class QuestionBankService {

    public List<QuestionSeed> getDevelopmentQuestions(String jobRole, String interviewType) {
        InterviewQuestionType primaryType = resolveType(interviewType);
        String role = jobRole.toLowerCase();

        if (role.contains("java")) {
            return List.of(
                    new QuestionSeed("Explain the difference between an interface and an abstract class in Java.", primaryType),
                    new QuestionSeed("How would you design error handling for a Java service used by multiple clients?", InterviewQuestionType.SCENARIO),
                    new QuestionSeed("Tell us about a technical decision you made and how you evaluated its trade-offs.", InterviewQuestionType.BEHAVIORAL)
            );
        }
        if (role.contains("frontend")) {
            return List.of(
                    new QuestionSeed("How would you improve the performance of a slow client-rendered page?", primaryType),
                    new QuestionSeed("Describe how you would make a form accessible and resilient to validation errors.", InterviewQuestionType.SCENARIO),
                    new QuestionSeed("Tell us about a time you incorporated feedback into a user-facing feature.", InterviewQuestionType.BEHAVIORAL)
            );
        }
        if (role.contains("backend")) {
            return List.of(
                    new QuestionSeed("How would you design an API that remains reliable as traffic grows?", primaryType),
                    new QuestionSeed("What would you investigate first when a production endpoint becomes slow?", InterviewQuestionType.SCENARIO),
                    new QuestionSeed("Describe a backend change where you had to balance speed and maintainability.", InterviewQuestionType.BEHAVIORAL)
            );
        }
        return List.of(
                new QuestionSeed("Walk through how you would break down an unfamiliar engineering problem.", primaryType),
                new QuestionSeed("How would you validate that a proposed solution meets its users' needs?", InterviewQuestionType.SCENARIO),
                new QuestionSeed("Tell us about a time you learned something important from a project setback.", InterviewQuestionType.BEHAVIORAL)
        );
    }

    private InterviewQuestionType resolveType(String interviewType) {
        return switch (interviewType.trim().toUpperCase()) {
            case "HR" -> InterviewQuestionType.HR;
            case "BEHAVIORAL" -> InterviewQuestionType.BEHAVIORAL;
            default -> InterviewQuestionType.TECHNICAL;
        };
    }

    public record QuestionSeed(String text, InterviewQuestionType type) { }
}
