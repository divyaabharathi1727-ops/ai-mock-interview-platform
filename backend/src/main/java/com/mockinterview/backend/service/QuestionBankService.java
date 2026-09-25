package com.mockinterview.backend.service;

import com.mockinterview.backend.entity.InterviewQuestionType;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import org.springframework.stereotype.Service;

@Service
public class QuestionBankService {

    public List<QuestionSeed> getDevelopmentQuestions(String jobRole, String interviewType) {
        return List.of(new QuestionSeed("Explain a core concept for this role.", InterviewQuestionType.TECHNICAL));
    }

    public List<AIService.GeneratedQuestion> getFallbackQuestions(String jobRole, String interviewType,
                                                                  String difficulty, int count) {
        String role = jobRole.toLowerCase(Locale.ROOT);
        InterviewQuestionType textType = interviewType.equalsIgnoreCase("HR")
                ? InterviewQuestionType.BEHAVIORAL : InterviewQuestionType.TEXT;
        String roleTopic = role.contains("java") ? "Java" : role.contains("frontend") ? "frontend" : "software engineering";
        List<AIService.GeneratedQuestion> bank = new ArrayList<>();
        bank.add(new AIService.GeneratedQuestion("Explain a core concept you would expect a " + roleTopic + " developer to know.", textType, List.of(), null, List.of()));
        bank.add(new AIService.GeneratedQuestion("Which practice most improves maintainability in a production codebase?", InterviewQuestionType.MCQ, List.of("Clear boundaries", "Duplicated logic", "Hidden global state", "Skipping tests"), "Clear boundaries", List.of()));
        bank.add(new AIService.GeneratedQuestion("Which are useful signals when diagnosing a production issue?", InterviewQuestionType.MULTIPLE_SELECT, List.of("Logs", "Metrics", "Traces", "Guesswork"), null, List.of("Logs", "Metrics", "Traces")));
        bank.add(new AIService.GeneratedQuestion("A good interview answer should explain its reasoning, not only its conclusion.", InterviewQuestionType.TRUE_FALSE, List.of("True", "False"), "True", List.of()));
        bank.add(new AIService.GeneratedQuestion("Describe how you would approach a new problem in this role at " + difficulty + " difficulty.", InterviewQuestionType.TEXT, List.of(), null, List.of()));
        bank.add(new AIService.GeneratedQuestion("Which choice best supports reliable delivery?", InterviewQuestionType.MCQ, List.of("Small tested changes", "Unreviewed large changes", "No monitoring", "Manual production edits"), "Small tested changes", List.of()));
        bank.add(new AIService.GeneratedQuestion("Which are examples of useful engineering trade-offs?", InterviewQuestionType.MULTIPLE_SELECT, List.of("Latency vs cost", "Speed vs maintainability", "Security vs convenience", "Ignoring requirements"), null, List.of("Latency vs cost", "Speed vs maintainability", "Security vs convenience")));
        bank.add(new AIService.GeneratedQuestion("Communication with stakeholders is part of effective engineering work.", InterviewQuestionType.TRUE_FALSE, List.of("True", "False"), "True", List.of()));
        bank.add(new AIService.GeneratedQuestion("Explain how you would test a feature before calling it complete.", InterviewQuestionType.TEXT, List.of(), null, List.of()));
        bank.add(new AIService.GeneratedQuestion("What is the best first response to an unclear requirement?", InterviewQuestionType.MCQ, List.of("Ask clarifying questions", "Build assumptions silently", "Ignore it", "Deploy immediately"), "Ask clarifying questions", List.of()));
        bank.add(new AIService.GeneratedQuestion("Which practices reduce regression risk?", InterviewQuestionType.MULTIPLE_SELECT, List.of("Automated tests", "Code review", "Observability", "Removing validation"), null, List.of("Automated tests", "Code review", "Observability")));
        bank.add(new AIService.GeneratedQuestion("A production fix should be verified after deployment.", InterviewQuestionType.TRUE_FALSE, List.of("True", "False"), "True", List.of()));
        bank.add(new AIService.GeneratedQuestion("Discuss a difficult technical decision and how you would communicate it.", InterviewQuestionType.TEXT, List.of(), null, List.of()));
        bank.add(new AIService.GeneratedQuestion("Which design choice usually makes a system easier to change?", InterviewQuestionType.MCQ, List.of("Loose coupling", "Hidden dependencies", "Duplicated configuration", "Unbounded scope"), "Loose coupling", List.of()));
        bank.add(new AIService.GeneratedQuestion("Which qualities matter in a production API?", InterviewQuestionType.MULTIPLE_SELECT, List.of("Clear contracts", "Validation", "Useful errors", "Undocumented breaking changes"), null, List.of("Clear contracts", "Validation", "Useful errors")));
        bank.add(new AIService.GeneratedQuestion("A security boundary should be enforced by the backend, not only the UI.", InterviewQuestionType.TRUE_FALSE, List.of("True", "False"), "True", List.of()));
        bank.add(new AIService.GeneratedQuestion("How would you explain a complex technical idea to a non-technical partner?", InterviewQuestionType.TEXT, List.of(), null, List.of()));
        bank.add(new AIService.GeneratedQuestion("Which approach best handles a failing dependency?", InterviewQuestionType.MCQ, List.of("Timeouts and clear fallback behavior", "Retry forever", "Hide the error", "Block all requests"), "Timeouts and clear fallback behavior", List.of()));
        bank.add(new AIService.GeneratedQuestion("Which are signs of a healthy team process?", InterviewQuestionType.MULTIPLE_SELECT, List.of("Feedback", "Shared ownership", "Visible priorities", "Blaming individuals"), null, List.of("Feedback", "Shared ownership", "Visible priorities")));
        bank.add(new AIService.GeneratedQuestion("A useful retrospective focuses on learning and improvement.", InterviewQuestionType.TRUE_FALSE, List.of("True", "False"), "True", List.of()));
        List<AIService.GeneratedQuestion> result = new ArrayList<>();
        for (int index = 0; index < count; index++) result.add(bank.get(index % bank.size()));
        return result;
    }

    public record QuestionSeed(String text, InterviewQuestionType type) { }
}
